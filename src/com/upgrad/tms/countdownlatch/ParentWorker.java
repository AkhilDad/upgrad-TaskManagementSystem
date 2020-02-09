package com.upgrad.tms.countdownlatch;

import com.upgrad.tms.entities.Task;
import com.upgrad.tms.menu.AbstractWorker;
import com.upgrad.tms.repository.AssigneeRepository;

import java.util.concurrent.CountDownLatch;

public class ParentWorker extends AbstractWorker implements Runnable {

    private final CountDownLatch countDownLatch;
    private Task task;

    public ParentWorker(AssigneeRepository assigneeRepository, CountDownLatch countDownLatch, Task task) {
        super(assigneeRepository);
        this.countDownLatch = countDownLatch;
        this.task = task;
    }

    @Override
    public void doWork() {
        System.out.println("Do work of parent");
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        processTask(task);
        updateInFile();
        System.out.println("Parent is also done");

    }

    @Override
    public void run() {
        doWork();
    }
}
