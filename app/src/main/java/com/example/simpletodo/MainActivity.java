package com.example.simpletodo;

import org.apache.commons.io.FileUtils;
//import android.os.FileUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<String> items;
    Button addBtn;
    EditText editText;
    RecyclerView rv;
    ItemsAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addBtn = findViewById(R.id.btnAdd);
        editText = findViewById(R.id.editText);
        rv = findViewById(R.id.rv);

        loadData();

        //Called when long pressing an item
        ItemsAdapter.OnLongClickListener onLongClickListener = new ItemsAdapter.OnLongClickListener() {
            @Override
            //Receive position from the interface
            public void onItemLongClicked(int position) {
                items.remove(position);
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item removed", Toast.LENGTH_SHORT).show();
                saveData();

            }
        };

        itemsAdapter = new ItemsAdapter(items, onLongClickListener);
        rv.setAdapter(itemsAdapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newItem = editText.getText().toString();
                items.add(newItem);
                //Notify adapter of the new item
                itemsAdapter.notifyItemInserted(items.size()-1);
                //Empty the text field
                editText.setText("");
                //Small pop up
                Toast.makeText(getApplicationContext(), "Item added", Toast.LENGTH_SHORT).show();
                saveData();

            }
        });

    }

    //Returns the file to work with
    private File getDataFile(){
        return new File(getFilesDir(), "data.txt");
    }

    //Load data from the file
    private void loadData(){
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        }catch (IOException e){
            Log.e("Main Activity", "Error loading items", e);
            items= new ArrayList<>();
        }
    }

    //Save data to the file
    private void saveData(){
        try {
            FileUtils.writeLines(getDataFile(),items);
        } catch (IOException e) {
            Log.e("Main Activity", "Error saving items", e);
        }

    }
}