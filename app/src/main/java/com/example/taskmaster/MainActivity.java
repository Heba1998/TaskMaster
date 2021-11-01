package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button allTask = findViewById(R.id.all);
        allTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                Intent goToAllTasks = new Intent(MainActivity.this, AllTasks.class);
                startActivity(goToAllTasks);
            }
        });


        Button addtask = findViewById(R.id.add);
        addtask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                Intent goToAddTask = new Intent(MainActivity.this,  AddTask.class);
                startActivity(goToAddTask);
            }
        });


        Button Settings = findViewById(R.id.Settings);
        Settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                Intent goToSettings = new Intent(MainActivity.this,  Setting.class);
                startActivity(goToSettings);
            }
        });


        Button Details = findViewById(R.id.task1);
        Details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                Intent goToDetails = new Intent(MainActivity.this,  TaskDetail.class);
                startActivity(goToDetails);
            }
        });

        Button task1 = findViewById(R.id.task1);
        task1.setOnClickListener((view -> {
            String taskTitle = task1.getText().toString();
            Intent goToTask1 = new Intent(MainActivity.this , TaskDetail.class);
            goToTask1.putExtra("Task Title", taskTitle);
            startActivity(goToTask1);
        }));

        Button task2 = findViewById(R.id.task2);
        task2.setOnClickListener((view -> {
            String taskTitle = task2.getText().toString();
            Intent goToTask2 = new Intent(MainActivity.this , TaskDetail.class);
            goToTask2.putExtra("Task Title", taskTitle);
            startActivity(goToTask2);
        }));

        Button task3 = findViewById(R.id.task3);
        task3.setOnClickListener((view -> {
            String taskTitle = task3.getText().toString();
            Intent goToTask3 = new Intent(MainActivity.this , TaskDetail.class);
            goToTask3.putExtra("Task Title", taskTitle);
            startActivity(goToTask3);
        }));

    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String username = sharedPreferences.getString("username", "Your task");
        TextView userTasks = findViewById(R.id.UsernameTasks);
        userTasks.setText(username+"’s tasks");
    }
}