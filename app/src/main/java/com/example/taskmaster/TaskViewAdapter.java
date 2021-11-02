package com.example.taskmaster;


import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TaskViewAdapter extends RecyclerView.Adapter<TaskViewAdapter.TaskViewHolder> {
    List<Task> AllTasks = new ArrayList<>();

    public TaskViewAdapter(ArrayList<Task> AllTasks){
        this.AllTasks= AllTasks;
    }


    public static class TaskViewHolder extends RecyclerView.ViewHolder{
        public Task task;

        View itemView;


        public TaskViewHolder (@NonNull View itemView){
            super(itemView);
            this.itemView = itemView;



        }

    }


    @NonNull
    @Override
    public TaskViewAdapter.TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_part, parent , false);
        return  new TaskViewAdapter.TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewAdapter.TaskViewHolder holder, int position) {
        holder.task = AllTasks.get(position);
        TextView title = holder.itemView.findViewById(R.id.titleFragment);
        TextView body = holder.itemView.findViewById(R.id.bodyFragment);
        TextView state = holder.itemView.findViewById(R.id.stateFragment);

        title.setText(holder.task.title);
        body.setText(holder.task.body);
        state.setText(holder.task.state);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToDetails = new Intent(view.getContext(), TaskDetail.class);
                goToDetails.putExtra("Task Title",AllTasks.get(position).getTitle());
                view.getContext().startActivity(goToDetails);
            }
        });
    }


    @Override
    public int getItemCount() {
        return AllTasks.size();
    }
}