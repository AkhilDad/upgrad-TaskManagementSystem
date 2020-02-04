package com.upgrad.tms.menu;

import com.upgrad.tms.entities.Task;
import com.upgrad.tms.repository.AssigneeRepository;
import com.upgrad.tms.util.TaskStatus;

import java.io.IOException;

public class TaskWorker extends Thread {

    private final Task task;
    private final AssigneeRepository assigneeRepository;

    public TaskWorker(Task task, AssigneeRepository assigneeRepository) {
        this.task = task;
        this.assigneeRepository = assigneeRepository;
    }

    private void doWork() {
        if (task.getTaskStatus() == TaskStatus.DONE) {
            return;
        }
        System.out.println("Starting Time: "+System.currentTimeMillis()+ " Task Id: "+task.getId()+ " Task Title: "+task.getTitle());
        task.setStatus(TaskStatus.PENDING);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Completing Time: "+System.currentTimeMillis()+ " Task Id: "+task.getId()+ " Task Title: "+task.getTitle());
        task.setStatus(TaskStatus.DONE);
        updateInFile();
    }

    private void updateInFile() {
        System.out.println("updating In File ");
        try {
            assigneeRepository.updateListToFile();
            System.out.println("updated list in File");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Exiting updatInFile");
        }
    }

    @Override
    public void run() {
        super.run();
        doWork();
    }
}
