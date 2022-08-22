package ru.blodge.bserver.commander.telegram;

import com.github.dockerjava.api.model.Container;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.blodge.bserver.commander.docker.DockerApi;

import java.util.List;

public class CommanderBot extends TelegramLongPollingBot {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommanderBot.class);
    private static final long ADMIN_USER_ID = Long.parseLong(System.getenv("ADMIN_USER_ID"));

    @Override
    public void onUpdateReceived(Update update) {
        LOGGER.info("Received new update {}", update);

        long userId = update.getMessage().getFrom().getId();
        if (userId != ADMIN_USER_ID) {
            LOGGER.warn("User with ID {} is not an administrator", userId);

            SendMessage accessDeniedMessage = new SendMessage();
            accessDeniedMessage.setChatId(update.getMessage().getChatId());
            accessDeniedMessage.setText("Вы кто такие?! Я вас не звал?! Идите нахуй!!!");

            try {
                execute(accessDeniedMessage);
            } catch (TelegramApiException e) {
                LOGGER.error("Error while sending response to {}", update.getMessage());
            }

            return;
        }


        List<Container> containers = DockerApi.instance.getContainers();
        containers.forEach(container -> {
            System.out.println(container.getNames()[0]);
        });
    }

    @Override
    public String getBotToken() {
        return System.getenv("TELEGRAM_BOT_TOKEN");
    }

    @Override
    public String getBotUsername() {
        return System.getenv("TELEGRAM_BOT_USERNAME");
    }

}
