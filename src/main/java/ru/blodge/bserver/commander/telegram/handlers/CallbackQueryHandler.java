package ru.blodge.bserver.commander.telegram.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Update;

import static ru.blodge.bserver.commander.telegram.menu.MenuRouter.route;

public class CallbackQueryHandler implements UpdateHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CallbackQueryHandler.class);

    @Override
    public void handle(Update update) {
        LOGGER.debug("Received update, passing it to router...");
        route(update.getCallbackQuery());
    }

}
