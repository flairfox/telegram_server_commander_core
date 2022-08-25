package ru.blodge.bserver.commander.menu;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import static ru.blodge.bserver.commander.menu.MenuMessageFactory.DOCKER_MENU_SELECTOR;
import static ru.blodge.bserver.commander.menu.MenuMessageFactory.SYSTEM_MENU_SELECTOR;

public class MainMenuMessageFactory implements MessageFactory {

    @Override
    public EditMessageText buildMenu(String callbackData) {

        EditMessageText mainMenuMessage = new EditMessageText();
        mainMenuMessage.setParseMode("markdown");
        mainMenuMessage.setText("""
                *Главное меню*
                                
                Это главное меню Бобрового Сервера, здесь можно узнать о:
                """);

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardBuilder()
                .addButton("Системе", SYSTEM_MENU_SELECTOR)
                .nextRow()
                .addButton("Docker'е", DOCKER_MENU_SELECTOR)
                .build();

        mainMenuMessage.setReplyMarkup(keyboardMarkup);

        return mainMenuMessage;
    }

}
