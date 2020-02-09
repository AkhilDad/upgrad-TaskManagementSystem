package com.upgrad.tms.exchanger;

import com.upgrad.tms.entities.Task;
import com.upgrad.tms.menu.AbstractWorker;
import com.upgrad.tms.repository.AssigneeRepository;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ExchangeTaskWorker extends AbstractWorker implements Runnable {

    private final Task task;
    private final Exchanger<String> stringExchanger;

    public ExchangeTaskWorker(AssigneeRepository assigneeRepository, Task task, Exchanger<String> stringExchanger) {
        super(assigneeRepository);
        this.task = task;
        this.stringExchanger = stringExchanger;
    }

    @Override
    public void doWork() {
        try {
            System.out.println("Pushing to change title for task: "+task.getId());
            String exchangedTitle = stringExchanger.exchange(task.getTitle(), 3, TimeUnit.SECONDS);
            System.out.println("For Task Id: "+task.getId()+" From <"+task.getTitle()+"> To <"+exchangedTitle+">");
            task.setTitle(exchangedTitle);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            System.out.println("Timeout happened for task id: "+task.getId());
        }
        processTask(task);
        updateInFile();
    }

    @Override
    public void run() {
        doWork();
    }
}
