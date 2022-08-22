package ru.blodge.bserver.commander.telegram;

import com.github.dockerjava.api.model.Container;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.blodge.bserver.commander.docker.DockerApi;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class CommanderBot extends TelegramLongPollingBot {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommanderBot.class);
    private static final long ADMIN_USER_ID = Long.parseLong(System.getenv("ADMIN_USER_ID"));
    private static final String ACCESS_DENIED_FILE = "media/access_denied.mp4";

    private static String ACCESS_DENIED_FILE_ID = null;

    @Override
    public void onUpdateReceived(Update update) {
        LOGGER.info("Received new update {}", update);

        if (update.getMessage().getFrom().getId() != ADMIN_USER_ID) {
            handleAccessDeniedError(update);
            return;
        }

        List<Container> containers = DockerApi.instance.getContainers();
        containers.forEach(container -> System.out.println(container.getNames()[0]));
    }

    private void handleAccessDeniedError(Update update) {
        LOGGER.warn("User with ID {} is not an administrator", update.getMessage().getFrom().getId());

        // Сообщение на которое нужно отправить ответ
        Message message = update.getMessage();

        // Файл с видео-ответом
        InputFile inputFile = new InputFile();
        if (ACCESS_DENIED_FILE_ID == null) {
            ClassLoader classLoader = getClass().getClassLoader();
            try (InputStream inputStream = classLoader.getResourceAsStream(ACCESS_DENIED_FILE)) {
                inputFile.setMedia(inputStream, ACCESS_DENIED_FILE);
                sendAccessDeniedResponse(message, inputFile);
            } catch (IOException e) {
                LOGGER.error("Error while loading {}", ACCESS_DENIED_FILE);
            }
        } else {
            inputFile.setMedia(ACCESS_DENIED_FILE_ID);
            sendAccessDeniedResponse(message, inputFile);
        }
    }

    private void sendAccessDeniedResponse(Message message, InputFile responseFile) {
        SendVideo accessDeniedVideo = new SendVideo();
        accessDeniedVideo.setChatId(message.getChatId());
        accessDeniedVideo.setVideo(responseFile);

        try {
            Message sentMessage = execute(accessDeniedVideo);
            ACCESS_DENIED_FILE_ID = sentMessage.getVideo().getFileId();
        } catch (TelegramApiException e) {
            LOGGER.error("Error while sending response to {}", message);
        }
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
