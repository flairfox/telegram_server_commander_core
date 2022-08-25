package ru.blodge.bserver.commander.menu.docker;

import com.github.dockerjava.api.model.Container;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.blodge.bserver.commander.docker.DockerAgent;
import ru.blodge.bserver.commander.menu.InlineKeyboardBuilder;
import ru.blodge.bserver.commander.menu.MessageFactory;

import static ru.blodge.bserver.commander.menu.MenuMessageFactory.DOCKER_CONTAINER_MENU_SELECTOR;
import static ru.blodge.bserver.commander.menu.MenuMessageFactory.DOCKER_MENU_SELECTOR;

public class DockerContainersMessageFactory implements MessageFactory {

    @Override
    public EditMessageText buildMenu(String callbackData) {

        EditMessageText mainMenuMessage = new EditMessageText();
        mainMenuMessage.setParseMode("markdown");
        mainMenuMessage.setText("""
                *Docker-контейнеры*
                                
                Вот какие контейнеры я нашел:
                """);

        InlineKeyboardBuilder keyboardBuilder = new InlineKeyboardBuilder();
        for (Container container : DockerAgent.instance().getContainers()) {
            keyboardBuilder
                    .addButton(container.getNames()[0], buildContainerCallbackData(container))
                    .nextRow();
        }

        InlineKeyboardMarkup keyboardMarkup = keyboardBuilder
                .addButton("Назад", DOCKER_MENU_SELECTOR)
                .build();

        mainMenuMessage.setReplyMarkup(keyboardMarkup);

        return mainMenuMessage;
    }

    private String buildContainerCallbackData(Container container) {
        return DOCKER_CONTAINER_MENU_SELECTOR + "." + container.getId().substring(0, 12) + ".d";
    }

}
