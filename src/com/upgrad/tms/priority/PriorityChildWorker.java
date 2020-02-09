package com.upgrad.tms.priority;

import com.upgrad.tms.entities.Task;
import com.upgrad.tms.menu.AbstractWorker;
import com.upgrad.tms.repository.AssigneeRepository;
import com.upgrad.tms.util.TaskStatus;

public class PriorityChildWorker extends AbstractWorker implements Runnable {

    private final Task task;
    private final ShareObject shareObject;

    public PriorityChildWorker(AssigneeRepository assigneeRepository, Task task, ShareObject shareObject) {
        super(assigneeRepository);
        this.task = task;
        this.shareObject = shareObject;
    }

    @Override
    public void doWork() {
        while (task.getTaskStatus() != TaskStatus.DONE) {
            synchronized (shareObject) {
                if (ShareObject.priorityCounter < task.getPriority()) {
                    try {
                        System.out.println("Task id: "+task.getId()+" Waiting for priority: "+task.getPriority());
                        shareObject.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    processTask(task);
                    updateInFile();
                    shareObject.notifyAll();
                }
            }
        }
    }

    @Override
    public void run() {
        doWork();
    }
}
