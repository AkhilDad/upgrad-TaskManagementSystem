package com.upgrad.tms.repository;

import com.upgrad.tms.exception.ManagerMapFullException;
import com.upgrad.tms.util.Constants;


import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ManagerRepository {
    private static ManagerRepository managerRepository;
    private Map<String, String> managerCredentials;

    public static ManagerRepository getInstance() throws IOException {
        if (managerRepository == null) {
            managerRepository = new ManagerRepository();
        }
        return managerRepository;
    }

    private ManagerRepository() throws IOException {
        initManagerCredentials();
    }

    private void initManagerCredentials() throws IOException {
        File file = new File(Constants.MANAGER_FILE_NAME);
        if (!file.exists()){
            file.createNewFile();
        }
        managerCredentials = new HashMap<String, String>();
        try (BufferedReader br = new BufferedReader(new FileReader(Constants.MANAGER_FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] split = line.split(",");
                managerCredentials.put(split[0], split[1]);
            }
        } catch (IOException e) {
            System.out.println("IO Exception handling");
            e.printStackTrace();
        }
    }

    public void saveManager(String userName, String password){
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(Constants.MANAGER_FILE_NAME, true));
            String line = userName+","+password +"\n";

            writer.write(line);
            writer.close();
        } catch (IOException e) {
            System.out.println("IO Exception handling");
            e.printStackTrace();
        }
    }

    public boolean isValidCredentials(String username, String passwd) {
        return (managerCredentials.containsKey(username) && managerCredentials.get(username).equals(passwd));
    }

    public boolean isManager(String username){
        return managerCredentials.containsKey(username);
    }
}
