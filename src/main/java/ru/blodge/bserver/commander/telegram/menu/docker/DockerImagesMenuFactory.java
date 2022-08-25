package ru.blodge.bserver.commander.telegram.menu.docker;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.blodge.bserver.commander.utils.InlineKeyboardBuilder;
import ru.blodge.bserver.commander.telegram.menu.MessageFactory;

import static ru.blodge.bserver.commander.telegram.menu.MenuFactory.DOCKER_MENU_SELECTOR;

public class DockerImagesMenuFactory implements MessageFactory {

    @Override
    public EditMessageText buildMenu(CallbackQuery callbackQuery) {

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
