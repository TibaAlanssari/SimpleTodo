package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomePageActivity extends AppCompatActivity {

    private Button goToTodolist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        goToTodolist = (Button) findViewById(R.id.goToTodolist);
        goToTodolist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToTodolist();

            }
        });
    }

    private void goToTodolist() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}