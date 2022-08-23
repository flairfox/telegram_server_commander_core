package ru.blodge.bserver.commander.menu.system;

import ru.blodge.bserver.commander.menu.MenuEntry;

import java.util.List;

import static ru.blodge.bserver.commander.menu.MainMenuEntry.MAIN_MENU_SELECTOR;

public class SystemMenuEntry implements MenuEntry {

    public static final String SYSTEM_MENU_SELECTOR = "system-menu-selector";

    @Override
    public String getSelector() {
        return SYSTEM_MENU_SELECTOR;
    }

    @Override
    public String getTitle() {
        return "Система";
    }

    @Override
    public String getDescription() {
        return "Общая информация о системе";
    }

    @Override
    public String getPreviousMenuSelector() {
        return MAIN_MENU_SELECTOR;
    }

    @Override
    public List<String> getSubMenuSelectors() {
        return null;
    }


}
