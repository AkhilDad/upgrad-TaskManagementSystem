package com.upgrad.tms.menu;

import com.upgrad.tms.repository.AssigneeRepository;
import com.upgrad.tms.repository.ManagerRepository;
import com.upgrad.tms.util.Constants;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

class MainMenu {
    private ManagerRepository managerRepository;
    private AssigneeRepository assigneeRepository;

    public MainMenu() {
        try {
            managerRepository = ManagerRepository.getInstance();
            assigneeRepository = AssigneeRepository.getInstance();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void getLoginDetails() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Username");
        String username = sc.nextLine();
        System.out.println("Enter password");
        String passwd = sc.nextLine();
        processInput(username, passwd);
    }

    private void processInput(String username, String passwd) {
        File file = new File(Constants.MANAGER_FILE_NAME);
        if (!file.exists()) {
            if ("manager".equals(username) && "manager".equals(passwd)) {
                showMenu(OptionsMenuType.PROJECT_MANAGER);
            }
        } else {
            if (managerRepository.isValidCredentials(username, passwd)) {
                showMenu(OptionsMenuType.PROJECT_MANAGER);
            }
            else if (assigneeRepository.isValidCredentials(username, passwd)) {
                showMenu(OptionsMenuType.ASSIGNEE);
            } else {
                System.out.println("Credentials are not valid. Please try again");
                getLoginDetails();
            }
        }
    }

    private void showMenu(OptionsMenuType optionsMenuType) {
        MenuFactory.getMenuByType(optionsMenuType).showTopOptions();
    }

    public static void exit() {
        System.exit(0);
    }

    public static void main(String args[]) {
        new MainMenu().getLoginDetails();
    }
}
