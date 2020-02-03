package com.upgrad.tms.menu;

import com.upgrad.tms.entities.Assignee;
import com.upgrad.tms.entities.Meeting;
import com.upgrad.tms.entities.Task;
import com.upgrad.tms.entities.Todo;
import com.upgrad.tms.exception.NotFoundException;
import com.upgrad.tms.repository.AssigneeRepository;
import com.upgrad.tms.util.DateUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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
                seeAllTasks(); //done
                break;
            case 2:
                seeTodayTasks(); //done
                break;
            case 3:
                seeTaskSortedOnPriority();//done
                break;
            case 4:
                seeTaskByCategory(); //done
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
        Map<String, List<Task>> listMap = new TreeMap<>();
        List<Class<? extends Task>> classTypes = List.of( Todo.class, Meeting.class);
        //init list for all classes there in classTypes
        for (Class<? extends Task> classType : classTypes) {
            listMap.put(classType.getSimpleName(), new ArrayList<>());
        }

        Assignee assignee = assigneeRepository.getAssignee(MainMenu.loggedInUserName);
        List<Task> taskList = assignee.getTaskCalendar().getTaskList();
        for (Task task : taskList) {
            List<Task> taskTypeList = listMap.get(task.getClass().getSimpleName());
            if (taskTypeList != null) {
                taskTypeList.add(task);
            } else {
                throw new NotFoundException("Task type not found");
            }
        }

        for (Map.Entry<String, List<Task>> listEntry : listMap.entrySet()) {
            System.out.println("======= Category: " + listEntry.getKey() + " =======");
            if (listEntry.getValue().isEmpty()) {
                System.out.println("No task in this category");
            }
            for (Task task : listEntry.getValue()) {
                task.printTaskOnConsole();
            }
            System.out.println("=======================");
        }


    }

    private void seeTaskSortedOnPriority() {
        if (MainMenu.loggedInUserName != null) {
            Assignee assignee = assigneeRepository.getAssignee(MainMenu.loggedInUserName);
            List<Task> taskList = assignee.getTaskCalendar().getTaskList();
            PriorityQueue<Task> taskPriorityQueue = new PriorityQueue<>(new Comparator<Task>() {
                @Override
                public int compare(Task t1, Task t2) {
                    return t2.getPriority() - t1.getPriority();
                }
            });
            for (Task task : taskList) {
                if (DateUtils.isSameDate(task.getDueDate(), Calendar.getInstance().getTime())) {
                    taskPriorityQueue.add(task);
                }
            }
            while (!taskPriorityQueue.isEmpty()) {
                taskPriorityQueue.poll().printTaskOnConsole();
            }
        }
    }

    private void seeTodayTasks() {
        if (MainMenu.loggedInUserName != null) {
            Assignee assignee = assigneeRepository.getAssignee(MainMenu.loggedInUserName);
            List<Task> todayTaskList = assignee.getTaskCalendar().getTaskList().stream()
                    .filter(task -> DateUtils.isSameDate(task.getDueDate(), Calendar.getInstance().getTime()))
                    .collect(Collectors.toList());
            if (todayTaskList.isEmpty()) {
                System.out.println("Hurray! No task for today");
            } else {
                printTaskList(todayTaskList);
            }
        }


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
