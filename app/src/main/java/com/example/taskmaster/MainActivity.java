package com.example.taskmaster;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.annotation.SuppressLint;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Task> taskList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                        Task taskOrg = new Task(todo.getTitle(),todo.getBody(),todo.getState());
                        Log.i("graph testing", todo.getTitle());
                        taskList.add(taskOrg);
                    }
                    handler.sendEmptyMessage(1);
                }, error -> Log.e("MyAmplifyApp", "Query failure", error)
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String username = sharedPreferences.getString("username", "Your task");
        TextView userTasks = findViewById(R.id.UsernameTasks);
        userTasks.setText(username+"’s tasks");

        // database render
//        TaskDatabase db = Room.databaseBuilder(getApplicationContext(),
//                TaskDatabase.class, "database-name").allowMainThreadQueries().build();
//        TaskDAO taskDao = db.taskDao();
//        List<Task> taskList = taskDao.getAll();
//        RecyclerView taskRec = findViewById(R.id.recycleViewId);
//        taskRec.setLayoutManager(new LinearLayoutManager(this));
//        taskRec.setAdapter(new TaskViewAdapter(taskList));
        }
}