package com.upgrad.tms.administration;

import com.upgrad.tms.repository.AssigneeRepository;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

public class LearnReportGenerator {

    public static void main(String[] args) {
        Method[] methods = AssigneeRepository.class.getMethods();
        for (Method method : methods) {
//            System.out.println("Method: "+method.getName());
            if (method.isAnnotationPresent(ConceptsLearned.class)) {
                ConceptsLearned conceptsLearned = method.getAnnotation(ConceptsLearned.class);
                System.out.println("Method: "+method.getName());
                System.out.println("Difficulty Level: "+conceptsLearned.difficultyLevel());
                String conceptsString = Arrays.stream(conceptsLearned.concepts()).collect(Collectors.joining(", "));
                System.out.println("Concepts Learned: "+conceptsString);
                System.out.println("\n");
            }
        }
    }
}
