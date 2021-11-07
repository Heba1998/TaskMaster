package com.example.taskmaster;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Task {
    @PrimaryKey(autoGenerate = true)
    public Long id;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "body")
    public String body;

    @ColumnInfo(name = "state")
    public String state;

    public Task(String title, String body, String state) {
        this.title = title;
        this.body = body;
        this.state = state;
    }


    public String getBody() {
        return body;
    }
    public String getTitle() {
        return title;
    }
    public String getState() {
        return state;
    }
    public void setBody(String body) {
        this.body = body;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setState(String state) {
        this.state = state;
    }

}
