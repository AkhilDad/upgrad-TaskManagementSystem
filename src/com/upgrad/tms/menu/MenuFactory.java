package com.upgrad.tms.menu;

class MenuFactory {
    public static OptionsMenu getMenuByType(OptionsMenuType optionsMenuType) {
        switch (optionsMenuType) {
            case PROJECT_MANAGER:
                return new ManagerMenu();
            case ASSIGNEE:
                return new AssigneeMenu();
        }
        throw new RuntimeException("Options Menu Type not supported");
        
    }

}
