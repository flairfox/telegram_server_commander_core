package ru.blodge.bserver.commander.menu;

import java.util.List;

public interface MenuEntry {

    String getSelector();

    String getTitle();

    String getHtmlBody();

    String getPreviousMenuSelector();

    List<String> getSubMenuSelectors();

    default boolean allowUpdate() {
        return false;
    }

}
