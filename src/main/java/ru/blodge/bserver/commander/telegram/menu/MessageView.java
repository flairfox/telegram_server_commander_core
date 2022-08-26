package ru.blodge.bserver.commander.telegram.menu;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public interface MessageView {

    void display(CallbackQuery callbackQuery);

}
