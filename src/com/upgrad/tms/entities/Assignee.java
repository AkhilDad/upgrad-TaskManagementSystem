package com.upgrad.tms.entities;

import java.io.Serializable;

public class Assignee implements Serializable {
    private long id;
    private String name;
    private String username;
    private String password;
    private static final long serialVersionUID=1L;
    private Calendar<Task> taskCalendar;

    public Assignee(long id, String name, String username, String password) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        taskCalendar = new Calendar<>();
    }

    public Calendar<Task> getTaskCalendar() {
        return taskCalendar;
    }

    public void setTaskCalendar(Calendar<Task> taskCalendar) {
        this.taskCalendar = taskCalendar;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
