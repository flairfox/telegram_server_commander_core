package ru.blodge.bserver.commander.telegram.menu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.blodge.bserver.commander.telegram.menu.docker.DockerContainerView;
import ru.blodge.bserver.commander.telegram.menu.docker.DockerContainersListView;

import java.util.Map;

public class MenuRouter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MenuRouter.class);

    public static final String MAIN_MENU_SELECTOR = "1";
    public static final String DOCKER_CONTAINERS_MENU_SELECTOR = "2";
    public static final String DOCKER_CONTAINER_MENU_SELECTOR = "3";

    private static final Map<String, MessageView> viewSelectorMap = Map.of(
            MAIN_MENU_SELECTOR, new MainMenuView(),
            DOCKER_CONTAINERS_MENU_SELECTOR, new DockerContainersListView(),
            DOCKER_CONTAINER_MENU_SELECTOR, new DockerContainerView()
    );

    private MenuRouter() {
    }

    public static void route(CallbackQuery callbackQuery) {
        LOGGER.debug("Received callback query with data {}.", callbackQuery.getData());
        String viewSelector = callbackQuery.getData().split("\\.")[0];
        MessageView messageView = viewSelectorMap.get(viewSelector);
        if (messageView == null) {
            LOGGER.error("No MessageView is available for selector {}", viewSelector);
        } else {
            messageView.display(callbackQuery);
        }
    }

}
