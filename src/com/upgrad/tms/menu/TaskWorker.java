package com.upgrad.tms.menu;

import com.upgrad.tms.entities.Task;
import com.upgrad.tms.repository.AssigneeRepository;
import com.upgrad.tms.util.TaskStatus;

import java.util.concurrent.Callable;

public class TaskWorker extends AbstractWorker implements Callable<Integer> {

    private final Task task;

    public TaskWorker(Task task, AssigneeRepository assigneeRepository) {
        super(assigneeRepository);
        this.task = task;
    }

    public void doWork() {
        System.out.println("Task id: "+task.getId()+ " Current thread priority: "+Thread.currentThread().getPriority());
        processTask(task);
        updateInFile();
    }

    @Override
    public Integer call() {
        int actualDone = task.getTaskStatus() == TaskStatus.DONE ? 0 : 1;
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY - task.getPriority());
        doWork();
        return actualDone;
    }
}
