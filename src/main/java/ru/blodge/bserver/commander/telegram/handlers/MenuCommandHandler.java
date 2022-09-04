package ru.blodge.bserver.commander.telegram.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.blodge.bserver.commander.telegram.CommanderBot;
import ru.blodge.bserver.commander.utils.builders.InlineKeyboardBuilder;

import static ru.blodge.bserver.commander.telegram.menu.MenuRouter.DOCKER_CONTAINERS_MENU_SELECTOR;
import static ru.blodge.bserver.commander.telegram.menu.MenuRouter.RESOURCE_UTILIZATION_MENU_SELECTOR;

public class MenuCommandHandler implements UpdateHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MenuCommandHandler.class);

    @Override
    public void handle(Update update) {
        long chatId = update.getMessage().getChatId();

        SendMessage mainMenuMessage = new SendMessage();
        mainMenuMessage.setChatId(chatId);
        mainMenuMessage.setParseMode("markdown");

        mainMenuMessage.setText("""
                *Главное меню*
                                
                Это главное меню твоего Сервера, здесь можно узнать о:
                """);

        InlineKeyboardMarkup keyboard = new InlineKeyboardBuilder()
                .addButton("Утилизации CPU, RAM и SWAP", RESOURCE_UTILIZATION_MENU_SELECTOR)
                .nextRow()
                .addButton("Docker-контейнерах", DOCKER_CONTAINERS_MENU_SELECTOR)
                .build();

        mainMenuMessage.setReplyMarkup(keyboard);

        try {
            CommanderBot.instance().execute(mainMenuMessage);
        } catch (TelegramApiException e) {
            LOGGER.error("Error while sending message", e);
        }
    }

}
