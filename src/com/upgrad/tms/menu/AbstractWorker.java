package com.upgrad.tms.menu;

import com.upgrad.tms.entities.Task;
import com.upgrad.tms.repository.AssigneeRepository;
import com.upgrad.tms.util.TaskStatus;

import java.io.IOException;

public abstract class AbstractWorker {

    private AssigneeRepository assigneeRepository;

    public AbstractWorker(AssigneeRepository assigneeRepository) {
        this.assigneeRepository = assigneeRepository;
    }

    public abstract void doWork();

    protected void processTask(Task task) {
        if (task.getTaskStatus() == TaskStatus.DONE) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Task id: "+task.getId()+ " Current thread priority: "+Thread.currentThread().getPriority());
            return;
        }
        System.out.println("Starting Time: "+System.currentTimeMillis()+ " Task Id: "+task.getId()+ " Task Title: "+task.getTitle());
        task.setStatus(TaskStatus.PENDING);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Task id: "+task.getId()+ " Current thread priority: "+Thread.currentThread().getPriority());
        System.out.println("Completing Time: "+System.currentTimeMillis()+ " Task Id: "+task.getId()+ " Task Title: "+task.getTitle());
        task.setStatus(TaskStatus.DONE);
    }

    protected void updateInFile() {
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
}
