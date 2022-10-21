package ru.blodge.bserver.commander.telegram.dispatchers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.blodge.bserver.commander.telegram.handlers.AccessDeniedErrorHandler;
import ru.blodge.bserver.commander.telegram.handlers.CallbackQueryHandler;
import ru.blodge.bserver.commander.telegram.handlers.UpdateHandler;

import java.util.Optional;

import static ru.blodge.bserver.commander.configuration.TelegramBotConfig.ADMIN_USERS_IDS;

public class TelegramUpdateDispatcher implements UpdateDispatcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(TelegramUpdateDispatcher.class);

    private final UpdateDispatcher commandDispatcher = new CommandDispatcher();

    private final UpdateHandler accessDeniedErrorHandler = new AccessDeniedErrorHandler();
    private final UpdateHandler callbackQueryHandler = new CallbackQueryHandler();

    public void dispatch(Update update) {
        LOGGER.debug("Received update.");

        // Сообщение пришло НЕ от администратора
        Optional<Long> userId = getUserId(update);
        if (userId.isEmpty()) {
            return;
        } else if (!ADMIN_USERS_IDS.contains(userId.get())) {
            accessDeniedErrorHandler.handle(update);
            return;
        }

        if (update.hasMessage()) {
            // Сообщение является командой
            if (update.getMessage().getText().startsWith("/")) {
                commandDispatcher.dispatch(update);
            }

            // Сообщение содержит CallbackQuery
        } else if (update.hasCallbackQuery()) {
            callbackQueryHandler.handle(update);
        }

    }

    private Optional<Long> getUserId(Update update) {
        if (update.hasMessage()) {
            return Optional.of(update.getMessage().getFrom().getId());
        } else if (update.hasCallbackQuery()) {
            return Optional.of(update.getCallbackQuery().getFrom().getId());
        } else {
            return Optional.empty();
        }
    }

}
