package ru.blodge.bserver.commander.telegram.menu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import ru.blodge.bserver.commander.telegram.menu.docker.DockerContainerMenuFactory;
import ru.blodge.bserver.commander.telegram.menu.docker.DockerContainersMenuFactory;
import ru.blodge.bserver.commander.telegram.menu.docker.DockerImagesMenuFactory;
import ru.blodge.bserver.commander.telegram.menu.docker.DockerMenuFactory;
import ru.blodge.bserver.commander.telegram.menu.system.SystemMenuFactory;

import java.util.Map;

public class MenuFactory implements MessageFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(MenuFactory.class);

    public static final String MAIN_MENU_SELECTOR = "1";
    public static final String SYSTEM_MENU_SELECTOR = "2";
    public static final String DOCKER_MENU_SELECTOR = "3";
    public static final String DOCKER_CONTAINERS_MENU_SELECTOR = "4";
    public static final String DOCKER_IMAGES_MENU_SELECTOR = "5";
    public static final String DOCKER_CONTAINER_MENU_SELECTOR = "6";

    private static final Map<String, MessageFactory> factorySelectorMap = Map.of(
            MAIN_MENU_SELECTOR, new MainMenuFactory(),
            SYSTEM_MENU_SELECTOR, new SystemMenuFactory(),
            DOCKER_MENU_SELECTOR, new DockerMenuFactory(),
            DOCKER_CONTAINERS_MENU_SELECTOR, new DockerContainersMenuFactory(),
            DOCKER_IMAGES_MENU_SELECTOR, new DockerImagesMenuFactory(),
            DOCKER_CONTAINER_MENU_SELECTOR, new DockerContainerMenuFactory()
    );

    private static final MenuFactory instance = new MenuFactory();

    public static MenuFactory instance() {
        return instance;
    }

    private MenuFactory() {
    }

    @Override
    public EditMessageText buildMenu(String callbackData) {
        String factorySelector = callbackData.split("\\.")[0];
        return factorySelectorMap.get(factorySelector).buildMenu(callbackData);
    }

}
