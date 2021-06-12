package com.example.simpletodo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import org.apache.commons.io.FileUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    public static final String KEY_TASK_TEXT = "task_text";
    public static final String KEY_TASK_POSITION = "task_position";
    public static final int EDIT_TEXT_CODE = 20;

    List<String> items;

    //handlers for xml components
    ImageButton btnAdd;
    EditText etItem;
    RecyclerView rvItems;
    ItemsAdapter itemsAdapter;
    ImageButton homebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnAdd = findViewById(R.id.btnAdd);
        etItem = findViewById(R.id.etItem);
        rvItems = findViewById(R.id.rvItems);
        homebtn = (ImageButton) findViewById(R.id.homebtn);

        loadItems();

        /*
        //mock data
        items.add("Buy coconut milk");
        items.add("go to the gym");
        items.add("have fun!");
    */

        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener() {
            @Override
            public void onItemLongClicked(int position) {
                //Delete the item from the model
                items.remove(position);
                //notify the adapter at which position we deleted the item
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Task removed!", Toast.LENGTH_SHORT).show();
                saveItems();
;            }
        };

        ItemsAdapter.OnClickListener onClickListener = new ItemsAdapter.OnClickListener() {
            @Override
            public void onItemClicked(int position) {
                Log.d("MainActivity","Single click at position " + position);
                //create the new activity - using intent (request to the android system)
                Intent i = new Intent(MainActivity.this, EditActivity.class);
                //pass the data bring edited
                i.putExtra(KEY_TASK_TEXT, items.get(position));
                i.putExtra(KEY_TASK_POSITION, position);
                //display the activity
                startActivityForResult(i, 2);
            }
        };

        itemsAdapter = new ItemsAdapter(items, onLongClickListener, onClickListener);
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todoItem = etItem.getText().toString();

                //Add item to the model
                items.add(todoItem);
                //Notify adapter that we inserted an item
                itemsAdapter.notifyItemInserted(items.size() - 1);
                etItem.setText("");
                //adding a success popup
                Toast.makeText(getApplicationContext(), "Task added!", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });

        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToHomePage();

            }
        });
    }

        private void goToHomePage() {
            Intent intent = new Intent(this, HomePageActivity.class);
            startActivity(intent);
        }

    //handles the result of the edit activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK /*&& requestCode == EDIT_TEXT_CODE*/) {
            //Retrieve the updated text value
            String itemText = data.getStringExtra(KEY_TASK_TEXT);
            //extract the original position of the edited item from the position key
            int position = data.getExtras().getInt(KEY_TASK_POSITION);

            //update the model at the right position with the new task text
            items.set(position, itemText);
            //notify the adapter
            itemsAdapter.notifyItemChanged(position);
            //persist the changes
            saveItems();
            Toast.makeText(getApplicationContext(), "Task updated successfully!", Toast.LENGTH_SHORT).show();
        } else {

            Log.w("MainActivity", "Unknown call to onActivityResult");
        }

    }

    private File getDataFile(){
        return new File(getFilesDir(), "data.txt");
    }

    //This function will load items by reading every lone of the data file
    private void loadItems(){
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e){
            Log.e("MainActivity", "Error reading items", e);
            items = new ArrayList<>();
        }
    }
    //This function saves items by writing them into the data file
    private  void saveItems(){
        try{
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e){
            Log.e("MainActivity", "Error writing items", e);
        }
    }


}

