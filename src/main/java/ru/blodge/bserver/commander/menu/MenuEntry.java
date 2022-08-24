package ru.blodge.bserver.commander.menu;

import java.util.List;

public abstract class MenuEntry {

    protected String menuEntrySelector;

    public MenuEntry(String menuEntrySelector) {
        this.menuEntrySelector = menuEntrySelector;
    }

    public String getMenuEntrySelector() {
        return menuEntrySelector;
    }

    public boolean allowUpdate() {
        return false;
    }

    public abstract String getTitleMarkdown();

    public abstract String getBodyMarkdown();

    public abstract String getPreviousMenuEntrySelector();

    public abstract List<String> getSubMenuEntriesSelectors();

}
