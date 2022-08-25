package ru.blodge.bserver.commander.telegram.menu;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

public interface MessageFactory {

    EditMessageText buildMenu(String callbackData);

}
