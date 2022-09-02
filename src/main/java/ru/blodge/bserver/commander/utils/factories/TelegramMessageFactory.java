package ru.blodge.bserver.commander.utils.factories;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public class TelegramMessageFactory {

    private TelegramMessageFactory() {}

    public static EditMessageText buildEditMessage(
            long chatId,
            int messageId,
            String text,
            InlineKeyboardMarkup keyboard) {

        EditMessageText editMessage = new EditMessageText();
        editMessage.setChatId(chatId);
        editMessage.setMessageId(messageId);
        editMessage.setParseMode("markdown");
        editMessage.setText(text);
        editMessage.setReplyMarkup(keyboard);

        return editMessage;
    }

}
