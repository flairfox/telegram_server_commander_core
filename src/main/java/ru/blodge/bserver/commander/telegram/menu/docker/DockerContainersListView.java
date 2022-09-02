package ru.blodge.bserver.commander.telegram.menu.docker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.blodge.bserver.commander.model.docker.DockerContainer;
import ru.blodge.bserver.commander.services.DockerService;
import ru.blodge.bserver.commander.telegram.CommanderBot;
import ru.blodge.bserver.commander.telegram.menu.MessageContext;
import ru.blodge.bserver.commander.telegram.menu.MessageView;
import ru.blodge.bserver.commander.utils.builders.InlineKeyboardBuilder;
import ru.blodge.bserver.commander.utils.factories.TelegramMessageFactory;

import static ru.blodge.bserver.commander.telegram.menu.MenuRouter.*;
import static ru.blodge.bserver.commander.utils.Emoji.BACK_EMOJI;
import static ru.blodge.bserver.commander.utils.Emoji.REFRESH_EMOJI;

public class DockerContainersListView implements MessageView {

    private static final Logger LOGGER = LoggerFactory.getLogger(DockerContainersListView.class);

    @Override
    public void display(MessageContext context) {

        InlineKeyboardBuilder keyboardBuilder = new InlineKeyboardBuilder();
        for (DockerContainer container : DockerService.instance().getContainers()) {
            keyboardBuilder
                    .addButton(buildContainerCaption(container), buildContainerCallbackData(container))
                    .nextRow();
        }

        InlineKeyboardMarkup keyboard = keyboardBuilder
                .addButton(REFRESH_EMOJI + " Обновить", DOCKER_CONTAINERS_MENU_SELECTOR)
                .addButton(BACK_EMOJI + " Назад", MAIN_MENU_SELECTOR)
                .build();

        EditMessageText dockerContainers = TelegramMessageFactory.buildEditMessage(
                context.chatId(),
                context.messageId(),
                """
                        *Docker-контейнеры*
                                        
                        Вот какие контейнеры я нашел:
                        """,
                keyboard);

        try {
            CommanderBot.instance().execute(dockerContainers);
        } catch (TelegramApiException e) {
            LOGGER.error("Error executing docker containers list menu message", e);
        }
    }

    private String buildContainerCaption(DockerContainer container) {
        return container.status().statusEmoji() + " " + container.names();
    }

    private String buildContainerCallbackData(DockerContainer container) {
        return DOCKER_CONTAINER_INFO_MENU_SELECTOR + "." + container.id() + ".d";
    }

}
