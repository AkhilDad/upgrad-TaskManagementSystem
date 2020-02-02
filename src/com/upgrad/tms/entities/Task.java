package com.upgrad.tms.entities;

import com.upgrad.tms.util.DateUtils;
import com.upgrad.tms.util.TaskStatus;

import java.io.Serializable;
import java.util.Date;

public abstract class Task implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String title;
    private int priority;
    private Date dueDate;
    private TaskStatus taskStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public static String getTaskTypeString(Task task) {
        String taskType = task instanceof Todo ? "Todo" : "Meeting";
        return taskType;
    }

    public void printTaskOnConsole() {
        String type = Task.getTaskTypeString(this);
        System.out.println(type + " " + getId());
        System.out.println("Title: " + getTitle());
        System.out.println("Priority: " + getPriority());
        System.out.println("Due Date: " + DateUtils.getFormattedDate(getDueDate(), DateUtils.DateFormat.DAY_MONTH_YEAR_HOUR_MIN_SLASH_SEPARATED));
        System.out.println("Task Status: " + getTaskStatus());
        printSubTaskProperties();
    }

    public abstract void printSubTaskProperties();
}

