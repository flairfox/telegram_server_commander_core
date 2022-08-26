package ru.blodge.bserver.commander.telegram.menu.docker;

import com.github.dockerjava.api.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.blodge.bserver.commander.model.DockerContainer;
import ru.blodge.bserver.commander.services.DockerService;
import ru.blodge.bserver.commander.telegram.CommanderBot;
import ru.blodge.bserver.commander.telegram.menu.MessageView;
import ru.blodge.bserver.commander.utils.builders.EditMessageBuilder;
import ru.blodge.bserver.commander.utils.builders.InlineKeyboardBuilder;

import static ru.blodge.bserver.commander.telegram.menu.MenuRouter.DOCKER_CONTAINERS_MENU_SELECTOR;
import static ru.blodge.bserver.commander.telegram.menu.MenuRouter.DOCKER_CONTAINER_MENU_SELECTOR;
import static ru.blodge.bserver.commander.utils.Emoji.BACK_EMOJI;
import static ru.blodge.bserver.commander.utils.Emoji.REFRESH_EMOJI;

public class DockerContainerView implements MessageView {

    private static final Logger LOGGER = LoggerFactory.getLogger(DockerContainerView.class);

    @Override
    public void display(CallbackQuery callbackQuery) {

        String[] callbackDataArr = callbackQuery.getData().split("\\.");
        String containerId = callbackDataArr[1];
        String action = callbackDataArr[2];

        DockerContainer container;
        try {
            container = DockerService.instance().getContainer(containerId);
        } catch (NotFoundException e) {
            displayContainerNotFoundMessage(callbackQuery, containerId);
            return;
        }

        switch (action) {
            case "r?" -> displayContainerRestartConfirmation(callbackQuery, container);
            case "r!" -> displayContainerRestart(callbackQuery, container);
            default -> displayContainerInfo(callbackQuery, container);
        }

    }

    private void displayContainerNotFoundMessage(
            CallbackQuery callbackQuery,
            String containerId) {

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardBuilder()
                .addButton("ОК", DOCKER_CONTAINERS_MENU_SELECTOR)
                .build();

        EditMessageText containerNotFoundMessage = new EditMessageBuilder(callbackQuery)
                .withMessageText("""
                        *Docker-контейнер с ID* `%s` *не найден!*
                        """.formatted(containerId))
                .withReplyMarkup(keyboardMarkup)
                .build();

        send(containerNotFoundMessage);
    }

    private void displayContainerInfo(
            CallbackQuery callbackQuery,
            DockerContainer container) {

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardBuilder()
                .addButton("Перезапустить", buildContainerCallbackData(container.id(), "r?"))
                .nextRow()
                .addButton(REFRESH_EMOJI + " Обновить", buildContainerCallbackData(container.id(), "d"))
                .addButton(BACK_EMOJI + " Назад", DOCKER_CONTAINERS_MENU_SELECTOR)
                .build();

        EditMessageText containerInfo = new EditMessageBuilder(callbackQuery)
                .withMessageText("""
                        *Docker-контейнер*
                        `%s`

                        *ID:*\t`%s`
                        *Состояние:*\t%s
                        """.formatted(
                        container.names(),
                        container.id(),
                        container.status().statusEmoji() + " " + container.status().statusDuration()
                ))
                .withReplyMarkup(keyboardMarkup)
                .build();

        send(containerInfo);
    }

    private void displayContainerRestartConfirmation(
            CallbackQuery callbackQuery,
            DockerContainer container) {

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardBuilder()
                .addButton("Да", buildContainerCallbackData(container.id(), "r!"))
                .addButton("Отмена", buildContainerCallbackData(container.id(), "d"))
                .build();

        EditMessageText restartConfirmation = new EditMessageBuilder(callbackQuery)
                .withMessageText("""
                        *Docker-контейнер*
                        `%s`
                        *будет перезапущен!*
                                        
                        Вы действительно хотите продолжить?
                        """.formatted(
                        container.names()
                ))
                .withReplyMarkup(keyboardMarkup)
                .build();

        send(restartConfirmation);
    }

    private void displayContainerRestart(
            CallbackQuery callbackQuery,
            DockerContainer container) {

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardBuilder()
                .addButton("Назад", buildContainerCallbackData(container.id(), "d"))
                .build();

        EditMessageText containerIsRestartingMenuMessage = new EditMessageBuilder(callbackQuery)
                .withMessageText("""
                        *Docker-контейнер*
                        `%s` перезапускается!
                        """.formatted(
                        container.names()
                ))
                .withReplyMarkup(keyboardMarkup)
                .build();

        send(containerIsRestartingMenuMessage);

        try {
            DockerService.instance().restartContainer(container.id());
        } catch (NotFoundException e) {
            displayContainerNotFoundMessage(callbackQuery, container.id());
        }
    }

    
    private void send(EditMessageText message) {
        try {
            CommanderBot.instance().execute(message);
        } catch (TelegramApiException e) {
            LOGGER.error("Error executing docker container menu message", e);
        }
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
