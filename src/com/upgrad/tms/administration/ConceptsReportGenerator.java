package com.upgrad.tms.administration;

import com.upgrad.tms.repository.AssigneeRepository;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ConceptsReportGenerator {

    public static void main(String[] args) {
        for (Method method : AssigneeRepository.class.getMethods()) {
            if (method.isAnnotationPresent(ConceptsLearned.class)) {
                ConceptsLearned annotation = method.getAnnotation(ConceptsLearned.class);
                System.out.println("Method: "+method.getName());
                System.out.println("Difficulty Level: "+annotation.difficulty() );
                String concepts = Arrays.stream(annotation.concepts()).collect(Collectors.joining(", "));
                System.out.println("Concepts Learned: "+concepts);
                System.out.println("\n");
            }
        }
    }
}