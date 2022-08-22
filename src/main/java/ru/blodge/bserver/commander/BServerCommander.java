package ru.blodge.bserver.commander;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.blodge.bserver.commander.telegram.CommanderBot;

public class BServerCommander {

    private static final Logger LOGGER = LoggerFactory.getLogger(BServerCommander.class);

    public static void main(String[] args) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new CommanderBot());

            LOGGER.info("BServerCommander bot is up and running!");
        } catch (TelegramApiException e) {
            LOGGER.error("Error while starting BServerCommander bot!", e);
        }
    }

}
