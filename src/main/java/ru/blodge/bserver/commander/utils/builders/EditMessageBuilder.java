package ru.blodge.bserver.commander.utils.builders;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

public class EditMessageBuilder {

    private final CallbackQuery callbackQuery;

    private String messageText;
    private InlineKeyboardMarkup replyMarkup;

    public EditMessageBuilder(CallbackQuery callbackQuery) {
        this.callbackQuery = callbackQuery;
    }

    public EditMessageBuilder withMessageText(String messageText) {
        this.messageText = messageText;
        return this;
    }

    public EditMessageBuilder withReplyMarkup(InlineKeyboardMarkup replyMarkup) {
        this.replyMarkup = replyMarkup;
        return this;
    }

    public EditMessageText build() {
        EditMessageText editMessage = new EditMessageText();
        editMessage.setChatId(callbackQuery.getMessage().getChatId());
        editMessage.setMessageId(callbackQuery.getMessage().getMessageId());
        editMessage.setParseMode("markdown");

        if (messageText != null)
            editMessage.setText(messageText);
        if (replyMarkup != null)
            editMessage.setReplyMarkup(replyMarkup);

        return editMessage;
    }

}
