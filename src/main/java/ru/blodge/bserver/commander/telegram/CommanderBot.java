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
import ru.blodge.bserver.commander.docker.DockerAgent;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static ru.blodge.bserver.commander.telegram.TelegramBotConfig.ACCESS_DENIED_FILE;
import static ru.blodge.bserver.commander.telegram.TelegramBotConfig.ADMIN_USER_ID;
import static ru.blodge.bserver.commander.telegram.TelegramBotConfig.TELEGRAM_BOT_TOKEN;
import static ru.blodge.bserver.commander.telegram.TelegramBotConfig.TELEGRAM_BOT_USERNAME;

public class CommanderBot extends TelegramLongPollingBot {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommanderBot.class);

    private static final CommanderBot instance = new CommanderBot();

    public static CommanderBot instance() {
        return instance;
    }


    private static String ACCESS_DENIED_FILE_ID = null;

    private CommanderBot() {
    }

    @Override
    public void onUpdateReceived(Update update) {
        LOGGER.info("Received new update {}", update);

        if (update.getMessage().getFrom().getId() != ADMIN_USER_ID) {
            handleAccessDeniedError(update);
            return;
        }

        List<Container> containers = DockerAgent.instance().getContainers();
        containers.forEach(container -> System.out.println(container.getNames()[0]));
    }

    private void handleAccessDeniedError(Update update) {
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
            Message sentMessage = execute(accessDeniedVideo);
            // Обновляем в кэше ID файла видеоответа
            ACCESS_DENIED_FILE_ID = sentMessage.getVideo().getFileId();
        } catch (TelegramApiException e) {
            LOGGER.error("Error while sending response to {}", message);
        }
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
