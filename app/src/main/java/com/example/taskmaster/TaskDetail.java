package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

public class TaskDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent   = new Intent(TaskDetail.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();

        String taskTitle = intent.getExtras().getString("Task Title");
        TextView Title = findViewById(R.id.title1);
        Title.setText(taskTitle);

        String taskBody = intent.getStringExtra("body");
        TextView body = findViewById(R.id.body);
        body.setText(taskBody);

        String taskState = intent.getStringExtra("state");
        TextView state = findViewById(R.id.state);
        state.setText(taskState);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}