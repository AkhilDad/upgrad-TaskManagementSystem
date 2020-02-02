package com.upgrad.tms.menu;

import java.util.Scanner;

class MainMenu {

    private void getLoginDetails() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Username");
        String username = sc.nextLine();
        System.out.println("Enter password");
        String passwd = sc.nextLine();
        processInput(username, passwd);
    }

    private void processInput ( String username, String passwd){
        if("manager".equals(username) && "manager".equals(passwd)){
            showMenu(OptionsMenuType.PROJECT_MANAGER);
        } else if ("assignee".equals(username) && "assignee".equals(passwd)){
            showMenu (OptionsMenuType.ASSIGNEE);
        }
    }

    private void showMenu (OptionsMenuType optionsMenuType){
        MenuFactory.getMenuByType(optionsMenuType).showTopOptions();
    }

    public static void exit() {
        System.exit(0);
    }

    public static void main (String args[]){
        new MainMenu().getLoginDetails();
    }
}
