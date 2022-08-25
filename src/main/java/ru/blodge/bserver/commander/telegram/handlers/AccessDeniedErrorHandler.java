package ru.blodge.bserver.commander.telegram.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.blodge.bserver.commander.telegram.CommanderBot;

import java.io.IOException;
import java.io.InputStream;

import static ru.blodge.bserver.commander.configuration.TelegramBotConfig.ACCESS_DENIED_FILE;

public class AccessDeniedErrorHandler implements UpdateHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccessDeniedErrorHandler.class);

    private static String ACCESS_DENIED_FILE_ID = null;

    public void handle(Update update) {
        LOGGER.warn("User with ID {} is not an administrator", update.getMessage().getFrom().getId());

        // Сообщение на которое нужно отправить ответ
        Message message = update.getMessage();

        // Файл с видео-ответом
        InputFile inputFile = new InputFile();
        if (ACCESS_DENIED_FILE_ID == null) {
            // todo Кэширование в temp
            // Загружаем файл с видеоответом, если ID не закэширован
            ClassLoader classLoader = getClass().getClassLoader();
            try (InputStream inputStream = classLoader.getResourceAsStream(ACCESS_DENIED_FILE)) {
                inputFile.setMedia(inputStream, ACCESS_DENIED_FILE);
                sendAccessDeniedResponse(message, inputFile);
            } catch (IOException e) {
                LOGGER.error("Error while loading {}", ACCESS_DENIED_FILE);
            }
        } else {
            // Если ID файла был закэширован, просто отправляем его по ID
            inputFile.setMedia(ACCESS_DENIED_FILE_ID);
            sendAccessDeniedResponse(message, inputFile);
        }
    }

    private void sendAccessDeniedResponse(Message message, InputFile responseFile) {
        // Создаем сообщение-видеоответ
        SendVideo accessDeniedVideo = new SendVideo();
        accessDeniedVideo.setChatId(message.getChatId());
        accessDeniedVideo.setVideo(responseFile);

        try {
            Message sentMessage = CommanderBot.instance().execute(accessDeniedVideo);
            // Обновляем в кэше ID файла видеоответа
            ACCESS_DENIED_FILE_ID = sentMessage.getVideo().getFileId();
        } catch (TelegramApiException e) {
            LOGGER.error("Error while sending response to {}", message);
        }
    }

}
