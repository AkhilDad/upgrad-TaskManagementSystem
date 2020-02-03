package com.upgrad.tms.menu;

import com.upgrad.tms.entities.Task;
import com.upgrad.tms.repository.AssigneeRepository;
import com.upgrad.tms.util.TaskStatus;

import java.io.IOException;
import java.util.List;

public class CompositeTaskWorker extends Thread {

    private final List<Task> taskList;
    private final AssigneeRepository assigneeRepository;

    public CompositeTaskWorker(List<Task> taskList, AssigneeRepository assigneeRepository) {
        this.taskList = taskList;
        this.assigneeRepository = assigneeRepository;
    }

    public void doWork() {
        for (Task task : taskList) {
            processTask(task);
        }
        updateInFile();
    }

    private void processTask(Task task) {
        //already return the task if already done
        if (task.getTaskStatus() == TaskStatus.DONE) {
            return;
        }
        long i = 0;
        System.out.println("Time: " + System.currentTimeMillis() + " Starting task with id and title" + task.getId() + ": " + task.getTitle());
        task.setStatus(TaskStatus.PENDING);
        System.out.println(Long.MAX_VALUE / Integer.MAX_VALUE);
        while (i < 1000000) {
            if (i % 1000 == 0) {
                System.out.println("Task: " + task.getId() + " Value: " + i);
            }
            i++;
        }
        System.out.println("Time: " + System.currentTimeMillis() + " Completing task with id and title" + task.getId() + ": " + task.getTitle());
        task.setStatus(TaskStatus.DONE);
    }

    private void updateInFile() {
        try {
            System.out.println("updateInFile(): updating list to the file ");
            assigneeRepository.updateListToFile();
            System.out.println("updating list to the file is done");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("exiting updateInFile()");
        }
    }

    @Override
    public void run() {
        super.run();
        doWork();
    }
}
