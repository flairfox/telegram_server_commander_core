package ru.blodge.bserver.commander.telegram.menu;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.blodge.bserver.commander.utils.InlineKeyboardBuilder;

import static ru.blodge.bserver.commander.telegram.menu.MenuFactory.DOCKER_MENU_SELECTOR;
import static ru.blodge.bserver.commander.telegram.menu.MenuFactory.SYSTEM_MENU_SELECTOR;

public class MainMenuFactory implements MessageFactory {

    @Override
    public EditMessageText buildMenu(CallbackQuery callbackQuery) {

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
