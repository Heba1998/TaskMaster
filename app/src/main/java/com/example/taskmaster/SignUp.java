package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toolbar;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;

public class SignUp extends AppCompatActivity {
    private static final String TAG = "Signup";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        createNotificationChannel();

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
//
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent   = new Intent(SignUp.this, MainActivity.class);
//                startActivity(intent);
//            }
//        });

        EditText email = findViewById(R.id.Email);
        Log.i(TAG, "onCreate" + email.getText().toString());
        EditText password = findViewById(R.id.Password);
        Log.i(TAG, "onCreate" + password.getText().toString() );

        try {
            Amplify.addPlugin(new AWSApiPlugin());
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.configure(getApplicationContext());

            Log.i("MyAmplifyApp", "Initialized Amplify");
        } catch (AmplifyException error) {
            Log.e("MyAmplifyApp", "Could not initialize Amplify", error);
        }


        Button signUp = findViewById(R.id.signupButton);
        signUp.setOnClickListener(view -> {
            Amplify.Auth.fetchAuthSession(
                    result -> Log.i("AmplifyQuickstart", result.toString()),
                    error -> Log.e("AmplifyQuickstart", error.toString())
            );
            AuthSignUpOptions options = AuthSignUpOptions.builder()
                    .userAttribute(AuthUserAttributeKey.email(), email.getText().toString())
                    .build();
            Log.i(TAG, "onCreate" + email.getText().toString());
            Amplify.Auth.signUp(email.getText().toString(), password.getText().toString(), options,
                    result -> Log.i("AuthQuickStart", "Result: " + result.toString()),
                    error -> Log.e("AuthQuickStart", "Sign up failed", error)
            );

            Intent intent = new Intent(SignUp.this, SignUpConfirm.class);
            startActivity(intent);

        });



        Button signIn = findViewById(R.id.signinbutton1);
        signIn.setOnClickListener(view -> {
            Intent intent = new Intent(SignUp.this,SignIn.class);
            startActivity(intent);
        });


    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(PushListenerService.CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}