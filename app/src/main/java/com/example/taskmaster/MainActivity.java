package com.example.taskmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;
import com.amazonaws.mobileconnectors.pinpoint.targeting.TargetingClient;
import com.amazonaws.mobileconnectors.pinpoint.targeting.endpointProfile.EndpointProfile;
import com.amazonaws.mobileconnectors.pinpoint.targeting.endpointProfile.EndpointProfileUser;
import com.amplifyframework.AmplifyException;
import com.amplifyframework.analytics.AnalyticsEvent;
import com.amplifyframework.analytics.AnalyticsProperties;
import com.amplifyframework.analytics.UserProfile;
import com.amplifyframework.analytics.pinpoint.AWSPinpointAnalyticsPlugin;
import com.amplifyframework.analytics.pinpoint.models.AWSPinpointUserProfile;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.storage.s3.AWSS3StoragePlugin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<com.amplifyframework.datastore.generated.model.Task> taskList = new ArrayList<>();
    private static final String TAG = "MainActivity";
    private static PinpointManager pinpointManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getPinpointManager(getApplicationContext());
        assignUserIdToEndpoint();
        // ----------------lab28-----------------------------
//        ArrayList<Task> Tasks = new ArrayList<Task>();
//        Tasks.add(new Task("Title","              Body", "                State"));
//        Tasks.add(new Task("task1","Task 1 About Linked list", "complete"));
//        Tasks.add(new Task("task2","Task 2 about binary tree", "in progress"));
//        Tasks.add(new Task("task3","Task 3 about Stack & Queue","assigned" ));
//
//
//        RecyclerView TasksRecuclerView = findViewById(R.id.recycleViewId);
//        TasksRecuclerView.setLayoutManager(new LinearLayoutManager(this));
//        TasksRecuclerView.setAdapter(new TaskViewAdapter(Tasks));

        // ------------------------------------------------------

        Button allTask = findViewById(R.id.all);
        allTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                Intent goToAllTasks = new Intent(MainActivity.this, AllTasks.class);
                startActivity(goToAllTasks);
            }
        });


        Button addtask = findViewById(R.id.add);
        addtask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                Intent goToAddTask = new Intent(MainActivity.this,  AddTask.class);
                startActivity(goToAddTask);
            }
        });


        Button Settings = findViewById(R.id.Settings);
        Settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                Intent goToSettings = new Intent(MainActivity.this,  Setting.class);
                startActivity(goToSettings);
            }
        });


//        Button Details = findViewById(R.id.task1);
//        Details.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View V) {
//                Intent goToDetails = new Intent(MainActivity.this,  TaskDetail.class);
//                startActivity(goToDetails);
//            }
//        });
//
//        Button task1 = findViewById(R.id.task1);
//        task1.setOnClickListener((view -> {
//            String taskTitle = task1.getText().toString();
//            Intent goToTask1 = new Intent(MainActivity.this , TaskDetail.class);
//            goToTask1.putExtra("Task Title", taskTitle);
//            startActivity(goToTask1);
//        }));
//
//        Button task2 = findViewById(R.id.task2);
//        task2.setOnClickListener((view -> {
//            String taskTitle = task2.getText().toString();
//            Intent goToTask2 = new Intent(MainActivity.this , TaskDetail.class);
//            goToTask2.putExtra("Task Title", taskTitle);
//            startActivity(goToTask2);
//        }));
//
//        Button task3 = findViewById(R.id.task3);
//        task3.setOnClickListener((view -> {
//            String taskTitle = task3.getText().toString();
//            Intent goToTask3 = new Intent(MainActivity.this , TaskDetail.class);
//            goToTask3.putExtra("Task Title", taskTitle);
//            startActivity(goToTask3);
//        }));

        try {

            Amplify.addPlugin(new AWSApiPlugin());

            // ----------------lab36---------------------------------
            // Add this line, to include the Auth plugin.
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            // ----------------lab36---------------------------------

            // ----------------lab37---------------------------------
            Amplify.addPlugin(new AWSS3StoragePlugin());
            // ----------------lab37---------------------------------


            // ----------------lab39--------------
            Amplify.addPlugin(new AWSPinpointAnalyticsPlugin(getApplication()));
            // ----------------lab39--------------
            Amplify.configure(getApplicationContext());

            Log.i("MyAmplifyApp", "Initialized Amplify");
        } catch (AmplifyException error) {
            Log.e("MyAmplifyApp", "Could not initialize Amplify", error);
        }
        RecyclerView AllTasks = findViewById(R.id.recycleViewId);
        AllTasks.setLayoutManager(new LinearLayoutManager(this));

        AllTasks.setAdapter(new TaskViewAdapter(taskList, MainActivity.this));

        Handler handler = new Handler(Looper.myLooper(), new Handler.Callback() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                AllTasks.getAdapter().notifyDataSetChanged();
                return false;
            }
        });
        Amplify.API.query(
                ModelQuery.list(com.amplifyframework.datastore.generated.model.Task.class),
                response -> {
                    for (com.amplifyframework.datastore.generated.model.Task todo : response.getData()) {
//                        com.amplifyframework.datastore.generated.model.Task taskOrg = new Task(todo.getTitle(),todo.getBody(),todo.getState());
                        Log.i("graph testing", todo.getTitle()+ "todo" + todo);
                        taskList.add(todo);
                    }
                    handler.sendEmptyMessage(1);
                }, error -> Log.e("MyAmplifyApp", "Query failure", error)
        );


        Button logOut = findViewById(R.id.logout);
        logOut.setOnClickListener(view -> {
            Amplify.Auth.signOut(
                    () -> Log.i("AuthQuickstart", "Signed out successfully"),
                    error -> Log.e("not complemte", error.toString())
            );

            Intent intent = new Intent(MainActivity.this, SignIn.class);
            startActivity(intent);
        });


        AnalyticsEvent event = AnalyticsEvent.builder()
                .name("PasswordReset")
                .addProperty("Channel", "SMS")
                .addProperty("Successful", true)
                .addProperty("ProcessDuration", 792)
                .addProperty("UserAge", 120.3)
                .build();

        Amplify.Analytics.recordEvent(event);

        UserProfile.Location location = UserProfile.Location.builder()
                .latitude(31.992079)
                .longitude(35.845488)
                .postalCode("542546")
                .city("zarqa")
                .region("36 street")
                .country("Jordan")
                .build();

        AnalyticsProperties customProperties = AnalyticsProperties.builder()
                .add("property1", "Property value")
                .build();

        AnalyticsProperties userAttributes = AnalyticsProperties.builder()
                .add("someUserAttribute", "User attribute value")
                .build();

        AWSPinpointUserProfile profile = AWSPinpointUserProfile.builder()
                .name("Heba AL-Momani")
                .email("hebaalmomani1998@gmail.com")
                .plan("I'm so tired")
                .location(location)
                .customProperties(customProperties)
                .userAttributes(userAttributes)
                .build();

        String userId = Amplify.Auth.getCurrentUser().getUserId();

        Amplify.Analytics.identifyUser(userId, profile);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String username = sharedPreferences.getString("username", "Your task");
        TextView userTasks = findViewById(R.id.UsernameTasks);
        userTasks.setText(username+"’s tasks");

        String username1 = sharedPreferences.getString("username22", "Your email");
        TextView useremail = findViewById(R.id.username22);
        useremail.setText("User Name: "+username1);

        // database render
//        TaskDatabase db = Room.databaseBuilder(getApplicationContext(),
//                TaskDatabase.class, "database-name").allowMainThreadQueries().build();
//        TaskDAO taskDao = db.taskDao();
//        List<Task> taskList = taskDao.getAll();
//        RecyclerView taskRec = findViewById(R.id.recycleViewId);
//        taskRec.setLayoutManager(new LinearLayoutManager(this));
//        taskRec.setAdapter(new TaskViewAdapter(taskList));
        }


    public static PinpointManager getPinpointManager(final Context applicationContext) {
        if (pinpointManager == null) {
            final AWSConfiguration awsConfig = new AWSConfiguration(applicationContext);
            AWSMobileClient.getInstance().initialize(applicationContext, awsConfig, new Callback<UserStateDetails>() {
                @Override
                public void onResult(UserStateDetails userStateDetails) {
                    Log.i(TAG, "INIT => " + userStateDetails.getUserState().toString());
                }

                @Override
                public void onError(Exception e) {
                    Log.e("INIT", "Initialization error.", e);
                }
            });

            PinpointConfiguration pinpointConfig = new PinpointConfiguration(
                    applicationContext,
                    AWSMobileClient.getInstance(),
                    awsConfig);

            pinpointManager = new PinpointManager(pinpointConfig);

            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull com.google.android.gms.tasks.Task<String> task) {
                            if (!task.isSuccessful()) {
                                Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                                return;
                            }
                            final String token = task.getResult();
                            Log.d(TAG, "Registering push notifications token: " + token);
                            pinpointManager.getNotificationClient().registerDeviceToken(token);
                        }
                    });
        }
        return pinpointManager;
    }
    public void assignUserIdToEndpoint() {
        TargetingClient targetingClient = pinpointManager.getTargetingClient();
        EndpointProfile endpointProfile = targetingClient.currentEndpoint();
        EndpointProfileUser endpointProfileUser = new EndpointProfileUser();
        endpointProfileUser.setUserId("UserIdValue");
        endpointProfile.setUser(endpointProfileUser);
        targetingClient.updateEndpointProfile(endpointProfile);
        Log.d(TAG, "Assigned user ID " + endpointProfileUser.getUserId() +
                " to endpoint " + endpointProfile.getEndpointId());
    }
}