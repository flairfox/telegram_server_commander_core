package ru.blodge.bserver.commander.menu.docker;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.blodge.bserver.commander.menu.InlineKeyboardBuilder;
import ru.blodge.bserver.commander.menu.MessageFactory;

import static ru.blodge.bserver.commander.menu.MenuMessageFactory.DOCKER_MENU_SELECTOR;

public class DockerImagesMessageFactory implements MessageFactory {

    @Override
    public EditMessageText buildMenu(String callbackData) {

        EditMessageText mainMenuMessage = new EditMessageText();
        mainMenuMessage.setParseMode("markdown");
        mainMenuMessage.setText("""
                *Docker-образы*
                                
                Вот какие образы я нашел:
                """);

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardBuilder()
                .addButton("Назад", DOCKER_MENU_SELECTOR)
                .build();

        mainMenuMessage.setReplyMarkup(keyboardMarkup);

        return mainMenuMessage;
    }

}
