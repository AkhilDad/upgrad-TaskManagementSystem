package com.upgrad.tms.menu;

import com.upgrad.tms.entities.Task;
import com.upgrad.tms.util.TaskStatus;

import java.util.List;

public class ParentTaskWorker implements Runnable {

    private final List<Task> childTasks;
    private static Object myLock = new Object();
    private final Task parentTask;

    public ParentTaskWorker(List<Task> childTasks, Task task) {
        this.childTasks = childTasks;
        this.parentTask = task;
    }

    @Override
    public void run() {
        synchronized (myLock) {
            while (childTasks.stream().anyMatch(task -> task.getTaskStatus() != TaskStatus.DONE)) {
                try {
                    myLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            parentTask.setStatus(TaskStatus.DONE);
            System.out.println("Parent task is completed : " );
            myLock.notifyAll();
        }
    }
}
