package ru.blodge.bserver.commander.menu.docker;

import com.github.dockerjava.api.model.Container;
import ru.blodge.bserver.commander.docker.DockerAgent;
import ru.blodge.bserver.commander.menu.MenuEntry;

import java.util.List;

import static ru.blodge.bserver.commander.menu.MenuEntryFactory.DOCKER_CONTAINER_MENU_ENTRY_SELECTOR;
import static ru.blodge.bserver.commander.menu.MenuEntryFactory.DOCKER_MENU_ENTRY_SELECTOR;

public class DockerContainersMenuEntry extends MenuEntry {

    public DockerContainersMenuEntry(String menuEntrySelector) {
        super(menuEntrySelector);
    }

    @Override
    public String getTitleMarkdown() {
        return "Список docker-контейнеров";
    }

    @Override
    public String getBodyMarkdown() {
        return """
                <b>Список docker-контейнеров</b>
                """;
    }

    @Override
    public boolean allowUpdate() {
        return true;
    }

    @Override
    public String getPreviousMenuEntrySelector() {
        return DOCKER_MENU_ENTRY_SELECTOR;
    }

    @Override
    public List<String> getSubMenuEntriesSelectors() {
        return DockerAgent.instance().getContainers().stream()
                .map(this::buildDockerContainerMenuEntrySelector)
                .toList();
    }

    private String buildDockerContainerMenuEntrySelector(Container container) {
        return DOCKER_CONTAINER_MENU_ENTRY_SELECTOR + "." + container.getId().substring(0, 12);
    }

}
