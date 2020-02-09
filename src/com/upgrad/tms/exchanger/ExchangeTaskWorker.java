package com.upgrad.tms.exchanger;

import com.upgrad.tms.entities.Task;
import com.upgrad.tms.menu.AbstractWorker;
import com.upgrad.tms.repository.AssigneeRepository;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ExchangeTaskWorker extends AbstractWorker implements Runnable {

    private final Exchanger<String> titleExchanger;
    private Task task;

    public ExchangeTaskWorker(AssigneeRepository assigneeRepository, Task task, Exchanger<String> titleExchanger) {
        super(assigneeRepository);
        this.titleExchanger = titleExchanger;
        this.task = task;
    }

    @Override
    public void doWork() {
        try {
            System.out.println("Pushing to change title for task: "+task.getId());
            String exchangedTitle = titleExchanger.exchange(task.getTitle(), 3, TimeUnit.SECONDS);
            System.out.println("For task id: "+task.getId()+" Changing title from:< "+task.getTitle()+ "> to: <"+exchangedTitle+">");
            task.setTitle(exchangedTitle);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            System.out.println("---Timeout happened for task: "+task.getId());
        }
        processTask(task);
        updateInFile();

    }

    @Override
    public void run() {
        doWork();
    }
}
