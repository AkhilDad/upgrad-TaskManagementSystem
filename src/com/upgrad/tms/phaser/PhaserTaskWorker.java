package com.upgrad.tms.phaser;

import com.upgrad.tms.entities.Task;
import com.upgrad.tms.menu.AbstractWorker;
import com.upgrad.tms.repository.AssigneeRepository;

import java.util.concurrent.Phaser;

public class PhaserTaskWorker extends PhaserAbstractWorker implements Runnable {

    private final Task task;

    public PhaserTaskWorker(AssigneeRepository assigneeRepository, Task task, Phaser phaser) {
        super(assigneeRepository, phaser);
        this.task = task;
    }

    @Override
    public void doWork() {
        processTask(task);
        updateInFile();
    }

    @Override
    public void run() {
        doWork();
    }
}
