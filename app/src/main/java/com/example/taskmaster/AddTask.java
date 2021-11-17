package com.example.taskmaster;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

import org.apache.commons.io.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AddTask extends AppCompatActivity {
    private static final String TAG = AddTask.class.getName();
    String imageName = "";
    public Uri uri;

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
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

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

                Intent intent = new Intent(AddTask.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Button uploadImg = findViewById(R.id.upload);
        uploadImg.setOnClickListener(view -> {
            fileChoose();
            uploadInputStream();

        });


        TextView textView = findViewById(R.id.textView4);
        Button button = findViewById(R.id.Button3);
        button.setOnClickListener(new View.OnClickListener() {
            int counter = 0;

            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                textView.setText("Total Tasks :" + counter++);
                Toast.makeText(getApplicationContext(), "you added task successfully 📝🖊", Toast.LENGTH_SHORT).show();

                EditText taskTitle = findViewById(R.id.titletask);
                EditText taskBody = findViewById(R.id.bodytask);
                EditText taskState = findViewById(R.id.statetask);

                String setTitle = taskTitle.getText().toString();
                String setBody = taskBody.getText().toString();
                String setState = taskState.getText().toString();
                String imageURl = sharedPreferences.getString("FileUrlForReal", "no files");

//                Task details = new Task(setTitle , setBody , setState);
//                taskDao.insert(details);


                Task todo = Task.builder()
                        .title(setTitle)
                        .body(setBody)
                        .state(setState)
                        .image(imageURl)
                        .build();

                Amplify.API.mutate(
                        ModelMutation.create(todo),
                        response -> Log.i("MyAmplifyApp", "Added Todo with id: " + response.getData().getId()),
                        error -> Log.e("MyAmplifyApp", "Create failed", error)
                );

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String fileName = sdf.format(new Date());
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        File uploadFile = new File(getApplicationContext().getFilesDir(), fileName);
        try {
//            Amplify.addPlugin(new AWSApiPlugin());
//
//            // ----------------lab36---------------------------------
//            // Add this line, to include the Auth plugin.
//            Amplify.addPlugin(new AWSCognitoAuthPlugin());
//            // ----------------lab36---------------------------------
//            // ----------------lab37---------------------------------
//            Amplify.addPlugin(new AWSS3StoragePlugin());
//            // ----------------lab37---------------------------------
//            Amplify.configure(getApplicationContext());
            InputStream exampleInputStream = getContentResolver().openInputStream(data.getData());
            OutputStream outputStream = new FileOutputStream(uploadFile);
            imageName = data.getData().toString();
            byte[] buff = new byte[1024];
            int length;
            while ((length = exampleInputStream.read(buff)) > 0) {
                outputStream.write(buff, 0, length);
            }
            exampleInputStream.close();
            outputStream.close();
            Amplify.Storage.uploadFile(
                    fileName + ".jpg",
                    uploadFile,
                    result -> {
                        Log.i("MyAmplifyAppUpload", "Successfully uploaded: " + result.getKey());
                        Amplify.Storage.getUrl(result.getKey(), urlResult -> {
                            sharedPreferences.edit().putString("FileUrlForReal", urlResult.getUrl().toString()).apply();
                        }, urlError -> {
                            Log.e(TAG, "onActivityResult: Error please dont be mad");
                        });
                    },
                    storageFailure -> Log.e("MyAmplifyApp", "Upload failed", storageFailure)
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void uploadInputStream() {
        if (uri != null) {
            try {
                InputStream exampleInputStream = getContentResolver().openInputStream(uri);

                Amplify.Storage.uploadInputStream(
                        imageName,
                        exampleInputStream,
                        result -> Log.i("MyAmplifyApp", "Successfully uploaded: " + result.getKey()),
                        storageFailure -> Log.e("MyAmplifyApp", "Upload failed", storageFailure)
                );
            } catch (FileNotFoundException error) {
                Log.e("MyAmplifyApp", "Could not find file to open for input stream.", error);
            }
        }
    }


    public void fileChoose() {
        Intent fileChoose = new Intent(Intent.ACTION_GET_CONTENT);
        fileChoose.setType("*/*");
        fileChoose = Intent.createChooser(fileChoose, "choose file");
        startActivityForResult(fileChoose, 1234);
    }

}
