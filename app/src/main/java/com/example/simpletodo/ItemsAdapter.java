package com.example.simpletodo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

//Responsible for displaying data from the model into row in the recycler view
public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder>{

    public interface OnClickListener{
        void onItemClicked(int position);
    }

    public interface OnLongClickListener {
        void onItemLongClicked(int position);
    }

    List<String> items;
    OnLongClickListener longClickListener;
    OnClickListener clickListener;

    public ItemsAdapter(List<String> items, OnLongClickListener longClickListener, OnClickListener clickListener) {
        this.items = items; //setting member variable to the variable passed in through the constructor
        this.longClickListener = longClickListener;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    //responsible for creating each view
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Use layout inflator to inflate a view
        View todoView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_expandable_list_item_1,  parent, false);
        //Wrap it inside a View Holder and return it
        return new ViewHolder(todoView);
    }

    //responsible for taking data at a particular position and putting it into a View Holder
    //responsible for binding data to a particular View Holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Grab the item at the position
        String item = items.get(position);
        //Bind the item into the specified View Holder
        holder.bind(item);
    }

    //The number of Items available in the data
    //Tells the Recycler View how many items are on the list
    @Override
    public int getItemCount() {
        return items.size();
    }

    //define View Holder - Container to provide easy access to views that represent each row of the list
    class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(android.R.id.text1);
        }

        //responsible for updating view inside of the View Holder with the data of String item
        public void bind(String item) {
            tvItem.setText(item);
            tvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                clickListener.onItemClicked(getAbsoluteAdapterPosition());
                                      }
            });
            tvItem.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v){
                    //Notify the listner which position was long pressed
                    longClickListener.onItemLongClicked(getAdapterPosition());
                    return true; //Means callback is consuming the long click
                }
            });
        }
    }
}
