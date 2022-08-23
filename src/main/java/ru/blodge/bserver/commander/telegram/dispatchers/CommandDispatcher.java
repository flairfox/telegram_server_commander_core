package ru.blodge.bserver.commander.telegram.dispatchers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.blodge.bserver.commander.telegram.CommanderBot;
import ru.blodge.bserver.commander.telegram.handlers.MenuCommandHandler;
import ru.blodge.bserver.commander.telegram.handlers.UpdateHandler;

import java.util.Map;

public class CommandDispatcher implements UpdateDispatcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandDispatcher.class);

    private static final Map<String, UpdateHandler> updateHandlersMap = Map.of(
            "/menu", new MenuCommandHandler()
    );

    @Override
    public void dispatch(Update update) {
        String command = update.getMessage().getText();

        if (command == null) {
            long chatId = update.getMessage().getChatId();
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText("Неизвестная команда!");

            try {
                CommanderBot.instance().execute(sendMessage);
            } catch (TelegramApiException e) {
                LOGGER.error("Error while sending message", e);
            }
        }

        updateHandlersMap.get(command).handle(update);
    }

}
