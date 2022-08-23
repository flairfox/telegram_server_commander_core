package ru.blodge.bserver.commander.telegram.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.blodge.bserver.commander.menu.MainMenuEntry;
import ru.blodge.bserver.commander.menu.MenuEntry;
import ru.blodge.bserver.commander.telegram.CommanderBot;

import java.util.ArrayList;
import java.util.List;

import static ru.blodge.bserver.commander.menu.MenuSelectorHolder.getMenuEntry;

public class MenuCommandHandler implements UpdateHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MenuCommandHandler.class);

    @Override
    public void handle(Update update) {
        long chatId = update.getMessage().getChatId();

        SendMessage menuMessage = buildMainMenu(chatId, new MainMenuEntry());
        menuMessage.setChatId(chatId);

        try {
            CommanderBot.instance().execute(menuMessage);
        } catch (TelegramApiException e) {
            LOGGER.error("Error while sending message", e);
        }
    }

    private SendMessage buildMainMenu(long chatId, MenuEntry menuEntry) {

        SendMessage menuMessage = new SendMessage();
        menuMessage.setChatId(chatId);
        menuMessage.setParseMode("html");
        menuMessage.setText(menuEntry.getHtmlBody());

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();

        for (String menuSelector : menuEntry.getSubMenuSelectors()) {
            MenuEntry submenuEntry = getMenuEntry(menuSelector);

            List<InlineKeyboardButton> keyboardRow = new ArrayList<>();
            InlineKeyboardButton systemButton = new InlineKeyboardButton();
            systemButton.setText(submenuEntry.getTitle());
            systemButton.setCallbackData(submenuEntry.getSelector());
            keyboardRow.add(systemButton);

            rowsInline.add(keyboardRow);
        }

        keyboardMarkup.setKeyboard(rowsInline);
        menuMessage.setReplyMarkup(keyboardMarkup);


        return menuMessage;
    }
}
