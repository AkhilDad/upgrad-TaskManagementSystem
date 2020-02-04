package com.upgrad.tms.menu;

import com.upgrad.tms.entities.Task;
import com.upgrad.tms.repository.AssigneeRepository;
import com.upgrad.tms.util.TaskStatus;

import java.io.IOException;
import java.util.List;

public class CompositeWorker extends Thread {

    private final List<Task> taskList;
    private final AssigneeRepository assigneeRepository;

    public CompositeWorker(List<Task> taskList, AssigneeRepository assigneeRepository) {

        this.taskList = taskList;
        this.assigneeRepository = assigneeRepository;
    }

    private void doWork() {
        for (Task task : taskList) {
            processTask(task);
        }
        updateInFile();
    }

    private void processTask(Task task) {
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
