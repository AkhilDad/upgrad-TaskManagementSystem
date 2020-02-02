package com.upgrad.tms.menu;

import java.util.InputMismatchException;
import java.util.Scanner;

class ManagerMenu implements OptionsMenu {
    @Override
    public void showTopOptions() {
        Scanner sc = new Scanner(System.in);
        System.out.println("1. Create new user");
        System.out.println("2. Display all users");
        System.out.println("3. Create another manager");
        System.out.println("4. Create task and assign");
        System.out.println("5. Get all assignees who have a task on the given date");
        System.out.println("6. Get all tasks based on priority");
        System.out.println("7. Exit");
        int choice = -1;
        try {
            choice = sc.nextInt();
        } catch (InputMismatchException e){
            System.out.println("Wrong input type; only numbers are allowed. Please enter again");
        }
        switch (choice) {
            case 1:
                createUser();
                break;
            case 2:
                displayAllUsers();
                break;
            case 3:
                createManager();
                break;
            case 4:
                createTaskAndAssign();
                break;
            case 5:
                getAssigneesForSpecificDate();
                break;
            case 6:
                getAllTasksBasedOnPriority();
                break;
            case 7:
                MainMenu.exit();
                break;
            default:
                wrongInput();
        }
        showTopOptions();

    }

    private void getAllTasksBasedOnPriority() {
    }

    private void getAssigneesForSpecificDate() {
    }

    private void createTaskAndAssign() {
    }

    private void createManager() {
    }

    private void displayAllUsers() {
    }

    private void createUser() {
    }
}
