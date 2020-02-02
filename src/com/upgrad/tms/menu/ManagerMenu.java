package com.upgrad.tms.menu;

import com.upgrad.tms.entities.Assignee;
import com.upgrad.tms.exception.AssigneeListFullException;
import com.upgrad.tms.repository.AssigneeRepository;
import com.upgrad.tms.repository.ManagerRepository;
import com.upgrad.tms.util.AssigneeList;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ManagerMenu implements OptionsMenu {

    private AssigneeRepository assigneeRepository;
    private ManagerRepository managerRepository;

    public ManagerMenu()  {
        try {
            assigneeRepository = AssigneeRepository.getInstance();
            managerRepository = ManagerRepository.getInstance();
        } catch (ClassNotFoundException ex){
            System.out.println("Class not found");
            System.exit(1);
        } catch (IOException io){
            System.out.println("io exception");
            System.exit(1);
        }
    }

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
        } catch (InputMismatchException e) {
            System.out.println("Wrong input type; only numbers are allowed. Please enter again");
        }
        switch (choice) {
            case 1:
                createUser(); //done
                break;
            case 2:
                displayAllUsers();//done
                break;
            case 3:
                createManager();//done
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
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter username");
        String username = sc.nextLine();
        System.out.println("Enter password");
        String password = sc.nextLine();
        managerRepository.saveManager(username, password);
    }

    private void displayAllUsers() {
        AssigneeList allAssignees = assigneeRepository.getAssigneeList();
        if (allAssignees.size() == 0) {
            System.out.println("No assignees has been added");
            return;
        }
        for (int i = 0; i < allAssignees.size(); i++) {
            Assignee assignee = allAssignees.get(i);
            System.out.println("Id: " + assignee.getId() + " Name: " + assignee.getName() + " UserName: " +
                                       assignee.getUsername());
        }

    }

    private void createUser() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter name");
        String name = sc.nextLine();
        System.out.println("Enter username");
        String username = sc.nextLine();
        System.out.println("Enter password");
        String password = sc.nextLine();
        Assignee assignee = new Assignee(assigneeRepository.getAssigneeList().size() + 1, name, username, password);
        try {
            assigneeRepository.saveAssignee(assignee);
        } catch (AssigneeListFullException | IOException e) {
            System.out.println(e.getMessage());
        }

    }
}
