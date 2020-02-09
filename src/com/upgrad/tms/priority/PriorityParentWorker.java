package com.upgrad.tms.priority;

import com.upgrad.tms.entities.Task;
import com.upgrad.tms.util.TaskStatus;

import java.util.List;

/**
 * This class would be responsible for controlling the priority level globally
 */
public class PriorityParentWorker implements Runnable {

    private List<Task> taskList;
    private ShareObject shareObject;

    public PriorityParentWorker(List<Task> taskList, ShareObject shareObject) {
        this.taskList = taskList;
        this.shareObject = shareObject;
    }


    @Override
    public void run() {
        while (taskList.stream().anyMatch(task -> !task.getTaskStatus().equals(TaskStatus.DONE))) {
            System.out.println("starting while top: "+taskList.stream().anyMatch(task -> task.getPriority() < ShareObject.priorityCounter));
            synchronized (shareObject) {
                if (!taskList.stream().filter(task -> task.getTaskStatus() != TaskStatus.DONE).anyMatch(task -> task.getPriority() < ShareObject.priorityCounter)) {
                    System.out.println("Increasing the priority counter");
                    ShareObject.priorityCounter++;
                    shareObject.notifyAll();
                } else if (taskList.stream().anyMatch(task -> !task.getTaskStatus().equals(TaskStatus.DONE))){
                    try {
                        System.out.println("Parent is waiting on priority " + ShareObject.priorityCounter);
                        shareObject.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        System.out.println("All the tasks are marked done");
    }
}
