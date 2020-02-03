package com.upgrad.tms.administration;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConceptsLearned {
    enum DifficultyLevel {EASY, MEDIUM, DIFFICULT}
    String[] concepts();
    DifficultyLevel difficulty() default DifficultyLevel.MEDIUM;
}
