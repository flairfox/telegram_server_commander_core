package ru.blodge.bserver.commander.menu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.blodge.bserver.commander.menu.docker.DockerContainerMenuEntry;
import ru.blodge.bserver.commander.menu.docker.DockerContainersMenuEntry;
import ru.blodge.bserver.commander.menu.docker.DockerImagesMenuEntry;
import ru.blodge.bserver.commander.menu.docker.DockerMenuEntry;
import ru.blodge.bserver.commander.menu.system.SystemMenuEntry;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class MenuEntryFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(MenuEntryFactory.class);

    public static final String MAIN_MENU_ENTRY_SELECTOR = "1";
    public static final String SYSTEM_MENU_ENTRY_SELECTOR = "2";
    public static final String DOCKER_MENU_ENTRY_SELECTOR = "3";
    public static final String DOCKER_CONTAINERS_MENU_ENTRY_SELECTOR = "4";
    public static final String DOCKER_IMAGES_MENU_ENTRY_SELECTOR = "5";
    public static final String DOCKER_CONTAINER_MENU_ENTRY_SELECTOR = "6";
    private static final Map<String, Class<? extends MenuEntry>> classSelectorMap = Map.of(
            MAIN_MENU_ENTRY_SELECTOR, MainMenuEntry.class,
            SYSTEM_MENU_ENTRY_SELECTOR, SystemMenuEntry.class,
            DOCKER_MENU_ENTRY_SELECTOR, DockerMenuEntry.class,
            DOCKER_CONTAINERS_MENU_ENTRY_SELECTOR, DockerContainersMenuEntry.class,
            DOCKER_IMAGES_MENU_ENTRY_SELECTOR, DockerImagesMenuEntry.class,
            DOCKER_CONTAINER_MENU_ENTRY_SELECTOR, DockerContainerMenuEntry.class
    );

    private MenuEntryFactory() {
    }

    public static MenuEntry buildMenuEntry(String menuEntrySelector) {
        String classSelector = menuEntrySelector.split("\\.")[0];

        Class<? extends MenuEntry> menuEntryClass = classSelectorMap.get(classSelector);

        try {
            return menuEntryClass
                    .getConstructor(String.class)
                    .newInstance(menuEntrySelector);
        } catch (NoSuchMethodException e) {
            LOGGER.error("Constructor for class {} not found", menuEntryClass, e);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            LOGGER.error("Error constructing class {}", menuEntryClass, e);
        }

        return null;
    }
}
