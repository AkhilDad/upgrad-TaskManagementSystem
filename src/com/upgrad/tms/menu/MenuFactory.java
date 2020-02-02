package com.upgrad.tms.menu;

import com.upgrad.tms.exception.NotFoundException;

class MenuFactory {
    public static OptionsMenu getMenuByType(OptionsMenuType optionsMenuType) {
        switch (optionsMenuType) {
            case PROJECT_MANAGER:
                return new ManagerMenu();
            case ASSIGNEE:
                return new AssigneeMenu();
        }
        throw new NotFoundException("Options Menu Type not supported");

    }

}
