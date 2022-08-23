package ru.blodge.bserver.commander.menu.docker;

import ru.blodge.bserver.commander.docker.DockerAgent;
import ru.blodge.bserver.commander.menu.MenuEntry;

import java.util.List;

import static ru.blodge.bserver.commander.menu.docker.DockerMenuEntry.DOCKER_MENU_SELECTOR;

public class DockerContainersMenuEntry implements MenuEntry {

    public static final String DOCKER_CONTAINERS_MENU_SELECTOR = "docker-containers-menu-selector";

    @Override
    public String getSelector() {
        return DOCKER_CONTAINERS_MENU_SELECTOR;
    }

    @Override
    public String getTitle() {
        return "Список docker-контейнеров";
    }

    @Override
    public String getHtmlBody() {
        StringBuilder sb = new StringBuilder();
        DockerAgent.instance().getContainers().forEach(container -> {
            sb.append("- ");
            sb.append(container.getNames()[0]);
            sb.append("\t");
            sb.append(container.getStatus());
            sb.append("\n");
        });

        return """
                <b>Список docker-контейнеров</b>
                                
                <pre>%s</pre>
                """.formatted(sb);
    }

    @Override
    public boolean allowUpdate() {
        return true;
    }

    @Override
    public String getPreviousMenuSelector() {
        return DOCKER_MENU_SELECTOR;
    }

    @Override
    public List<String> getSubMenuSelectors() {
        return null;
    }


}
