package com.example.todolist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// Responsible for displaying data from the model into a row in the recycler view
public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder>{

    public interface OnLongClickListener {
        void onItemLongClicked(int position);
    }

    public interface OnClickListener {
        void onItemClicked(int position);
    }

    public interface OnCompleteClickListener {
        void onCompleteClicked(int position);
    }

    List<Item> items;
    OnLongClickListener longClickListener;
    OnClickListener clickListener;
    OnCompleteClickListener completeClickListener;

    public ItemsAdapter(List<Item> items, OnLongClickListener longClickListener, OnClickListener clickListener, OnCompleteClickListener completeClickListener ) {
        this.items = items;
        this.longClickListener = longClickListener;
        this.clickListener = clickListener;
        this.completeClickListener = completeClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Use layout inflator to inflate a view
        View todoView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent,   false);
        // Wrap it inside a View Holder and return it
        return new ViewHolder(todoView);
    }

    // Binds data to a particular view holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Grab the item at the position
        Item item = items.get(position);
        // Bind the item into the specified view holder
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    //Container to provide easy access to views that represent each row of the list
    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvItem;
        TextView tvCompleteDate;
        Button tvButton;
        RelativeLayout itemBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(R.id.item_name);
            tvCompleteDate = itemView.findViewById(R.id.item_complete_date);
            tvButton = itemView.findViewById(R.id.completeButton);
            itemBar = itemView.findViewById(R.id.itemBar);
        }

        // Update the view inside of the view holder with this data
        public void bind(Item item) {
            tvItem.setText(item.getName() + " (" + item.getPriority() + ")");
            tvCompleteDate.setText("Completed on " + item.getCompleteDate());
            boolean itemCompleted = !item.getCompleteDate().equals("");
            if (itemCompleted) {
                tvButton.setVisibility(View.INVISIBLE);
                tvCompleteDate.setVisibility(View.VISIBLE);
            }
            itemBar.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    // Notify the listener which position was long pressed
                    longClickListener.onItemLongClicked(getAdapterPosition());
                    return true;
                }
            });
            itemBar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClicked(getAdapterPosition());
                }
            });
            tvButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    completeClickListener.onCompleteClicked(getAdapterPosition());
                }
            });
        }
    }
}
