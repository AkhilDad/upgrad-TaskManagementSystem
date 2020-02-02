package com.upgrad.tms.menu;

import java.util.InputMismatchException;
import java.util.Scanner;

class AssigneeMenu implements OptionsMenu {

    @Override
    public void showTopOptions() throws InputMismatchException {
        Scanner sc = new Scanner(System.in);
        System.out.println("1. See all tasks");
        System.out.println("2. See Today's Task");
        System.out.println("3. See Task sorted on priority");
        System.out.println("4. Tasks by task category");
        System.out.println("5. Change task status");
        System.out.println("6. Exit");
        int choice = 0;

        choice = sc.nextInt();


        switch (choice) {
            case 1:
                seeAllTasks();
                break;
            case 2:
                seeTodayTasks();
                break;
            case 3:
                seeTaskSortedOnPriority();
                break;
            case 4:
                seeTaskByCategory();
                break;
            case 5:
                showAgain();
                break;
            case 6:
                MainMenu.exit();
                break;
            default:
                wrongInput();
        }
        showTopOptions();
    }

    private void seeTaskByCategory() {
        // TODO Auto-generated method stub

    }

    private void seeTaskSortedOnPriority() {
        // TODO Auto-generated method stub

    }

    private void seeTodayTasks() {
        // TODO Auto-generated method stub

    }

    private void seeAllTasks() {
        // TODO Auto-generated method stub

    }

}
