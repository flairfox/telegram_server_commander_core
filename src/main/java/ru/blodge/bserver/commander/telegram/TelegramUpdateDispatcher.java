package ru.blodge.bserver.commander.telegram;

import com.github.dockerjava.api.model.Container;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.blodge.bserver.commander.docker.DockerAgent;
import ru.blodge.bserver.commander.telegram.handlers.AccessDeniedErrorHandler;

import java.util.List;

import static ru.blodge.bserver.commander.telegram.TelegramBotConfig.ADMIN_USER_ID;

public class TelegramUpdateDispatcher {

    private final AccessDeniedErrorHandler accessDeniedErrorHandler = new AccessDeniedErrorHandler();

    public void dispatch(Update update) {
        // Сообщение пришло НЕ от администратора
        if (update.getMessage().getFrom().getId() != ADMIN_USER_ID) {
            accessDeniedErrorHandler.handle(update);
            return;
        }

        List<Container> containers = DockerAgent.instance().getContainers();
        containers.forEach(container -> System.out.println(container.getNames()[0]));
    }

}
