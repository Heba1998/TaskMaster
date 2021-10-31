package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toolbar;

public class AllTasks extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_tasks);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent   = new Intent(AllTasks.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Button home = findViewById(R.id.Home);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                Intent goToHome = new Intent(AllTasks.this, MainActivity.class);
                startActivity(goToHome);
            }
        });
    }

}