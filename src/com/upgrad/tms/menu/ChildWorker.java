package com.upgrad.tms.menu;

import com.upgrad.tms.entities.Task;
import com.upgrad.tms.repository.AssigneeRepository;
import com.upgrad.tms.util.TaskStatus;

public class ChildWorker implements Runnable {

    private final Task task;
    private final Object lock;

    public ChildWorker(Task task, Object lock) {
        this.task = task;
        this.lock = lock;
    }

    @Override
    public void run() {
        String name = Thread.currentThread().getName();
        if (task.getTaskStatus() != TaskStatus.DONE)
        synchronized (lock) {
            try{
                System.out.println(name+" waiting to get notified at time:"+System.currentTimeMillis());
                lock.wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            System.out.println(name+" waiter thread got notified at time:"+System.currentTimeMillis());
            //process the message now
            System.out.println(name+" processed: "+msg.getMsg());
        }
    }

    @Override
    public void doWork() {

    }
}
