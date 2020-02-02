package com.upgrad.tms.util;

import com.upgrad.tms.entities.Assignee;
import com.upgrad.tms.exception.EntityListFullException;

import java.io.Serializable;

public class EntityList<T> implements Serializable {
    private int counter;
    private int maxSize;
    private Object[] assignees;

    public EntityList() {
        this (10);
    }

    public EntityList(int n) {
        counter = 0;
        maxSize = n;
        assignees = new Object[n];
    }

    public void add (T assignee) throws EntityListFullException {
        if (counter < maxSize) {
            assignees[counter] = assignee;
            counter++;
        }
        else {
            throw new EntityListFullException("Assignee List is full");
        }
    }

    public T get (int index) {
        if (index < counter) {
            return (T)assignees[index];
        }
        else {
            throw new IndexOutOfBoundsException("Index must be less than " + counter);
        }
    }

    public int size () {
        return counter;
    }

    public T[] getAssignees() {
        return (T[])assignees;
    }
}
