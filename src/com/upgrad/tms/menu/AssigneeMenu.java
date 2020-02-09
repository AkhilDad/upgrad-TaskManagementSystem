package com.upgrad.tms.menu;

import com.upgrad.tms.countdownlatch.ChildWorker;
import com.upgrad.tms.countdownlatch.ParentWorker;
import com.upgrad.tms.entities.Assignee;
import com.upgrad.tms.entities.Meeting;
import com.upgrad.tms.entities.Task;
import com.upgrad.tms.entities.Todo;
import com.upgrad.tms.exception.NotFoundException;
import com.upgrad.tms.exchanger.ExchangeTaskWorker;
import com.upgrad.tms.meeting.LocationLocator;
import com.upgrad.tms.meeting.MeetingLocationUrlWorker;
import com.upgrad.tms.meeting.MeetingUrlLocationWorker;
import com.upgrad.tms.meeting.UrlLocator;
import com.upgrad.tms.phaser.PhaserTaskWorker;
import com.upgrad.tms.priority.PriorityChildWorker;
import com.upgrad.tms.priority.PriorityParentWorker;
import com.upgrad.tms.priority.ShareObject;
import com.upgrad.tms.repository.AssigneeRepository;
import com.upgrad.tms.util.DateUtils;
import com.upgrad.tms.util.TaskStatus;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
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

    private static boolean isMeeting(Task task) {
        return task instanceof Meeting;
    }

    @Override
    public void showTopOptions() throws InputMismatchException {
        Scanner sc = new Scanner(System.in);
        System.out.println("1. See all tasks");
        System.out.println("2. See Today's Task");
        System.out.println("3. See Task sorted on priority");
        System.out.println("4. Tasks by task category");
        System.out.println("5. Change task status");
        System.out.println("6. Change multiple task status together");
        System.out.println("7. Run task according to the priority");
        System.out.println("8. Print location and url details for meetings");
        System.out.println("9. Change parent tas status after multiple child task are done");
        System.out.println("10. Run all tasks in phases");
        System.out.println("11. Exchange title of the tasks");
        System.out.println("12. Exit");
        System.out.println("13. Change all task status to pending");
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
                changeTaskStatus();
                break;
            case 6:
                changeMultipleTaskStatus();
                break;
            case 7:
                runTaskAccordingToPriority();
                break;
            case 8:
                printUrlAndLocationDetails();
                break;
            case 9:
                changeParentTaskStatusAfterChild();
                break;
            case 10:
                runTasksInPhases();
                break;
            case 11:
                exchangeTitleOfTasks();
                break;
            case 12:
                MainMenu.exit();
                break;
            case 13:
                changeTaskStatusToPending();
                break;
            default:
                wrongInput();
        }
        showTopOptions();
    }

    private void exchangeTitleOfTasks() {
        List<Task> taskList = assigneeRepository.getAssignee(MainMenu.loggedInUserName).getTaskCalendar().getTaskList();
        Exchanger exchanger = new Exchanger();
        taskList.stream().map(task -> new Thread(new ExchangeTaskWorker(assigneeRepository, task, exchanger))).forEach(Thread::start);
    }

    private void runTasksInPhases() {
        List<Task> multipleTask = getMultipleTask();
        Phaser phaser = new Phaser(multipleTask.size());
        List<Thread> threadList = multipleTask.stream().map(task -> new Thread(new PhaserTaskWorker(task, assigneeRepository, phaser))).collect(Collectors.toList());
        for (Thread thread : threadList) {
            thread.start();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    private void changeParentTaskStatusAfterChild() {
        List<Task> multipleTask = getMultipleTask();
        if (multipleTask.size() > 2) {
            CountDownLatch countDownLatch = new CountDownLatch(multipleTask.size()- 1);
            CyclicBarrier cyclicBarrier = new CyclicBarrier(multipleTask.size() - 1, () -> System.out.println("Cyclic barrier reached at its stage"));
            Thread thread = new Thread(new ParentWorker(assigneeRepository, countDownLatch, multipleTask.get(0)));
            thread.start();
            for (int i = 1; i < multipleTask.size(); i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                new Thread(new ChildWorker(assigneeRepository, multipleTask.get(i), countDownLatch, cyclicBarrier)).start();
            }
        }

    }

    private void printUrlAndLocationDetails() {
        List<Task> taskList = assigneeRepository.getAssignee(MainMenu.loggedInUserName).getTaskCalendar()
                .getTaskList().stream().filter(AssigneeMenu::isMeeting).collect(Collectors.toList());
        List<Thread>  threads = new ArrayList<>(taskList.size());
        LocationLocator locationLocator = LocationLocator.getInstance();
        UrlLocator urlLocator = UrlLocator.getInstance();
        Semaphore semaphore = new Semaphore(1);
        for (int i = 0; i < taskList.size(); i++) {
            if (i % 2 == 0) {
                threads.add(new Thread(new MeetingLocationUrlWorker((Meeting) taskList.get(i), locationLocator, urlLocator, semaphore)));
            } else {
                threads.add(new Thread(new MeetingUrlLocationWorker((Meeting) taskList.get(i), locationLocator, urlLocator, semaphore)));
            }
        }
        threads.forEach(Thread::start);
    }

    private void runTaskAccordingToPriority() {
        List<Task> taskList = assigneeRepository.getAssignee(MainMenu.loggedInUserName).getTaskCalendar().getTaskList();
        ShareObject shareObject = new ShareObject();
        Thread parentThread = new Thread(new PriorityParentWorker(taskList, shareObject));
        parentThread.start();
        taskList.stream().map(task -> new Thread(new PriorityChildWorker(assigneeRepository, task, shareObject)))
                .forEach(Thread::start);

    }

    private void changeTaskStatusToPending() {
        assigneeRepository.getAssignee(MainMenu.loggedInUserName).getTaskCalendar()
                .getTaskList().stream().forEach(task -> task.setStatus(TaskStatus.PENDING));
        try {
            assigneeRepository.updateListToFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void changeMultipleTaskStatus() {
        List<Task> taskList = getMultipleTask();
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        for (Task taskItem: taskList) {
            executorService.execute(new TaskWorker(taskItem, assigneeRepository));
        }
    }

    private List<Task> getMultipleTask() {
        long taskId = 0;
        Scanner sc = new Scanner(System.in);
        List<Task> taskList = new ArrayList<>();
        Task task = null;
        do {
            System.out.println("Enter the task id to complete or -1 to process the already added: ");
            taskId = sc.nextLong();
            if (taskId != -1) {
                task = assigneeRepository.getTaskById(taskId);
                if (task == null) {
                    System.out.println("Task not found for id: "+taskId);
                } else {
                    taskList.add(task);
                }
            }
        } while (taskId != -1);
        return taskList;
    }

    private void changeTaskStatus() {
        Scanner sc = new Scanner(System.in);
        Task task = null;
        long taskId = 0;
        do {
            System.out.println("Enter the task id to complete: ");
            taskId = sc.nextLong();
            task = assigneeRepository.getTaskById(taskId);
        } while (task == null);
        Thread currentThread = Thread.currentThread();
        System.out.println("Current thread: "+currentThread.getName());
        Thread thread = new Thread(new TaskWorker(task, assigneeRepository));
        System.out.println("User Thread: "+thread.getName());
        thread.setDaemon(true);
        System.out.println("User thread: isAlive: "+thread.isAlive());
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("User thread: isAlive: "+thread.isAlive());
        System.out.println("isDaemon: "+thread.isDaemon());
        System.out.println("Main thread isDaemon: "+currentThread.isDaemon());
    }

    @Override
    public void showAgain() {
        System.out.println("This is under implementation");
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
