package ru.blodge.bserver.commander.telegram.menu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.blodge.bserver.commander.telegram.CommanderBot;
import ru.blodge.bserver.commander.utils.builders.InlineKeyboardBuilder;
import ru.blodge.bserver.commander.utils.factories.TelegramMessageFactory;

import static ru.blodge.bserver.commander.telegram.menu.MenuRouter.*;

public class MainMenuView implements MessageView {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainMenuView.class);

    @Override
    public void display(MessageContext context) {

        InlineKeyboardMarkup keyboard = new InlineKeyboardBuilder()
                .addButton("CPU, RAM и SWAP", RESOURCE_UTILIZATION_MENU_SELECTOR)
                .nextRow()
                .addButton("Дисках", DRIVES_INFO_MENU_SELECTOR)
                .nextRow()
                .addButton("Docker-контейнерах", DOCKER_CONTAINERS_MENU_SELECTOR)
                .build();

        EditMessageText mainMenuMessage = TelegramMessageFactory.buildEditMessage(
                context.chatId(),
                context.messageId(),
                """
                        *Главное меню*
                                        
                        Это главное меню твоего Сервера, здесь можно узнать о:
                        """,
                keyboard);

        try {
            CommanderBot.instance().execute(mainMenuMessage);
        } catch (TelegramApiException e) {
            LOGGER.error("Error executing main menu message", e);
        }
    }

}
