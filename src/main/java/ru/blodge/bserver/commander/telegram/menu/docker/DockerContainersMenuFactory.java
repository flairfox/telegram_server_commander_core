package ru.blodge.bserver.commander.telegram.menu.docker;

import com.github.dockerjava.api.model.Container;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.blodge.bserver.commander.services.DockerService;
import ru.blodge.bserver.commander.telegram.menu.MessageFactory;
import ru.blodge.bserver.commander.utils.InlineKeyboardBuilder;

import static ru.blodge.bserver.commander.telegram.menu.MenuFactory.DOCKER_CONTAINERS_MENU_SELECTOR;
import static ru.blodge.bserver.commander.telegram.menu.MenuFactory.DOCKER_CONTAINER_MENU_SELECTOR;
import static ru.blodge.bserver.commander.telegram.menu.MenuFactory.DOCKER_MENU_SELECTOR;

public class DockerContainersMenuFactory implements MessageFactory {

    @Override
    public EditMessageText buildMenu(String callbackData) {

        EditMessageText mainMenuMessage = new EditMessageText();
        mainMenuMessage.setParseMode("markdown");
        mainMenuMessage.setText("""
                *Docker-контейнеры*
                                
                Вот какие контейнеры я нашел:
                """);

        InlineKeyboardBuilder keyboardBuilder = new InlineKeyboardBuilder();
        for (Container container : DockerService.instance().getContainers()) {
            keyboardBuilder
                    .addButton((container.getStatus().startsWith("Up") ? "\uD83D\uDFE2" : "\uD83D\uDD34") + "\t\t\t" + container.getNames()[0], buildContainerCallbackData(container))
                    .nextRow();
        }

        InlineKeyboardMarkup keyboardMarkup = keyboardBuilder
                .addButton("\uD83D\uDD04 Обновить", DOCKER_CONTAINERS_MENU_SELECTOR)
                .addButton("◀️ Назад", DOCKER_MENU_SELECTOR)
                .build();

        mainMenuMessage.setReplyMarkup(keyboardMarkup);

        return mainMenuMessage;
    }

    private String buildContainerCallbackData(Container container) {
        return DOCKER_CONTAINER_MENU_SELECTOR + "." + container.getId().substring(0, 12) + ".d";
    }

}
