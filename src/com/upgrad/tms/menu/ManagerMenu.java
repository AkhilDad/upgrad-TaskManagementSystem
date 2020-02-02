package com.upgrad.tms.menu;

import com.upgrad.tms.entities.Assignee;
import com.upgrad.tms.entities.Meeting;
import com.upgrad.tms.entities.Task;
import com.upgrad.tms.entities.Todo;
import com.upgrad.tms.repository.AssigneeRepository;
import com.upgrad.tms.repository.ManagerRepository;
import com.upgrad.tms.util.DateUtils;
import com.upgrad.tms.util.TaskStatus;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

public class ManagerMenu implements OptionsMenu {

    private AssigneeRepository assigneeRepository;
    private ManagerRepository managerRepository;

    public ManagerMenu() {
        try {
            assigneeRepository = AssigneeRepository.getInstance();
            managerRepository = ManagerRepository.getInstance();
        } catch (ClassNotFoundException ex) {
            System.out.println("Class not found");
            System.exit(1);
        } catch (IOException io) {
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
        System.out.println("5. Get all unique assignees who have a task on the given date");
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
                createTaskAndAssign(); //done
                break;
            case 5:
                getUniqueAssigneesForSpecificDate(); //done
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

    private void getUniqueAssigneesForSpecificDate() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the due date for which you want to get all the users who have pending tasks");
        Date specificDateForPendingUser = getDateFromUser(sc, DateUtils.DateFormat.DAY_MONTH_YEAR_SLASH_SEPARATED);
        Collection<Assignee> assignees = assigneeRepository.getUniqueAssigneesForSpecificDate(specificDateForPendingUser);
        if (assignees.isEmpty()) {
            System.out.println("No assignees are there for the given due date");
        }
        for (Assignee assignee : assignees) {
            System.out.println("Id: " + assignee.getId() + " Name: " + assignee.getName() + " UserName: " +
                                       assignee.getUsername() + " Total tasks: " +
                                       assignee.getTaskCalendar().getTaskList().size());
        }
    }

    private void createTaskAndAssign() {
        Scanner sc = new Scanner(System.in);
        Task task;
        int taskChoice = 0;
        while (!(taskChoice == 1 || taskChoice == 2)) {
            System.out.println("Enter task type.\n 1. Todo \n 2. Meeting");
            taskChoice = sc.nextInt();
        }
        if (taskChoice == 1) {
            task = createTodo();
        }
        else {
            task = createMeeting();
        }
        Assignee assignee = getAssigneeForTask(sc);
        task.setId((long) assignee.getTaskCalendar().getTaskList().size() + 1);
        assignee.getTaskCalendar().add(task);
        try {
            assigneeRepository.updateListToFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Assignee getAssigneeForTask(Scanner sc) {
        Assignee assignee = null;
        do {
            System.out.println("Enter username whom to assign task");
            String username = sc.next();
            assignee = assigneeRepository.getAssignee(username);
            if (assignee == null) {
                System.out.println("Assignee not found for the username: " + username);
            }
        } while (assignee == null);
        return assignee;

    }

    private Task createTodo() {
        Scanner sc = new Scanner(System.in);
        Todo todo = new Todo();
        fillTaskValues(sc, todo);
        System.out.print("Enter description: ");
        todo.setDescription(sc.nextLine());
        return todo;

    }

    private Task createMeeting() {
        Scanner sc = new Scanner(System.in);
        Meeting meeting = new Meeting();
        fillTaskValues(sc, meeting);
        System.out.print("Enter meeting agenda: ");
        meeting.setAgenda(sc.nextLine());
        System.out.print("Enter location: ");
        meeting.setLocation(sc.nextLine());
        System.out.print("Enter meeting url: ");
        meeting.setUrl(sc.nextLine());
        return meeting;

    }

    private void fillTaskValues(Scanner sc, Task task) {
        System.out.print("Enter title of the task: ");
        String title = sc.nextLine();
        System.out.println("Enter priority of the task [High-Low] [1-5]: ");
        int priority = sc.nextInt();
        //Just to read \n from the previous nextInt() reading
        sc.nextLine();
        Date dueDate = getDateFromUser(sc, DateUtils.DateFormat.DAY_MONTH_YEAR_HOUR_MIN_SLASH_SEPARATED);
        task.setTitle(title);
        task.setPriority(priority);
        task.setDueDate(dueDate);
        task.setStatus(TaskStatus.PENDING);

    }

    private Date getDateFromUser(Scanner sc, String dateFormat) {
        Date formattedDate = null;
        do {
            System.out.println("Enter due date [" + dateFormat + "]: ");
            String dueDateString = sc.nextLine();
            try {
                formattedDate = DateUtils.getFormattedDate(dueDateString, dateFormat);
            } catch (ParseException e) {
                System.out.println("Wrong date format, Please enter correct date");
            }
        } while (formattedDate == null);
        return formattedDate;

    }

    private void createManager() {
        Scanner sc = new Scanner(System.in);

        String username = getUserName(sc);
        System.out.println("Enter password");
        String password = sc.nextLine();
        managerRepository.saveManager(username, password);
    }

    private void displayAllUsers() {
        List<Assignee> allAssignees = assigneeRepository.getAssigneeList();
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

    private String getUserName(Scanner sc){
        System.out.println("Enter username");
        String userName = sc.nextLine();
        Assignee existingAssignee = assigneeRepository.getAssignee(userName);
        if ( existingAssignee != null || managerRepository.isManager(userName)){
            System.out.println("Username already exists.");
            return getUserName(sc);
        } else {
            return userName;
        }
    }

    private void createUser() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter name");
        String name = sc.nextLine();

        String username = getUserName(sc);
        System.out.println("Enter password");
        String password = sc.nextLine();
        Assignee assignee = new Assignee(assigneeRepository.getAssigneeList().size() + 1, name, username, password);
        try {
            assigneeRepository.saveAssignee(assignee);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }
}
