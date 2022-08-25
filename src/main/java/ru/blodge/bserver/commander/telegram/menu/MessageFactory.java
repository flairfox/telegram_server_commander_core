package ru.blodge.bserver.commander.telegram.menu;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public interface MessageFactory {

    EditMessageText buildMenu(CallbackQuery callbackQuery);

}
