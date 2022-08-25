package ru.blodge.bserver.commander.telegram.menu.docker;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.blodge.bserver.commander.telegram.menu.MessageFactory;
import ru.blodge.bserver.commander.utils.InlineKeyboardBuilder;

import static ru.blodge.bserver.commander.telegram.menu.MenuFactory.DOCKER_CONTAINERS_MENU_SELECTOR;
import static ru.blodge.bserver.commander.telegram.menu.MenuFactory.DOCKER_IMAGES_MENU_SELECTOR;
import static ru.blodge.bserver.commander.telegram.menu.MenuFactory.MAIN_MENU_SELECTOR;
import static ru.blodge.bserver.commander.utils.Emoji.BACK_EMOJI;

public class DockerMenuFactory implements MessageFactory {

    @Override
    public EditMessageText buildMenu(CallbackQuery callbackQuery) {

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
                .addButton(BACK_EMOJI + "Назад", MAIN_MENU_SELECTOR)
                .build();

        mainMenuMessage.setReplyMarkup(keyboardMarkup);

        return mainMenuMessage;
    }

}
