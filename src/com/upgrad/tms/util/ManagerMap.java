package com.upgrad.tms.util;

import com.upgrad.tms.exception.ManagerMapFullException;

public class ManagerMap {
    private KeyValuePair[] map;
    private int counter;
    private int maxSize;

    public ManagerMap () {
        this (10);
    }

    public ManagerMap (int n) {
        counter = 0;
        map = new KeyValuePair[n];
        maxSize = n;
    }

    public void put (String username, String password) throws ManagerMapFullException {
        if (counter < maxSize) {
            map[counter] = new KeyValuePair(username, password);
            counter++;
        }
        else {
            throw new ManagerMapFullException("Manager Map is full");
        }
    }

    public boolean containsKey(String key) {
        for (int i=0; i<counter; i++) {
            if (key.equals(map[i].getKey())) {
                return true;
            }
        }
        return false;
    }

    public String get (String key) {
        String value = null;
        for (int i=0; i<counter; i++) {
            if (key.equals(map[i].getKey())) {
                value = map[i].getValue();
            }
        }
        return value;
    }
}

