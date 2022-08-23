package ru.blodge.bserver.commander.menu;

import java.util.List;

public interface MenuEntry {

    String getSelector();

    String getTitle();

    String getDescription();

    String getPreviousMenuSelector();

    List<String> getSubMenuSelectors();

}
