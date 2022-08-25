package ru.blodge.bserver.commander.menu;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

import java.util.List;

public interface MessageFactory {

    EditMessageText buildMenu(String callbackData);

}
