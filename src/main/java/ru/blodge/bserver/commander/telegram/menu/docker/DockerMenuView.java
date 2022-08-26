package ru.blodge.bserver.commander.telegram.menu.docker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.blodge.bserver.commander.telegram.CommanderBot;
import ru.blodge.bserver.commander.telegram.menu.MessageView;
import ru.blodge.bserver.commander.utils.EditMessageBuilder;
import ru.blodge.bserver.commander.utils.InlineKeyboardBuilder;

import static ru.blodge.bserver.commander.telegram.menu.MenuRouter.DOCKER_CONTAINERS_MENU_SELECTOR;
import static ru.blodge.bserver.commander.telegram.menu.MenuRouter.DOCKER_IMAGES_MENU_SELECTOR;
import static ru.blodge.bserver.commander.telegram.menu.MenuRouter.MAIN_MENU_SELECTOR;
import static ru.blodge.bserver.commander.utils.Emoji.BACK_EMOJI;

public class DockerMenuView implements MessageView {

    private static final Logger LOGGER = LoggerFactory.getLogger(DockerMenuView.class);

    @Override
    public void display(CallbackQuery callbackQuery) {

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardBuilder()
                .addButton("Docker-контейнеры", DOCKER_CONTAINERS_MENU_SELECTOR)
                .nextRow()
                .addButton("Docker-образы", DOCKER_IMAGES_MENU_SELECTOR)
                .nextRow()
                .addButton(BACK_EMOJI + "Назад", MAIN_MENU_SELECTOR)
                .build();

        EditMessageText dockerMenuMessage = new EditMessageBuilder(callbackQuery)
                .withMessageText("""
                        *Docker*
                                        
                        Здесь есть все, что вы хотели знать о Docker
                        """)
                .withReplyMarkup(keyboardMarkup)
                .build();
        
        try {
            CommanderBot.instance().execute(dockerMenuMessage);
        } catch (TelegramApiException e) {
            LOGGER.error("Error executing docker menu message", e);
        }
    }

}
