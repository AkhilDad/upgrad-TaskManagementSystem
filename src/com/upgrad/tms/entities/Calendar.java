package com.upgrad.tms.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Calendar<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<T>  taskList = new ArrayList<>();

    public List<T> getTaskList() {
        return taskList;
    }

    public void add(T task) {
        taskList.add(task);
    }
}

