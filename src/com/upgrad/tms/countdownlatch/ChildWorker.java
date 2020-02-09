package com.upgrad.tms.countdownlatch;

import com.upgrad.tms.entities.Task;
import com.upgrad.tms.menu.AbstractWorker;
import com.upgrad.tms.repository.AssigneeRepository;

import java.util.concurrent.CountDownLatch;

public class ChildWorker extends AbstractWorker implements Runnable {

    private final CountDownLatch countDownLatch;
    private final Task task;

    public ChildWorker(AssigneeRepository assigneeRepository, Task task, CountDownLatch countDownLatch) {
        super(assigneeRepository);
        this.countDownLatch = countDownLatch;
        this.task = task;
    }

    @Override
    public void doWork() {
        processTask(task);
        updateInFile();
        countDownLatch.countDown();
    }

    @Override
    public void run() {
        doWork();
    }
}
