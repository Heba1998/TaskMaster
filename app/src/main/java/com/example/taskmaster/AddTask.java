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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Task;
import com.amplifyframework.datastore.generated.model.Team;

import java.util.ArrayList;
import java.util.List;


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
                RadioButton Team1 = findViewById(R.id.team1Radio);
                RadioButton Team2 = findViewById(R.id.team2Radio);
                RadioButton Team3 = findViewById(R.id.team3Radio);


                String setTitle = taskTitle.getText().toString();
                String setBody = taskBody.getText().toString();
                String setState = taskState.getText().toString();
                String setTeam="";
                if(Team1.isChecked()) {
                   setTeam="Team1";
                }else if(Team2.isChecked()){
                   setTeam = "Team2";
                }else if(Team3.isChecked()){
                    setTeam = "Team3";
                }

//                Task details = new Task(setTitle , setBody , setState);
//                taskDao.insert(details);

                List<Team> AllTeams = new ArrayList<>();
                Amplify.API.query(
                        ModelQuery.list(Team.class),
                        response -> {
                            for (Team taskss : response.getData()) {
                                Log.i("MyAmplifyApp", taskss.getName());
                                AllTeams.add(taskss);

                            }
                        },
                        error -> Log.e("MyAmplifyApp", "Query failure", error)
                );
                Team team=null;
                for (int i = 0; i < AllTeams.size(); i++) {
                    if(AllTeams.get(i).getName().equals(setTeam)){
                        team = AllTeams.get(i);
                    }
                }

                Task todo = Task.builder()
                        .title(setTitle)
                        .body(setBody)
                        .state(setState)
                        .team(team)
                        .build();

                Amplify.API.mutate(
                        ModelMutation.create(todo),
                        response -> Log.i("MyAmplifyApp", "Added Todo with id: " + response.getData().getId()),
                        error -> Log.e("MyAmplifyApp", "Create failed", error)
                );

            }
        });

    }
}