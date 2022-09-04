package ru.blodge.bserver.commander.telegram.menu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import ru.blodge.bserver.commander.telegram.menu.docker.DockerContainerInfoView;
import ru.blodge.bserver.commander.telegram.menu.docker.DockerContainersListView;
import ru.blodge.bserver.commander.telegram.menu.system.DrivesInfoView;
import ru.blodge.bserver.commander.telegram.menu.system.ResourceUtilizationView;
import ru.blodge.bserver.commander.telegram.menu.system.SystemInfoView;

import java.util.Arrays;
import java.util.Map;

public class MenuRouter {

    private static final Logger LOGGER = LoggerFactory.getLogger(MenuRouter.class);

    public static final String MAIN_MENU_SELECTOR = "1";
    public static final String DOCKER_CONTAINERS_MENU_SELECTOR = "2";
    public static final String DOCKER_CONTAINER_INFO_MENU_SELECTOR = "3";
    public static final String SYSTEM_INFO_MENU_SELECTOR = "4";
    public static final String RESOURCE_UTILIZATION_MENU_SELECTOR = "5";
    public static final String DRIVES_INFO_MENU_SELECTOR = "6";

    private static final Map<String, MessageView> viewSelectorMap = Map.of(
            MAIN_MENU_SELECTOR, new MainMenuView(),
            DOCKER_CONTAINERS_MENU_SELECTOR, new DockerContainersListView(),
            DOCKER_CONTAINER_INFO_MENU_SELECTOR, new DockerContainerInfoView(),
            SYSTEM_INFO_MENU_SELECTOR, new SystemInfoView(),
            RESOURCE_UTILIZATION_MENU_SELECTOR, new ResourceUtilizationView(),
            DRIVES_INFO_MENU_SELECTOR, new DrivesInfoView()
    );

    private MenuRouter() {
    }

    public static void route(CallbackQuery callbackQuery) {
        LOGGER.debug("Received callback query with data {}.", callbackQuery.getData());
        String[] callbackData = callbackQuery.getData().split("\\.");
        String viewSelector = callbackData[0];
        MessageView messageView = viewSelectorMap.get(viewSelector);
        if (messageView == null) {
            LOGGER.error("No MessageView is available for selector {}", viewSelector);
        } else {
            String[] args = Arrays.copyOfRange(callbackData, 1, callbackData.length);
            MessageContext messageContext = new MessageContext(
                    callbackQuery.getMessage().getChatId(),
                    callbackQuery.getMessage().getMessageId(),
                    args);
            messageView.display(messageContext);
        }
    }

}
