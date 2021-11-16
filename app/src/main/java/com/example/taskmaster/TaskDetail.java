package com.example.taskmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.amplifyframework.core.Amplify;

import java.io.File;

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

//        ImageView imageView= findViewById(R.id.imageView);
//        if (intent.getExtras().getString("image")!=null){
//            Amplify.Storage.downloadFile(
//                    intent.getExtras().getString("image"),
//                    new File(getApplicationContext().getFilesDir()+ "/" + intent.getExtras().getString("image")+".jpg"),
//                    response->{
//
//                        Bitmap bitmap = BitmapFactory.decodeFile(response.getFile().getPath());
//                        imageView.setImageBitmap(bitmap);
//                        Log.i("TaskDetailsPageImage", "Successfully downloaded: " + response.getFile().getName());
//                    },
//                    error->{
//                        Log.i("testing", "onCreate: "+  intent.getExtras().getString("image"));
//                        Log.i("TaskDetailsPageImage", "Failed to download: " + error);
//                    }
//            );
//
//            Amplify.Storage.downloadFile(
//                    intent.getExtras().getString("image"),
//                    new File(getApplicationContext().getFilesDir() +"/image.jpg"),
//                    result -> {
//                        ImageView imageView = (ImageView) findViewById(R.id.imageView);
////                        ImageView imageView = findViewById(R.id.imageView);
//                        File newImg = result.getFile();
//                        imageView.setImageBitmap(BitmapFactory.decodeFile(String.valueOf(newImg)));
//
//                        Log.i("MyAmplifyApp", "Successfully downloaded: " + result.getFile());},
//                    error -> Log.e("MyAmplifyApp",  "Download Failure", error)
//            );
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}