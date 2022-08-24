package ru.blodge.bserver.commander.menu;

import java.util.List;

import static ru.blodge.bserver.commander.menu.MenuEntryFactory.DOCKER_MENU_ENTRY_SELECTOR;
import static ru.blodge.bserver.commander.menu.MenuEntryFactory.SYSTEM_MENU_ENTRY_SELECTOR;

public class MainMenuEntry extends MenuEntry {

    public MainMenuEntry(String menuEntrySelector) {
        super(menuEntrySelector);
    }

    @Override
    public String getTitleMarkdown() {
        return "Главное меню";
    }

    @Override
    public String getBodyMarkdown() {
        return """
                <b>Главное меню</b>
                                
                <pre>Это главное меню Бобрового Сервера, здесь можно узнать о:</pre>
                """;
    }

    @Override
    public String getPreviousMenuEntrySelector() {
        return null;
    }

    @Override
    public List<String> getSubMenuEntriesSelectors() {
        return List.of(
                SYSTEM_MENU_ENTRY_SELECTOR,
                DOCKER_MENU_ENTRY_SELECTOR
        );
    }


}
