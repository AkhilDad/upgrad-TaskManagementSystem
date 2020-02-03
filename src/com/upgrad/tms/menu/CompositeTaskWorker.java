package com.upgrad.tms.menu;

import com.upgrad.tms.entities.Task;
import com.upgrad.tms.repository.AssigneeRepository;

import java.util.List;

public class CompositeTaskWorker extends AbstractTaskWorker implements Runnable {

    private final List<Task> taskList;
    private final AssigneeRepository assigneeRepository;

    public CompositeTaskWorker(List<Task> taskList, AssigneeRepository assigneeRepository) {
        super(assigneeRepository);
        this.taskList = taskList;
        this.assigneeRepository = assigneeRepository;
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
