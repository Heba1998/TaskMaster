package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toolbar;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.analytics.AnalyticsEvent;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;

public class SignUpConfirm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_confirm);

                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Action();
                Intent intent   = new Intent(SignUpConfirm.this, SignUp.class);
                startActivity(intent);
            }
        });



        EditText email = findViewById(R.id.confirmEmail);
        EditText code = findViewById(R.id.confirmCode);
        try {
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.configure(getApplicationContext());

            Log.i("MyAmplifyApp", "Initialized Amplify");
        } catch (AmplifyException error) {
            Log.e("MyAmplifyApp", "Could not initialize Amplify", error);
        }

        Button confirm = findViewById(R.id.Confirm);
        confirm.setOnClickListener(view -> {
            Action();
            Amplify.Auth.confirmSignUp(
                    email.getText().toString(),
                    code.getText().toString(),
                    result -> Log.i("AuthQuickstart", result.isSignUpComplete() ? "Confirm signUp succeeded" : "Confirm sign up not complete"),
                    error -> Log.e("AuthQuickstart", error.toString())
            );

//            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//            @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putString("email",email.getText().toString());
//            editor.apply();

            EditText username = findViewById(R.id.username22);
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("username22",username.getText().toString());
            editor.apply();


            Intent intent = new Intent(SignUpConfirm.this, MainActivity.class);
            startActivity(intent);
        });



    }


    public void Action(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SignUpConfirm.this);
        String userName = sharedPreferences.getString("username","user");
        AnalyticsEvent event = AnalyticsEvent.builder()
                .name("Add Task Button Pressed")
                .addProperty("UserName", userName)
                .build();
        Amplify.Analytics.recordEvent(event);
    }
}