package com.upgrad.tms.repository;

import com.upgrad.tms.administration.ConceptsLearned;
import com.upgrad.tms.entities.Assignee;
import com.upgrad.tms.entities.Task;

import com.upgrad.tms.menu.MainMenu;
import com.upgrad.tms.util.Constants;
import com.upgrad.tms.util.DateUtils;
import com.upgrad.tms.util.KeyValuePair;

import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AssigneeRepository {
    private List<Assignee> assigneeList;
    private static AssigneeRepository assigneeRepository;

    private Map<String, Assignee> assigneeMap;

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
                assigneeMap = assigneeList.stream().collect(Collectors.toMap(Assignee::getUsername, Function.identity()));
            }
            else {
                assigneeList = new ArrayList<Assignee>();
                assigneeMap = new HashMap<>();
            }
        }
    }

    public Assignee saveAssignee(Assignee assignee) throws  IOException {
        assigneeList.add(assignee);
        assigneeMap.put(assignee.getUsername(), assignee);
        updateListToFile();
        return assignee;

    }

    public synchronized void updateListToFile() throws IOException {
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

    public Assignee getAssignee(String username) {
        return assigneeMap.get(username);
    }

    /**
     * In this method we have learned about streams, filter, anyMatch, Collectors and Set
     * @param specificDate
     * @return
     */
    @ConceptsLearned(concepts = {"Stream", "filter", "anyMatch", "Collectors", "Set"}, difficultyLevel = ConceptsLearned.DifficultyLevel.HARD)
    public Collection<Assignee> getUniqueAssigneesForSpecificDate(Date specificDate){
        return assigneeList.stream()
                .filter(assignee -> assignee.getTaskCalendar().getTaskList().stream()
                        .anyMatch(task -> DateUtils.isSameDate(task.getDueDate(), specificDate)))
                .collect(Collectors.toSet());
    }

    /**
     * This method we have learned PriorityQueue
     * @return
     */
    @ConceptsLearned(concepts = {"PriorityQueue"})
    public PriorityQueue<KeyValuePair<Task, String>> getAllTaskAssigneePairByPriority(){
        //using priority queue and passing comparator which will check on the priority of the task
        PriorityQueue<KeyValuePair<Task, String>> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(firstPair -> firstPair.getKey().getPriority()));

        List<Assignee> allAssignee = getAssigneeList();
        for (int i = 0; i < allAssignee.size(); i++) {
            Assignee assignee = allAssignee.get(i);
            List<Task> taskList = assignee.getTaskCalendar().getTaskList();
            for (int j = 0; j < taskList.size(); j++) {
                priorityQueue.add(new KeyValuePair<>(taskList.get(j), assignee.getUsername()));
            }
        }
        return priorityQueue;
    }

    public Task getTaskById(long taskId) {
        if (MainMenu.loggedInUserName != null) {
            Assignee assignee = assigneeMap.get(MainMenu.loggedInUserName);
            Optional<Task> identifiedTask = assignee.getTaskCalendar().getTaskList().stream()
                    .filter(task -> task.getId().equals(taskId))
                    .findFirst();
            return identifiedTask.orElse(null);
        }
        return null;
    }
}
