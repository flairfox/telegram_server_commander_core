package ru.blodge.bserver.commander.telegram.menu.docker;

import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.exception.NotModifiedException;
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

    private static final String RESTART_CONFIRMATION_TEXT = """
            *Docker-контейнер*
            `%s`
            *будет перезапущен!*
                            
            Вы действительно хотите продолжить?
            """;
    private static final String RESTART_INFO_TEXT = """
            *Docker-контейнер*
            `%s` перезапускается!
            """;

    private static final String STOP_CONFIRMATION_TEXT = """
            *Docker-контейнер*
            `%s`
            *будет остановлен!*
                            
            Вы действительно хотите продолжить?
            """;
    private static final String STOP_INFO_TEXT = """
            *Docker-контейнер*
            `%s` останавливается!
            """;

    private static final String LAUNCH_INFO_TEXT = """
            *Docker-контейнер*
            `%s` запускается!
            """;


    private static final String RESTART_ACTION = "r";
    private static final String LAUNCH_ACTION = "l";
    private static final String STOP_ACTION = "s";

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
            // Перезапуск контейнера ======================================================== //
            case RESTART_ACTION + "?" -> displayContainerActionConfirmation(
                    callbackQuery,
                    container,
                    RESTART_ACTION,
                    RESTART_CONFIRMATION_TEXT.formatted(container.names()));
            case RESTART_ACTION + "!" -> {
                displayContainerActionMessage(
                        callbackQuery,
                        container,
                        RESTART_INFO_TEXT.formatted(container.names()));

                try {
                    DockerService.instance().restartContainer(container.id());
                } catch (NotFoundException e) {
                    displayContainerNotFoundMessage(callbackQuery, container.id());
                }
            }
            // ============================================================================== //

            // Остановка контейнера ========================================================= //
            case STOP_ACTION + "?" -> displayContainerActionConfirmation(
                    callbackQuery,
                    container,
                    STOP_ACTION,
                    STOP_CONFIRMATION_TEXT.formatted(container.names()));
            case STOP_ACTION + "!" -> {
                displayContainerActionMessage(
                        callbackQuery,
                        container,
                        STOP_INFO_TEXT.formatted(container.names()));

                try {
                    DockerService.instance().stopContainer(container.id());
                } catch (NotFoundException e) {
                    displayContainerNotFoundMessage(callbackQuery, container.id());
                } catch (NotModifiedException e) {
                    LOGGER.error("Trying to stop container with ID {}, that already stopped", containerId);
                }
            }
            // ============================================================================== //

            // Запуск контейнера ============================================================ //
            case LAUNCH_ACTION -> {
                displayContainerActionMessage(
                        callbackQuery,
                        container,
                        LAUNCH_INFO_TEXT.formatted(container.names()));

                try {
                    DockerService.instance().startContainer(container.id());
                } catch (NotFoundException e) {
                    displayContainerNotFoundMessage(callbackQuery, container.id());
                } catch (NotModifiedException e) {
                    LOGGER.error("Trying to start container with ID {}, that already started", containerId);
                }
            }
            // ============================================================================== //

            // Общая информация о контейнере ================================================ //
            default -> displayContainerInfo(callbackQuery, container);
            // ============================================================================== //
        }

    }

    private void displayContainerInfo(
            CallbackQuery callbackQuery,
            DockerContainer container) {

        InlineKeyboardBuilder keyboardBuilder = new InlineKeyboardBuilder();
        if (container.status().isRunning()) {
            keyboardBuilder
                    .addButton("Перезапустить", buildContainerCallbackData(container.id(), RESTART_ACTION + "?"))
                    .nextRow();
            keyboardBuilder
                    .addButton("Остановить", buildContainerCallbackData(container.id(), STOP_ACTION + "?"))
                    .nextRow();
        } else {
            keyboardBuilder
                    .addButton("Запустить", buildContainerCallbackData(container.id(), LAUNCH_ACTION))
                    .nextRow();
        }


        keyboardBuilder
                .addButton(REFRESH_EMOJI + " Обновить", buildContainerCallbackData(container.id(), "d"))
                .addButton(BACK_EMOJI + " Назад", DOCKER_CONTAINERS_MENU_SELECTOR);

        InlineKeyboardMarkup keyboardMarkup = keyboardBuilder.build();

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

    private void displayContainerActionConfirmation(
            CallbackQuery callbackQuery,
            DockerContainer container,
            String action,
            String text) {

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardBuilder()
                .addButton("Да", buildContainerCallbackData(container.id(), action + "!"))
                .addButton("Отмена", buildContainerCallbackData(container.id(), "d"))
                .build();

        EditMessageText restartConfirmation = new EditMessageBuilder(callbackQuery)
                .withMessageText(text)
                .withReplyMarkup(keyboardMarkup)
                .build();

        send(restartConfirmation);
    }

    private void displayContainerActionMessage(
            CallbackQuery callbackQuery,
            DockerContainer container,
            String text) {

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardBuilder()
                .addButton("Назад", buildContainerCallbackData(container.id(), "d"))
                .build();

        EditMessageText containerActionMessage = new EditMessageBuilder(callbackQuery)
                .withMessageText(text)
                .withReplyMarkup(keyboardMarkup)
                .build();

        send(containerActionMessage);
    }

    private void displayContainerNotFoundMessage(
            CallbackQuery callbackQuery,
            String containerId) {

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardBuilder()
                .addButton(BACK_EMOJI + " К списку контейнеров...", DOCKER_CONTAINERS_MENU_SELECTOR)
                .build();

        EditMessageText containerNotFoundMessage = new EditMessageBuilder(callbackQuery)
                .withMessageText("""
                        *Docker-контейнер с ID*
                        `%s`
                        *не найден!*
                        """.formatted(containerId))
                .withReplyMarkup(keyboardMarkup)
                .build();

        send(containerNotFoundMessage);
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
