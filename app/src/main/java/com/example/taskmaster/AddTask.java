package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.storage.s3.AWSS3StoragePlugin;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;


public class AddTask extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
//----------------------------------------
        TaskDatabase db = Room.databaseBuilder(getApplicationContext(),
                TaskDatabase.class, "database-name").allowMainThreadQueries().build();
        TaskDAO taskDao = db.taskDao();
//----------------------------------------------------


        try {

            Amplify.addPlugin(new AWSApiPlugin());

            // ----------------lab36---------------------------------
            // Add this line, to include the Auth plugin.
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            // ----------------lab36---------------------------------

            // ----------------lab37---------------------------------
            Amplify.addPlugin(new AWSS3StoragePlugin());
            // ----------------lab37---------------------------------


            Amplify.configure(getApplicationContext());

            Log.i("MyAmplifyApp", "Initialized Amplify");
        } catch (AmplifyException error) {
            Log.e("MyAmplifyApp", "Could not initialize Amplify", error);
        }



        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent   = new Intent(AddTask.this, MainActivity.class);
                startActivity(intent);
            }
        });



        TextView textView = findViewById(R.id.textView4);
        Button button = findViewById(R.id.Button3);
        button.setOnClickListener(new View.OnClickListener() {
            int counter = 0 ;
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                textView.setText("Total Tasks :"+ counter++);
                Toast.makeText(getApplicationContext(),  "you added task successfully 📝🖊", Toast.LENGTH_SHORT).show();

                EditText taskTitle = findViewById(R.id.titletask);
                EditText taskBody = findViewById(R.id.bodytask);
                EditText taskState = findViewById(R.id.statetask);

                String setTitle = taskTitle.getText().toString();
                String setBody = taskBody.getText().toString();
                String setState = taskState.getText().toString();

//                Task details = new Task(setTitle , setBody , setState);
//                taskDao.insert(details);


                Task todo = Task.builder()
                        .title(setTitle)
                        .body(setBody)
                        .state(setState)
                        .build();

                Amplify.API.mutate(
                        ModelMutation.create(todo),
                        response -> Log.i("MyAmplifyApp", "Added Todo with id: " + response.getData().getId()),
                        error -> Log.e("MyAmplifyApp", "Create failed", error)
                );

            }
        });

        Button upload = findViewById(R.id.upload);
       upload.setOnClickListener(view -> {

           uploadFile();
           Toast.makeText(getApplicationContext(),  "I hate you", Toast.LENGTH_SHORT).show();

       });

    }

    private void uploadFile() {
        File exampleFile = new File(getApplicationContext().getFilesDir(), "ExampleKey");

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(exampleFile));
            writer.append("I HATE ERRORRRORORORORORROROROS");
            writer.close();
        } catch (Exception exception) {
            Log.e("MyAmplifyApp", "Upload failed", exception);
        }

        Amplify.Storage.uploadFile(
                "ExampleKey",
                exampleFile,
                result -> Log.i("MyAmplifyApp", "Successfully uploaded: " + result.getKey()),
                storageFailure -> Log.e("MyAmplifyApp", "Upload failed", storageFailure)
        );
    }
}