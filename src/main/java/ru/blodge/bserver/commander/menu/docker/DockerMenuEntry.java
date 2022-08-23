package ru.blodge.bserver.commander.menu.docker;

import ru.blodge.bserver.commander.menu.MenuEntry;

import java.util.List;

import static ru.blodge.bserver.commander.menu.MainMenuEntry.MAIN_MENU_SELECTOR;
import static ru.blodge.bserver.commander.menu.docker.DockerContainersMenuEntry.DOCKER_CONTAINERS_MENU_SELECTOR;
import static ru.blodge.bserver.commander.menu.docker.DockerImagesMenuEntry.DOCKER_IMAGES_MENU_SELECTOR;

public class DockerMenuEntry implements MenuEntry {

    public static final String DOCKER_MENU_SELECTOR = "docker-menu-selector";

    @Override
    public String getSelector() {
        return DOCKER_MENU_SELECTOR;
    }

    @Override
    public String getTitle() {
        return "Docker";
    }

    @Override
    public String getHtmlBody() {
        return """
                <b>Docker</b>
                                
                <pre>Все что вы хотели знать о Docker</pre>
                """;
    }

    @Override
    public String getPreviousMenuSelector() {
        return MAIN_MENU_SELECTOR;
    }

    @Override
    public List<String> getSubMenuSelectors() {
        return List.of(
                DOCKER_IMAGES_MENU_SELECTOR,
                DOCKER_CONTAINERS_MENU_SELECTOR
        );
    }


}
