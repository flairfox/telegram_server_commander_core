package ru.blodge.bserver.commander.menu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import ru.blodge.bserver.commander.menu.docker.DockerContainerMessageFactory;
import ru.blodge.bserver.commander.menu.docker.DockerContainersMessageFactory;
import ru.blodge.bserver.commander.menu.docker.DockerImagesMessageFactory;
import ru.blodge.bserver.commander.menu.docker.DockerMessageFactory;
import ru.blodge.bserver.commander.menu.system.SystemMessageFactory;

import java.util.Map;

public class MenuMessageFactory implements MessageFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(MenuMessageFactory.class);

    public static final String MAIN_MENU_SELECTOR = "1";
    public static final String SYSTEM_MENU_SELECTOR = "2";
    public static final String DOCKER_MENU_SELECTOR = "3";
    public static final String DOCKER_CONTAINERS_MENU_SELECTOR = "4";
    public static final String DOCKER_IMAGES_MENU_SELECTOR = "5";
    public static final String DOCKER_CONTAINER_MENU_SELECTOR = "6";

    private static final Map<String, MessageFactory> factorySelectorMap = Map.of(
            MAIN_MENU_SELECTOR, new MainMenuMessageFactory(),
            SYSTEM_MENU_SELECTOR, new SystemMessageFactory(),
            DOCKER_MENU_SELECTOR, new DockerMessageFactory(),
            DOCKER_CONTAINERS_MENU_SELECTOR, new DockerContainersMessageFactory(),
            DOCKER_IMAGES_MENU_SELECTOR, new DockerImagesMessageFactory(),
            DOCKER_CONTAINER_MENU_SELECTOR, new DockerContainerMessageFactory()
    );


    private static final MenuMessageFactory instance = new MenuMessageFactory();

    public static MenuMessageFactory instance() {
        return instance;
    }

    private MenuMessageFactory() {
    }

    @Override
    public EditMessageText buildMenu(String callbackData) {
        String factorySelector = callbackData.split("\\.")[0];
        return factorySelectorMap.get(factorySelector).buildMenu(callbackData);
    }

}
