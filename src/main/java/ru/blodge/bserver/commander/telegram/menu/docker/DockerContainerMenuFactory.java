package ru.blodge.bserver.commander.telegram.menu.docker;

import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.blodge.bserver.commander.services.DockerService;
import ru.blodge.bserver.commander.telegram.menu.MenuFactory;
import ru.blodge.bserver.commander.telegram.menu.MessageFactory;
import ru.blodge.bserver.commander.utils.InlineKeyboardBuilder;

import static ru.blodge.bserver.commander.telegram.menu.MenuFactory.DOCKER_CONTAINERS_MENU_SELECTOR;
import static ru.blodge.bserver.commander.telegram.menu.MenuFactory.DOCKER_CONTAINER_MENU_SELECTOR;
import static ru.blodge.bserver.commander.utils.Emoji.BACK_EMOJI;
import static ru.blodge.bserver.commander.utils.Emoji.GREEN_CIRCLE_EMOJI;
import static ru.blodge.bserver.commander.utils.Emoji.RED_CIRCLE_EMOJI;
import static ru.blodge.bserver.commander.utils.Emoji.REFRESH_EMOJI;
import static ru.blodge.bserver.commander.utils.TimeUtils.formatDuration;
import static ru.blodge.bserver.commander.utils.TimeUtils.getDuration;

public class DockerContainerMenuFactory implements MessageFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(MenuFactory.class);

    @Override
    public EditMessageText buildMenu(CallbackQuery callbackQuery) {

        String[] callbackDataArr = callbackQuery.getData().split("\\.");
        String containerId = callbackDataArr[1];
        String action = callbackDataArr[2];

        InspectContainerResponse containerResponse = DockerService.instance().getContainer(containerId);

        return switch (action) {
            case "r?" -> buildContainerRestartConfirmationMenu(containerResponse);
            case "r!" -> {
                try {
                    DockerService.instance().restartContainer(containerId);
                    yield buildContainerRestartingMenu(containerResponse);
                } catch (NotFoundException e) {
                    LOGGER.error("Not found container with id {}", containerId);
                    yield buildContainerNotFoundMenu();
                }
            }
            default -> buildContainerInfoMenu(containerResponse);
        };

    }

    private EditMessageText buildContainerNotFoundMenu() {
        EditMessageText mainMenuMessage = new EditMessageText();

        mainMenuMessage.setParseMode("markdown");
        mainMenuMessage.setText("""
                *Docker-контейнер с таким ID не найден!*
                """);

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardBuilder()
                .addButton("ОК", DOCKER_CONTAINERS_MENU_SELECTOR)
                .build();

        mainMenuMessage.setReplyMarkup(keyboardMarkup);

        return mainMenuMessage;
    }

    private EditMessageText buildContainerInfoMenu(InspectContainerResponse containerResponse) {
        EditMessageText mainMenuMessage = new EditMessageText();

        InspectContainerResponse.ContainerState containerState = containerResponse.getState();
        String containerStatus = switch (containerState.getStatus()) {
            case "running" -> GREEN_CIRCLE_EMOJI + " Работает " + formatDuration(getDuration(containerState.getStartedAt()));
            default -> RED_CIRCLE_EMOJI + " Неизвестно";
        };

        mainMenuMessage.setParseMode("markdown");
        mainMenuMessage.setText("""
                *Docker-контейнер*
                `%s`

                *ID:*\t`%s`
                *Состояние:*\t%s
                """.formatted(
                containerResponse.getName(),
                containerResponse.getId().substring(0, 12),
                containerStatus
        ));

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardBuilder()
                .addButton("Перезапустить", buildContainerCallbackData(containerResponse.getId(), "r?"))
                .nextRow()
                .addButton(REFRESH_EMOJI + " Обновить", buildContainerCallbackData(containerResponse.getId(), "d"))
                .addButton(BACK_EMOJI + " Назад", DOCKER_CONTAINERS_MENU_SELECTOR)
                .build();

        mainMenuMessage.setReplyMarkup(keyboardMarkup);

        return mainMenuMessage;
    }

    private EditMessageText buildContainerRestartConfirmationMenu(InspectContainerResponse containerResponse) {
        EditMessageText mainMenuMessage = new EditMessageText();
        mainMenuMessage.setParseMode("markdown");
        mainMenuMessage.setText("""
                *Docker-контейнер %s будет перезапущен!*
                                
                Вы действительно хотите продолжить?
                """.formatted(
                containerResponse.getName()
        ));

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardBuilder()
                .addButton("Да", buildContainerCallbackData(containerResponse.getId(), "r!"))
                .addButton("Отмена", buildContainerCallbackData(containerResponse.getId(), "d"))
                .build();

        mainMenuMessage.setReplyMarkup(keyboardMarkup);

        return mainMenuMessage;
    }

    private EditMessageText buildContainerRestartingMenu(InspectContainerResponse containerResponse) {
        EditMessageText mainMenuMessage = new EditMessageText();
        mainMenuMessage.setParseMode("markdown");
        mainMenuMessage.setText("""
                *Docker-контейнер %s перезапускается!*
                """.formatted(
                containerResponse.getName()
        ));

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardBuilder()
                .addButton("Назад", buildContainerCallbackData(containerResponse.getId(), "d"))
                .build();

        mainMenuMessage.setReplyMarkup(keyboardMarkup);

        return mainMenuMessage;
    }

    private String buildContainerCallbackData(String containerId, String action) {
        StringBuilder sb = new StringBuilder();
        sb.append(DOCKER_CONTAINER_MENU_SELECTOR)
                .append(".").append(containerId, 0, 12);
        if (action != null) {
            sb.append(".").append(action);
        }

        return sb.toString();
    }

}
