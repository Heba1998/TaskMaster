package com.example.taskmaster;

public class Task {
    public String title;
    public String body;
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
