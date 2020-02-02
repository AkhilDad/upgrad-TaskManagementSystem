package com.upgrad.tms.menu;

public interface OptionsMenu {
    void showTopOptions();

    default void wrongInput() {
        System.out.println("Entered wrong choice, input again");
        showTopOptions();
    }

    default void showAgain() {
        System.out.println("Show again");
        showTopOptions();
    }
}
