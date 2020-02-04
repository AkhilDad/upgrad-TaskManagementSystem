package com.upgrad.tms.menu;

import com.upgrad.tms.entities.Task;
import com.upgrad.tms.repository.AssigneeRepository;

public class TaskWorker extends AbstractWorker implements Runnable {

    private final Task task;

    public TaskWorker(Task task, AssigneeRepository assigneeRepository) {
        super(assigneeRepository);
        this.task = task;
    }

    public void doWork() {
        System.out.println("Task Id: "+task.getId()+" Current Thread priority: "+Thread.currentThread().getPriority());
        processTask(task);
        updateInFile();
    }

    @Override
    public void run() {
        doWork();
    }
}
