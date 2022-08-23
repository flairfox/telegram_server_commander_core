package ru.blodge.bserver.commander.telegram.handlers;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface UpdateHandler {

    void handle(Update update);

}
