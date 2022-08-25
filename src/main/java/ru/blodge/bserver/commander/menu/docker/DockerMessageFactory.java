package ru.blodge.bserver.commander.menu.docker;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.blodge.bserver.commander.menu.InlineKeyboardBuilder;
import ru.blodge.bserver.commander.menu.MessageFactory;

import static ru.blodge.bserver.commander.menu.MenuMessageFactory.DOCKER_CONTAINERS_MENU_SELECTOR;
import static ru.blodge.bserver.commander.menu.MenuMessageFactory.DOCKER_IMAGES_MENU_SELECTOR;
import static ru.blodge.bserver.commander.menu.MenuMessageFactory.MAIN_MENU_SELECTOR;

public class DockerMessageFactory implements MessageFactory {

    @Override
    public EditMessageText buildMenu(String callbackData) {

        EditMessageText mainMenuMessage = new EditMessageText();
        mainMenuMessage.setParseMode("markdown");
        mainMenuMessage.setText("""
                *Docker*
                                
                Здесь есть все, что вы хотели знать о Docker
                """);

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardBuilder()
                .addButton("Docker-контейнеры", DOCKER_CONTAINERS_MENU_SELECTOR)
                .nextRow()
                .addButton("Docker-образы", DOCKER_IMAGES_MENU_SELECTOR)
                .nextRow()
                .addButton("Назад", MAIN_MENU_SELECTOR)
                .build();

        mainMenuMessage.setReplyMarkup(keyboardMarkup);

        return mainMenuMessage;
    }

}
