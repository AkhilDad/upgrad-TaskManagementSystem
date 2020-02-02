package com.upgrad.tms.menu;

import com.upgrad.tms.entities.Assignee;
import com.upgrad.tms.entities.Task;
import com.upgrad.tms.repository.AssigneeRepository;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

class AssigneeMenu implements OptionsMenu {
    private AssigneeRepository assigneeRepository;

    public AssigneeMenu()  {
        try {
            assigneeRepository = AssigneeRepository.getInstance();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
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
        if (MainMenu.loggedInUserName != null){
            Assignee assignee = assigneeRepository.getAssignee(MainMenu.loggedInUserName);
            List<Task> taskList = assignee.getTaskCalendar().getTaskList();
            printTaskList(taskList);
        } else {
            System.out.println("User is not loggedin. Please login first");
            showTopOptions();
        }

    }

    private void printTaskList(List<Task> taskList){
        for (Task task: taskList){
            task.printTaskOnConsole();
            System.out.println("\n");
        }

    }

}
