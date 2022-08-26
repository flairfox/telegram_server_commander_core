package ru.blodge.bserver.commander.telegram.menu.docker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.blodge.bserver.commander.telegram.CommanderBot;
import ru.blodge.bserver.commander.telegram.menu.MessageView;
import ru.blodge.bserver.commander.utils.builders.EditMessageBuilder;
import ru.blodge.bserver.commander.utils.builders.InlineKeyboardBuilder;

import static ru.blodge.bserver.commander.telegram.menu.MenuRouter.DOCKER_MENU_SELECTOR;

public class DockerImagesListView implements MessageView {

    private static final Logger LOGGER = LoggerFactory.getLogger(DockerImagesListView.class);

    @Override
    public void display(CallbackQuery callbackQuery) {

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardBuilder()
                .addButton("Назад", DOCKER_MENU_SELECTOR)
                .build();

        EditMessageText dockerImagesMenuMessage = new EditMessageBuilder(callbackQuery)
                .withMessageText("""
                        *Docker-образы*
                                        
                        Вот какие образы я нашел:
                        """)
                .withReplyMarkup(keyboardMarkup)
                .build();

        try {
            CommanderBot.instance().execute(dockerImagesMenuMessage);
        } catch (TelegramApiException e) {
            LOGGER.error("Error executing docker images menu message", e);
        }
    }

}
