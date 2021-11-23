package com.example.taskmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.amplifyframework.analytics.AnalyticsEvent;
import com.amplifyframework.core.Amplify;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.io.File;

public class TaskDetail extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap googleMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Action();
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


        String url = intent.getExtras().getString("image");
        Log.i("url", "onCreate: "+ url);
        ImageView image = findViewById(R.id.imageview);
        Log.i("url", "onCreate: "+ image.getId());
        Picasso.get().load(url).into(image);

//        Amplify.Storage.downloadFile(
//                intent.getExtras().getString("imgName"),
//                new File(getApplicationContext().getFilesDir() + "/download.jpg"),
//                result -> {
//                    ImageView imageView = findViewById(R.id.imageView);
//                    String newImg = result.getFile().getPath();
//                    imageView.setImageBitmap(BitmapFactory.decodeFile(newImg));
//
//                    Log.i("MyAmplifyApp", "Successfully downloaded: " + result.getFile());},
//                error -> Log.e("MyAmplifyApp",  "Download Failure", error)
//        );

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Intent intent = getIntent();
        LatLng myLocation = new LatLng(getIntent().getDoubleExtra("latitude", intent.getFloatExtra("latitude",0)),
                getIntent().getDoubleExtra("longitude", intent.getFloatExtra("longitude",0)));
        googleMap.addMarker(new MarkerOptions().position(myLocation).title("My Location In Jordan"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
    }

    public void Action(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String userName = sharedPreferences.getString("username","user");
        AnalyticsEvent event = AnalyticsEvent.builder()
                .name("Add Task Button Pressed")
                .addProperty("UserName", userName)
                .build();
        Amplify.Analytics.recordEvent(event);
    }
}