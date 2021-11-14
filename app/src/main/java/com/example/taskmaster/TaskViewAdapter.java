package com.example.taskmaster;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.amplifyframework.datastore.generated.model.Task;
import java.util.ArrayList;
import java.util.List;

public class TaskViewAdapter extends RecyclerView.Adapter<TaskViewAdapter.TaskViewHolder> {
    List<Task> AllTasks = new ArrayList<>();

    public TaskViewAdapter(List<Task> AllTasks, MainActivity mainActivity){
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
    public void onBindViewHolder(@NonNull TaskViewAdapter.TaskViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.task = AllTasks.get(position);
        TextView title = holder.itemView.findViewById(R.id.titleFragment);
        TextView body = holder.itemView.findViewById(R.id.bodyFragment);
        TextView state = holder.itemView.findViewById(R.id.stateFragment);

        title.setText(holder.task.getTitle());
        body.setText(holder.task.getBody());
        state.setText(holder.task.getState());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToDetails = new Intent(view.getContext(), TaskDetail.class);
                goToDetails.putExtra("Task Title",AllTasks.get(position).getTitle());
                goToDetails.putExtra("body",AllTasks.get(position).getBody());
                goToDetails.putExtra("state",AllTasks.get(position).getState());
                view.getContext().startActivity(goToDetails);
            }
        });
    }


    @Override
    public int getItemCount() {
        return AllTasks.size();
    }
}