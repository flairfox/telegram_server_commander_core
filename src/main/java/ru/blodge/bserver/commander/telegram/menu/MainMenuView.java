package ru.blodge.bserver.commander.telegram.menu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.blodge.bserver.commander.telegram.CommanderBot;
import ru.blodge.bserver.commander.utils.builders.EditMessageBuilder;
import ru.blodge.bserver.commander.utils.builders.InlineKeyboardBuilder;

import static ru.blodge.bserver.commander.telegram.menu.MenuRouter.DOCKER_CONTAINERS_MENU_SELECTOR;

public class MainMenuView implements MessageView {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainMenuView.class);

    @Override
    public void display(CallbackQuery callbackQuery) {

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardBuilder()
                .addButton("Docker-контейнерах", DOCKER_CONTAINERS_MENU_SELECTOR)
                .nextRow()
                .build();

        EditMessageText mainMenuMessage = new EditMessageBuilder(callbackQuery)
                .withMessageText("""
                        *Главное меню*
                                        
                        Это главное меню твоего Сервера, здесь можно узнать о:
                        """)
                .withReplyMarkup(keyboardMarkup)
                .build();

        try {
            CommanderBot.instance().execute(mainMenuMessage);
        } catch (TelegramApiException e) {
            LOGGER.error("Error executing main menu message", e);
        }
    }

}
