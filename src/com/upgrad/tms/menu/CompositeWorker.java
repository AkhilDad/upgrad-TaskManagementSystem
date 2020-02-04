package com.upgrad.tms.menu;

import com.upgrad.tms.entities.Task;
import com.upgrad.tms.repository.AssigneeRepository;

import java.util.List;

public class CompositeWorker extends AbstractWorker implements Runnable {

    private final List<Task> taskList;

    public CompositeWorker(List<Task> taskList, AssigneeRepository assigneeRepository) {
        super(assigneeRepository);
        this.taskList = taskList;
    }

    public void doWork() {
        for (Task task : taskList) {
            processTask(task);
        }
        updateInFile();
    }

    @Override
    public void run() {
        doWork();
    }
}
