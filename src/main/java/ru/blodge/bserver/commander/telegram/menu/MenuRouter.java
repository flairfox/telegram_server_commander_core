package ru.blodge.bserver.commander.telegram.menu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.blodge.bserver.commander.telegram.menu.docker.DockerContainerView;
import ru.blodge.bserver.commander.telegram.menu.docker.DockerContainersListView;
import ru.blodge.bserver.commander.telegram.menu.docker.DockerImagesListView;
import ru.blodge.bserver.commander.telegram.menu.docker.DockerMenuView;
import ru.blodge.bserver.commander.telegram.menu.system.SystemMenuView;

import java.util.Map;

public class MenuRouter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MenuRouter.class);

    public static final String MAIN_MENU_SELECTOR = "1";
    public static final String SYSTEM_MENU_SELECTOR = "2";
    public static final String DOCKER_MENU_SELECTOR = "3";
    public static final String DOCKER_CONTAINERS_MENU_SELECTOR = "4";
    public static final String DOCKER_IMAGES_MENU_SELECTOR = "5";
    public static final String DOCKER_CONTAINER_MENU_SELECTOR = "6";

    private static final Map<String, MessageView> viewSelectorMap = Map.of(
            MAIN_MENU_SELECTOR, new MainMenuView(),
            SYSTEM_MENU_SELECTOR, new SystemMenuView(),
            DOCKER_MENU_SELECTOR, new DockerMenuView(),
            DOCKER_CONTAINERS_MENU_SELECTOR, new DockerContainersListView(),
            DOCKER_IMAGES_MENU_SELECTOR, new DockerImagesListView(),
            DOCKER_CONTAINER_MENU_SELECTOR, new DockerContainerView()
    );

    private MenuRouter() {
    }

    public static void route(CallbackQuery callbackQuery) {
        String viewSelector = callbackQuery.getData().split("\\.")[0];
        viewSelectorMap.get(viewSelector)
                .display(callbackQuery);
    }

}
