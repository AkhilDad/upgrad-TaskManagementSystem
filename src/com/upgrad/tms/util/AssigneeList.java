package com.upgrad.tms.util;

import com.upgrad.tms.entities.Assignee;
import com.upgrad.tms.exception.AssigneeListFullException;

import java.io.Serializable;

public class AssigneeList implements Serializable {
    private int counter;
    private int maxSize;
    private Assignee[] assignees;

    public AssigneeList () {
        this (10);
    }

    public AssigneeList (int n) {
        counter = 0;
        maxSize = n;
        assignees = new Assignee[n];
    }

    public void add (Assignee assignee) throws AssigneeListFullException {
        if (counter < maxSize) {
            assignees[counter] = assignee;
            counter++;
        }
        else {
            throw new AssigneeListFullException("Assignee List is full");
        }
    }

    public Assignee get (int index) {
        if (index < counter) {
            return assignees[index];
        }
        else {
            throw new IndexOutOfBoundsException("Index must be less than " + counter);
        }
    }

    public int size () {
        return counter;
    }

    public Assignee[] getAssignees() {
        return assignees;
    }
}
