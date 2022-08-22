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

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

public class CommanderBot extends TelegramLongPollingBot {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommanderBot.class);
    private static final long ADMIN_USER_ID = Long.parseLong(System.getenv("ADMIN_USER_ID"));
    private static final String ACCESS_DENIED_FILE = "media/access_denied.mp4";

    private static String ACCESS_DENIED_FILE_ID = null;

    @Override
    public void onUpdateReceived(Update update) {
        LOGGER.info("Received new update {}", update);

        long userId = update.getMessage().getFrom().getId();
        if (userId != ADMIN_USER_ID) {
            handleAccessDeniedError(update);
        }

        List<Container> containers = DockerApi.instance.getContainers();
        containers.forEach(container -> System.out.println(container.getNames()[0]));
    }

    private void handleAccessDeniedError(Update update) {
        LOGGER.warn("User with ID {} is not an administrator", update.getMessage().getFrom().getId());

        try {
            SendVideo accessDeniedVideo = new SendVideo();
            InputFile inputFile = new InputFile();
            if (ACCESS_DENIED_FILE_ID == null) {
                inputFile.setMedia(loadAccessDeniedFile());
            } else {
                inputFile.setMedia(ACCESS_DENIED_FILE_ID);
            }
            accessDeniedVideo.setChatId(update.getMessage().getChatId());
            accessDeniedVideo.setVideo(inputFile);

            Message sentMessage = execute(accessDeniedVideo);
            ACCESS_DENIED_FILE_ID = sentMessage.getVideo().getFileId();
        } catch (FileNotFoundException | URISyntaxException e) {
            LOGGER.error("Error while retrieving {} file", ACCESS_DENIED_FILE);
        } catch (TelegramApiException e) {
            LOGGER.error("Error while sending response to {}", update.getMessage());
        }
    }

    private File loadAccessDeniedFile() throws FileNotFoundException, URISyntaxException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(ACCESS_DENIED_FILE);
        if (resource == null) {
            throw new FileNotFoundException();
        }

        return new File(resource.toURI());
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
