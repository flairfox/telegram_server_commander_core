package ru.blodge.bserver.commander.telegram.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.blodge.bserver.commander.telegram.CommanderBot;

import java.util.ArrayList;
import java.util.List;

public class MenuCommandHandler implements UpdateHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MenuCommandHandler.class);

    @Override
    public void handle(Update update) {
        long chatId = update.getMessage().getChatId();

        SendMessage menuMessage = new SendMessage();
        menuMessage.setChatId(chatId);
        menuMessage.setText("Главное меню");
        menuMessage.setReplyMarkup(buildMenu());

        try {
            CommanderBot.instance().execute(menuMessage);
        } catch (TelegramApiException e) {
            LOGGER.error("Error while sending message", e);
        }
    }

    private InlineKeyboardMarkup buildMenu() {

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        List<InlineKeyboardButton> systemRow = new ArrayList<>();
        InlineKeyboardButton systemButton = new InlineKeyboardButton();
        systemButton.setText("Информация о системе");
        systemButton.setCallbackData("321");
        systemRow.add(systemButton);
        rowsInline.add(systemRow);

        List<InlineKeyboardButton> dockerRow = new ArrayList<>();
        InlineKeyboardButton dockerButton = new InlineKeyboardButton();
        dockerButton.setText("Состояние Docker");
        dockerButton.setCallbackData("123");
        dockerRow.add(dockerButton);
        rowsInline.add(dockerRow);

        keyboardMarkup.setKeyboard(rowsInline);

        return keyboardMarkup;
    }
}
