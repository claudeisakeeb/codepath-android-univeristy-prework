package com.example.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditActivity extends AppCompatActivity {
    EditText etItem;
    EditText etPrio;
    Button btnSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        etItem = findViewById(R.id.etItem);
        etPrio= findViewById(R.id.etPrio);
        btnSave = findViewById(R.id.btnSave);

        getSupportActionBar().setTitle("Edit item");

        etItem.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_NAME));
        etPrio.setText(getIntent().getStringExtra(MainActivity.KEY_ITEM_PRIORITY));

        // Save button is clicked after user is done editing
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create an intent which will contain user edits
                Intent data = new Intent();
                // Pass the results to MainActivity
                String itemName = etItem.getText().toString();
                if (itemName.equals("")) {
                    Toast.makeText(getApplicationContext(), "Item name can't be empty", Toast.LENGTH_SHORT).show();
                } else {
                    String itemPriority = etPrio.getText().toString().equals("") ? "1" : etPrio.getText().toString();
                    data.putExtra(MainActivity.KEY_ITEM_NAME, itemName);
                    data.putExtra(MainActivity.KEY_ITEM_PRIORITY, itemPriority);
                    data.putExtra(MainActivity.KEY_ITEM_POSITION, getIntent().getExtras().getInt(MainActivity.KEY_ITEM_POSITION));
                    // Set the result of the intent
                    setResult(RESULT_OK, data);
                    // Finish activity
                    finish();
                }
            }
        });
    }
}