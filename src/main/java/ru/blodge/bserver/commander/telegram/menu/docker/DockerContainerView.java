package ru.blodge.bserver.commander.telegram.menu.docker;

import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.blodge.bserver.commander.services.DockerService;
import ru.blodge.bserver.commander.telegram.CommanderBot;
import ru.blodge.bserver.commander.telegram.menu.MessageView;
import ru.blodge.bserver.commander.utils.EditMessageBuilder;
import ru.blodge.bserver.commander.utils.InlineKeyboardBuilder;

import static ru.blodge.bserver.commander.telegram.menu.MenuRouter.DOCKER_CONTAINERS_MENU_SELECTOR;
import static ru.blodge.bserver.commander.telegram.menu.MenuRouter.DOCKER_CONTAINER_MENU_SELECTOR;
import static ru.blodge.bserver.commander.utils.Emoji.BACK_EMOJI;
import static ru.blodge.bserver.commander.utils.Emoji.GREEN_CIRCLE_EMOJI;
import static ru.blodge.bserver.commander.utils.Emoji.RED_CIRCLE_EMOJI;
import static ru.blodge.bserver.commander.utils.Emoji.REFRESH_EMOJI;
import static ru.blodge.bserver.commander.utils.TimeUtils.formatDuration;
import static ru.blodge.bserver.commander.utils.TimeUtils.getDuration;

public class DockerContainerView implements MessageView {

    private static final Logger LOGGER = LoggerFactory.getLogger(DockerContainerView.class);

    @Override
    public void display(CallbackQuery callbackQuery) {

        String[] callbackDataArr = callbackQuery.getData().split("\\.");
        String containerId = callbackDataArr[1];
        String action = callbackDataArr[2];

        InspectContainerResponse containerResponse = DockerService.instance().getContainer(containerId);

        switch (action) {
            case "r?" -> displayContainerRestartConfirmation(callbackQuery, containerResponse);
            case "r!" -> {
                try {
                    displayContainerRestart(callbackQuery, containerResponse);
                } catch (NotFoundException e) {
                    displayContainerNotFoundMessage(callbackQuery);
                }
            }
            default -> displayContainerInfo(callbackQuery, containerResponse);
        }

    }

    private void send(EditMessageText message) {
        try {
            CommanderBot.instance().execute(message);
        } catch (TelegramApiException e) {
            LOGGER.error("Error executing docker container menu message", e);
        }
    }

    private void displayContainerNotFoundMessage(
            CallbackQuery callbackQuery) {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardBuilder()
                .addButton("ОК", DOCKER_CONTAINERS_MENU_SELECTOR)
                .build();

        EditMessageText containerNotFoundMessage = new EditMessageBuilder(callbackQuery)
                .withMessageText("""
                        *Docker-контейнер с таким ID не найден!*
                        """)
                .withReplyMarkup(keyboardMarkup)
                .build();

        send(containerNotFoundMessage);
    }

    private void displayContainerInfo(
            CallbackQuery callbackQuery,
            InspectContainerResponse containerResponse) {

        InspectContainerResponse.ContainerState containerState = containerResponse.getState();
        String containerStatus = switch (containerState.getStatus()) {
            case "running" -> GREEN_CIRCLE_EMOJI + " " + formatDuration(getDuration(containerState.getStartedAt()));
            default -> RED_CIRCLE_EMOJI + " " + formatDuration(getDuration(containerState.getFinishedAt()));
        };

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardBuilder()
                .addButton("Перезапустить", buildContainerCallbackData(containerResponse.getId(), "r?"))
                .nextRow()
                .addButton(REFRESH_EMOJI + " Обновить", buildContainerCallbackData(containerResponse.getId(), "d"))
                .addButton(BACK_EMOJI + " Назад", DOCKER_CONTAINERS_MENU_SELECTOR)
                .build();

        EditMessageText containerInfo = new EditMessageBuilder(callbackQuery)
                .withMessageText("""
                        *Docker-контейнер*
                        `%s`

                        *ID:*\t`%s`
                        *Состояние:*\t%s
                        """.formatted(
                        containerResponse.getName().substring(1),
                        containerResponse.getId().substring(0, 12),
                        containerStatus
                ))
                .withReplyMarkup(keyboardMarkup)
                .build();

        send(containerInfo);
    }

    private void displayContainerRestartConfirmation(
            CallbackQuery callbackQuery,
            InspectContainerResponse containerResponse) {

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardBuilder()
                .addButton("Да", buildContainerCallbackData(containerResponse.getId(), "r!"))
                .addButton("Отмена", buildContainerCallbackData(containerResponse.getId(), "d"))
                .build();

        EditMessageText restartConfirmation = new EditMessageBuilder(callbackQuery)
                .withMessageText("""
                        *Docker-контейнер %s будет перезапущен!*
                                        
                        Вы действительно хотите продолжить?
                        """.formatted(
                        containerResponse.getName()
                ))
                .withReplyMarkup(keyboardMarkup)
                .build();

        send(restartConfirmation);
    }

    private void displayContainerRestart(
            CallbackQuery callbackQuery,
            InspectContainerResponse containerResponse) {

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardBuilder()
                .addButton("Назад", buildContainerCallbackData(containerResponse.getId(), "d"))
                .build();

        EditMessageText containerIsRestartingMenuMessage = new EditMessageBuilder(callbackQuery)
                .withMessageText("""
                        *Docker-контейнер %s перезапускается!*
                        """.formatted(
                        containerResponse.getName()
                ))
                .withReplyMarkup(keyboardMarkup)
                .build();

        send(containerIsRestartingMenuMessage);
        DockerService.instance().restartContainer(containerResponse.getId());
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
