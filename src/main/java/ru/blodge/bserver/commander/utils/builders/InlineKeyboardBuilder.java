package ru.blodge.bserver.commander.utils.builders;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class InlineKeyboardBuilder {

    private final InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
    private final List<List<InlineKeyboardButton>> buttonRows = new ArrayList<>();

    private List<InlineKeyboardButton> currentRow = new ArrayList<>();

    public InlineKeyboardBuilder nextRow() {
        if (!currentRow.isEmpty()) {
            buttonRows.add(currentRow);
            currentRow = new ArrayList<>();
        }

        return this;
    }

    public InlineKeyboardBuilder addButton(String text, String callbackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData(callbackData);

        currentRow.add(button);

        return this;
    }

    public InlineKeyboardMarkup build() {
        nextRow();

        keyboardMarkup.setKeyboard(buttonRows);
        return keyboardMarkup;
    }

}
