package ru.blodge.bserver.commander.telegram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.blodge.bserver.commander.telegram.dispatchers.TelegramUpdateDispatcher;
import ru.blodge.bserver.commander.telegram.dispatchers.UpdateDispatcher;

import static ru.blodge.bserver.commander.telegram.TelegramBotConfig.TELEGRAM_BOT_TOKEN;
import static ru.blodge.bserver.commander.telegram.TelegramBotConfig.TELEGRAM_BOT_USERNAME;

public class CommanderBot extends TelegramLongPollingBot {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommanderBot.class);

    private static final CommanderBot instance = new CommanderBot();

    public static CommanderBot instance() {
        return instance;
    }

    private final UpdateDispatcher dispatcher = new TelegramUpdateDispatcher();

    private CommanderBot() {
    }

    @Override
    public void onUpdateReceived(Update update) {
        LOGGER.info("Received new update {}", update);
        dispatcher.dispatch(update);
    }

    @Override
    public String getBotToken() {
        return TELEGRAM_BOT_TOKEN;
    }

    @Override
    public String getBotUsername() {
        return TELEGRAM_BOT_USERNAME;
    }

}
