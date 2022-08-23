package ru.blodge.bserver.commander.menu;

import ru.blodge.bserver.commander.menu.docker.DockerContainersMenuEntry;
import ru.blodge.bserver.commander.menu.docker.DockerImagesMenuEntry;
import ru.blodge.bserver.commander.menu.docker.DockerMenuEntry;
import ru.blodge.bserver.commander.menu.system.SystemMenuEntry;

import java.util.Map;

import static ru.blodge.bserver.commander.menu.MainMenuEntry.MAIN_MENU_SELECTOR;
import static ru.blodge.bserver.commander.menu.docker.DockerContainersMenuEntry.DOCKER_CONTAINERS_MENU_SELECTOR;
import static ru.blodge.bserver.commander.menu.docker.DockerImagesMenuEntry.DOCKER_IMAGES_MENU_SELECTOR;
import static ru.blodge.bserver.commander.menu.docker.DockerMenuEntry.DOCKER_MENU_SELECTOR;
import static ru.blodge.bserver.commander.menu.system.SystemMenuEntry.SYSTEM_MENU_SELECTOR;

public class MenuSelectorHolder {


    private static final Map<String, MenuEntry> selectorMap = Map.of(
            MAIN_MENU_SELECTOR, new MainMenuEntry(),
            SYSTEM_MENU_SELECTOR, new SystemMenuEntry(),
            DOCKER_MENU_SELECTOR, new DockerMenuEntry(),
            DOCKER_CONTAINERS_MENU_SELECTOR, new DockerContainersMenuEntry(),
            DOCKER_IMAGES_MENU_SELECTOR, new DockerImagesMenuEntry()
    );

    public static MenuEntry getMenuEntry(String selector) {
        return selectorMap.get(selector);
    }
}
