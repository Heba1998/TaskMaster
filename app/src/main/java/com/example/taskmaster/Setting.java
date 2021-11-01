package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toolbar;

public class Setting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Setting.this, MainActivity.class);
                startActivity(intent);
            }
        });


        Button home = findViewById(R.id.GoToHomePage);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                Intent goToHome = new Intent(Setting.this, MainActivity.class);
                startActivity(goToHome);
            }
        });


        SharedPreferences UserName = PreferenceManager.getDefaultSharedPreferences(Setting.this);
        SharedPreferences.Editor welcomeMsg = UserName.edit();
        Button saveButton = findViewById(R.id.save);
        saveButton.setOnClickListener((View -> {
            EditText usernameInput = findViewById(R.id.editPersonName);
            String username = usernameInput.getText().toString();
            welcomeMsg.putString("username",username);
            welcomeMsg.apply();
        }));
    }
}