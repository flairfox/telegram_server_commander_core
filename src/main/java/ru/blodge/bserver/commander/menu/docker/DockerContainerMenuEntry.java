package ru.blodge.bserver.commander.menu.docker;

import com.github.dockerjava.api.command.InspectContainerResponse;
import ru.blodge.bserver.commander.docker.DockerAgent;
import ru.blodge.bserver.commander.menu.MenuEntry;

import java.util.List;

import static ru.blodge.bserver.commander.menu.MenuEntryFactory.DOCKER_CONTAINERS_MENU_ENTRY_SELECTOR;

public class DockerContainerMenuEntry extends MenuEntry {

    private final InspectContainerResponse dockerContainer;

    public DockerContainerMenuEntry(String menuEntrySelector) {
        super(menuEntrySelector);
        String dockerContainerId = menuEntrySelector.split("\\.")[1];
        dockerContainer = DockerAgent.instance().getContainer(dockerContainerId);
    }

    @Override
    public String getTitleMarkdown() {
        return dockerContainer.getName();
    }

    @Override
    public String getBodyMarkdown() {
        return "Здесь будет информация по контейнеру " + dockerContainer.getName();
    }

    @Override
    public String getPreviousMenuEntrySelector() {
        return DOCKER_CONTAINERS_MENU_ENTRY_SELECTOR;
    }

    @Override
    public List<String> getSubMenuEntriesSelectors() {
        return null;
    }

    @Override
    public boolean allowUpdate() {
        return true;
    }
}
