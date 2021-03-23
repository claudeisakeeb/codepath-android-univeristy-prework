package com.example.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEM_NAME = "item_name";
    public static final String KEY_ITEM_PRIORITY = "item_priority";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 20;

    List<Item> items;
    Button btnAdd;
    EditText etItem;
    EditText itemPrio;
    RecyclerView rvItems;
    ItemsAdapter itemsAdapter;

    public int binarySearch(int p) {
        int left = 0, right = items.size();
        Log.d("MainActivity", "Single clicked at position " + right);
        while (left < right) {
            int middle = left + (right - left) / 2;
            int current = items.get(middle).getPriority();
            if (current > p) {
                left = middle + 1;
            } else {
                right = middle;
            }
        }
        return left;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        itemPrio = findViewById(R.id.priority);
        etItem = findViewById(R.id.etItem);
        rvItems = findViewById(R.id.rvItems);

        loadItems();

        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                // Delete the item from the model
                items.remove(position);
                // Notify the adapter
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item was removed", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };

        ItemsAdapter.OnClickListener onClickListener = new ItemsAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                 Log.d("MainActivity", "Single clicked at position " + position);
                 Intent data = new Intent(MainActivity.this, EditActivity.class);
                 // Pass the data being edited
                 data.putExtra(KEY_ITEM_NAME, items.get(position).getName());
                 data.putExtra(KEY_ITEM_PRIORITY, String.valueOf(items.get(position).getPriority()));
                 data.putExtra(KEY_ITEM_POSITION, position);
                // Display the activity
                startActivityForResult(data, EDIT_TEXT_CODE);
            }
        };

        ItemsAdapter.OnCompleteClickListener onCompleteClickListener = new ItemsAdapter.OnCompleteClickListener() {
            @Override
            public void onCompleteClicked(int position) {
                items.get(position).completeItem();
                itemsAdapter.notifyItemChanged(position);
                Toast.makeText(getApplicationContext(), "Item completed", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };

        itemsAdapter = new ItemsAdapter(items, onLongClickListener, onClickListener, onCompleteClickListener);
        rvItems.setLayoutManager(new LinearLayoutManager(this));
        rvItems.setAdapter(itemsAdapter);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String itemName = etItem.getText().toString();
                if (itemName.equals("")) {
                    Toast.makeText(getApplicationContext(), "Item name can't be empty", Toast.LENGTH_SHORT).show();
                } else {
                    String itemPriority = itemPrio.getText().toString();
                    int itemP = itemPriority.equals("") ? 1 : Integer.parseInt(itemPriority);
                    // Add item to model
                    int index = binarySearch(itemP);
                    items.add(index, new Item(itemName, itemP));
                    // Notify adapter that an item is inserted
                    itemsAdapter.notifyItemInserted(index);
                    etItem.setText("");
                    itemPrio.setText("");
                    Toast.makeText(getApplicationContext(), "Item was added", Toast.LENGTH_SHORT).show();
                    saveItems();
                }
            }
        });
    }

    // Handle result of the edit activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_TEXT_CODE && resultCode == RESULT_OK) {
            // Retrieve updated text value
            String itemName = data.getStringExtra(KEY_ITEM_NAME);
            int itemPriority = Integer.parseInt(data.getStringExtra(KEY_ITEM_PRIORITY));
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);
            // Extract original position of the edited item
            items.remove(position);
            itemsAdapter.notifyItemRemoved(position);
            int index = binarySearch(itemPriority);
            items.add(index, new Item(itemName, itemPriority));
            itemsAdapter.notifyItemInserted(index);
            saveItems();
            Toast.makeText(getApplicationContext(), "Item updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            Log.w("MainActivity", "Unknown call to onActivityResult");
        }
    }

    private File getItemNames() {
        return new File(getFilesDir(), "names.txt");
    }
    private File getItemPrios() {
        return new File(getFilesDir(), "prios.txt");
    }
    private File getItemCompleteDates() {
        return new File(getFilesDir(), "dates.txt");
    }

    // Load items by reading every line of the data file
    public void loadItems() {
        try {
            ArrayList<String> itemNames = new ArrayList<>(FileUtils.readLines(getItemNames(), Charset.defaultCharset()));
            ArrayList<String> itemPriorities = new ArrayList<>(FileUtils.readLines(getItemPrios(), Charset.defaultCharset()));
            ArrayList<String> itemCompleteDates = new ArrayList<>(FileUtils.readLines(getItemCompleteDates(), Charset.defaultCharset()));
            items = new ArrayList<>();
            for (int i = 0; i < itemNames.size(); i++) {
                items.add(new Item(new String(itemNames.get(i)), new Integer(itemPriorities.get(i)), new String(itemCompleteDates.get(i))));
            }
        } catch(IOException e) {
            Log.e("MainActivity", "Error reading items", e);
            items = new ArrayList<>();
        }
    }
    // This function saves items by writing them into the data file
    private void saveItems() {
        try {
            ArrayList<String> itemNames = new ArrayList<>();
            ArrayList<String> itemPrios = new ArrayList<>();
            ArrayList<String> itemCompleteDates = new ArrayList<>();
            for (int i = 0; i < items.size(); i++) {
                itemNames.add(new String(items.get(i).getName()));
                itemPrios.add(Integer.toString(items.get(i).getPriority()));
                itemCompleteDates.add(new String(items.get(i).getCompleteDate()));
            }
            FileUtils.writeLines(getItemNames(), itemNames);
            FileUtils.writeLines(getItemPrios(), itemPrios);
            FileUtils.writeLines(getItemCompleteDates(), itemCompleteDates);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing items", e);
        }
    }
}