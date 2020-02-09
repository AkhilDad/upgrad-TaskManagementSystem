package com.upgrad.tms.phaser;

import com.upgrad.tms.entities.Task;
import com.upgrad.tms.menu.AbstractWorker;
import com.upgrad.tms.repository.AssigneeRepository;
import com.upgrad.tms.util.TaskStatus;

import java.util.concurrent.Phaser;

public abstract class PhaserAbstractWorker extends AbstractWorker {

    private final Phaser phaser;

    public PhaserAbstractWorker(AssigneeRepository assigneeRepository, Phaser phaser) {
        super(assigneeRepository);
        this.phaser = phaser;
    }

    @Override
    protected void processTask(Task task) {
            if (task.getTaskStatus() == TaskStatus.DONE) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Task id: "+task.getId()+ " Current thread priority: "+Thread.currentThread().getPriority());
                return;
            }
            System.out.println("Starting Time: "+System.currentTimeMillis()+ " Task Id: "+task.getId()+ " Task Title: "+task.getTitle());
            task.setStatus(TaskStatus.PENDING);
            System.out.println("Pending state: "+task.getId());
            phaser.arriveAndAwaitAdvance();
            System.out.println("Next state after pending state: "+task.getId());
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            task.setStatus(TaskStatus.IN_PROGRESS);
            System.out.println("In progress state: "+task.getId());
            phaser.arriveAndAwaitAdvance();
            System.out.println("After In Progress state: "+task.getId());
            System.out.println("Task id: "+task.getId()+ " Current thread priority: "+Thread.currentThread().getPriority());
            System.out.println("Completing Time: "+System.currentTimeMillis()+ " Task Id: "+task.getId()+ " Task Title: "+task.getTitle());
            task.setStatus(TaskStatus.DONE);
    }
}
