package ru.blodge.bserver.commander.telegram.dispatchers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.blodge.bserver.commander.telegram.handlers.AccessDeniedErrorHandler;
import ru.blodge.bserver.commander.telegram.handlers.CallbackQueryHandler;
import ru.blodge.bserver.commander.telegram.handlers.UpdateHandler;

import static ru.blodge.bserver.commander.configuration.TelegramBotConfig.ADMIN_USER_ID;

public class TelegramUpdateDispatcher implements UpdateDispatcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramUpdateDispatcher.class);

    private final UpdateDispatcher commandDispatcher = new CommandDispatcher();

    private final UpdateHandler accessDeniedErrorHandler = new AccessDeniedErrorHandler();
    private final UpdateHandler callbackQueryHandler = new CallbackQueryHandler();

    public void dispatch(Update update) {
        if (update.hasMessage()) {

            // Сообщение пришло НЕ от администратора
            if (update.getMessage().getFrom().getId() != ADMIN_USER_ID) {
                accessDeniedErrorHandler.handle(update);
                return;
            }

            // Сообщение является командой
            if (update.getMessage().getText().startsWith("/")) {
                commandDispatcher.dispatch(update);
                return;
            }

        } else if (update.hasCallbackQuery()) {
            callbackQueryHandler.handle(update);
        }

    }

}
