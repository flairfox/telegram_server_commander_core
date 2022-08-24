package ru.blodge.bserver.commander.menu.docker;

import ru.blodge.bserver.commander.menu.MenuEntry;

import java.util.List;

import static ru.blodge.bserver.commander.menu.MenuEntryFactory.DOCKER_CONTAINERS_MENU_ENTRY_SELECTOR;
import static ru.blodge.bserver.commander.menu.MenuEntryFactory.DOCKER_IMAGES_MENU_ENTRY_SELECTOR;
import static ru.blodge.bserver.commander.menu.MenuEntryFactory.MAIN_MENU_ENTRY_SELECTOR;

public class DockerMenuEntry extends MenuEntry {

    public DockerMenuEntry(String menuEntrySelector) {
        super(menuEntrySelector);
    }

    @Override
    public String getTitleMarkdown() {
        return "Docker";
    }

    @Override
    public String getBodyMarkdown() {
        return """
                <b>Docker</b>
                                
                <pre>Все что вы хотели знать о Docker</pre>
                """;
    }

    @Override
    public String getPreviousMenuEntrySelector() {
        return MAIN_MENU_ENTRY_SELECTOR;
    }

    @Override
    public List<String> getSubMenuEntriesSelectors() {
        return List.of(
                DOCKER_IMAGES_MENU_ENTRY_SELECTOR,
                DOCKER_CONTAINERS_MENU_ENTRY_SELECTOR
        );
    }


}
