package com.upgrad.tms.repository;

import com.upgrad.tms.entities.Assignee;
import com.upgrad.tms.exception.EntityListFullException;

import com.upgrad.tms.util.Constants;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AssigneeRepository {
    private List<Assignee> assigneeList;
    private static AssigneeRepository assigneeRepository;

    private AssigneeRepository() throws IOException, ClassNotFoundException {
        initAssignees();
    }

    public static AssigneeRepository   getInstance() throws IOException, ClassNotFoundException{
        if (assigneeRepository == null ){
            assigneeRepository = new AssigneeRepository();
        }
        return assigneeRepository;
    }

    private void initAssignees() throws IOException, ClassNotFoundException {
        File file = new File(Constants.ASSIGNEE_FILE_NAME);
        if (!file.exists()){
            file.createNewFile();
        }
        try (
                FileInputStream fi = new FileInputStream(new File(Constants.ASSIGNEE_FILE_NAME))
        ) {
            if (fi.available() > 0) {
                ObjectInputStream oi = new ObjectInputStream(fi);
                assigneeList = (List<Assignee>) oi.readObject();
                oi.close();
            }
            else {
                assigneeList = new ArrayList<Assignee>();
            }
        }
    }

    public Assignee saveAssignee(Assignee assignee) throws  IOException {
        assigneeList.add(assignee);
        updateListToFile();
        return assignee;

    }

    public void updateListToFile() throws IOException {
        try (
                FileOutputStream fileOutputStream = new FileOutputStream(new File(Constants.ASSIGNEE_FILE_NAME));
                ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream)) {
            outputStream.writeObject(assigneeList);
        }

    }

    public List<Assignee> getAssigneeList(){
        return assigneeList;
    }

    public boolean isValidCredentials(String username, String passwd) {
        for(Assignee assignee: assigneeList){
            if (assignee != null && assignee.getUsername().equals(username) &&
                assignee.getPassword().equals(passwd)){
                return true;
            }
        }
        return false;
    }
}
