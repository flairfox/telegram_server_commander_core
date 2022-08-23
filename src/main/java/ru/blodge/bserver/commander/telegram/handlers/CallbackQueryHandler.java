package ru.blodge.bserver.commander.telegram.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.blodge.bserver.commander.menu.MenuEntry;
import ru.blodge.bserver.commander.telegram.CommanderBot;

import java.util.ArrayList;
import java.util.List;

import static ru.blodge.bserver.commander.menu.MenuSelectorHolder.getMenuEntry;

public class CallbackQueryHandler implements UpdateHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CallbackQueryHandler.class);

    @Override
    public void handle(Update update) {
        long chatId = update.getCallbackQuery().getMessage().getChatId();
        int messageId = update.getCallbackQuery().getMessage().getMessageId();

        MenuEntry menuEntry = getMenuEntry(update.getCallbackQuery().getData());

        EditMessageText menuMessage = new EditMessageText();
        menuMessage.setChatId(chatId);
        menuMessage.setMessageId(messageId);
        menuMessage.setParseMode("html");
        menuMessage.setText(menuEntry.getHtmlBody());

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        if (menuEntry.getSubMenuSelectors() != null) {
            for (String menuSelector : menuEntry.getSubMenuSelectors()) {
                MenuEntry submenuEntry = getMenuEntry(menuSelector);

                List<InlineKeyboardButton> keyboardRow = new ArrayList<>();
                InlineKeyboardButton systemButton = new InlineKeyboardButton();
                systemButton.setText(submenuEntry.getTitle());
                systemButton.setCallbackData(submenuEntry.getSelector());
                keyboardRow.add(systemButton);

                rowsInline.add(keyboardRow);
            }
        }

        if (menuEntry.allowUpdate()) {
            List<InlineKeyboardButton> updateRow = new ArrayList<>();
            InlineKeyboardButton updateButton = new InlineKeyboardButton();
            updateButton.setText("Обновить");
            updateButton.setCallbackData(menuEntry.getSelector());
            updateRow.add(updateButton);
            rowsInline.add(updateRow);
        }

        if (menuEntry.getPreviousMenuSelector() != null) {
            List<InlineKeyboardButton> backRow = new ArrayList<>();
            InlineKeyboardButton backButton = new InlineKeyboardButton();
            backButton.setText("Назад");
            backButton.setCallbackData(menuEntry.getPreviousMenuSelector());
            backRow.add(backButton);
            rowsInline.add(backRow);
        }

        keyboardMarkup.setKeyboard(rowsInline);
        menuMessage.setReplyMarkup(keyboardMarkup);

        try {
            CommanderBot.instance().execute(menuMessage);
        } catch (TelegramApiException e) {
            LOGGER.error("Error while sending message", e);
        }
    }

}
