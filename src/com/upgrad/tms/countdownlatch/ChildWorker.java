package com.upgrad.tms.countdownlatch;

import com.upgrad.tms.entities.Task;
import com.upgrad.tms.menu.AbstractWorker;
import com.upgrad.tms.repository.AssigneeRepository;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class ChildWorker extends AbstractWorker implements Runnable {

    private final CountDownLatch countDownLatch;
    private final Task task;
    private final CyclicBarrier cyclicBarrier;

    public ChildWorker(AssigneeRepository assigneeRepository, Task task, CountDownLatch countDownLatch, CyclicBarrier cyclicBarrier) {
        super(assigneeRepository);
        this.countDownLatch = countDownLatch;
        this.task = task;
        this.cyclicBarrier = cyclicBarrier;
    }

    @Override
    public void doWork() {
        System.out.println("Task id: "+task.getId());
        System.out.println("Cyclic barrier total parties: "+cyclicBarrier.getParties()+ " Waiting parties: "+cyclicBarrier.getNumberWaiting());
        try {
            cyclicBarrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        processTask(task);
        updateInFile();
        countDownLatch.countDown();
    }

    @Override
    public void run() {
        doWork();
    }
}
