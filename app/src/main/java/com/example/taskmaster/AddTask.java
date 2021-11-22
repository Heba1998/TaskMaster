package com.example.taskmaster;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.room.Room;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.preference.PreferenceManager;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AddTask extends AppCompatActivity {
    private static final String TAG = AddTask.class.getName();
    private static final int PERMISSION_ID = 25;
    String imageName = "";
    public Uri uri;

    private FusedLocationProviderClient mFusedLocationClient;
    private double lat;
    private double lon;
    private final LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            Log.i(TAG, "The location is => " + mLastLocation);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
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

        ImageFromPhone();
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
                        .latitude(lat)
                        .longitude(lon)
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


    ///////////// lab42 ////////////////////
    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (checkPermissions()) {

            if (isLocationEnabled()) {

                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            lat = location.getLatitude();
                            lon = location.getLongitude();
                        }
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Setting.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }
    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5);
        locationRequest.setFastestInterval(0);
        locationRequest.setNumUpdates(10);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this); // this may or may not be needed
        mFusedLocationClient.requestLocationUpdates(locationRequest, mLocationCallback, Looper.myLooper());
    }
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;


    }
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    //////////////////////////////////////////////////////////////////////////////////////////

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



    public void ImageFromPhone(){
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        ImageView image = findViewById(R.id.imagefromphone);
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
                if (imageUri != null) {
                    image.setImageURI(imageUri);
                    image.setVisibility(View.VISIBLE);

                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLastLocation();
    }
}
