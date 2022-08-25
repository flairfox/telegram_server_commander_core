package ru.blodge.bserver.commander.menu.system;

import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.blodge.bserver.commander.menu.InlineKeyboardBuilder;
import ru.blodge.bserver.commander.menu.MessageFactory;

import static ru.blodge.bserver.commander.menu.MenuMessageFactory.MAIN_MENU_SELECTOR;

public class SystemMessageFactory implements MessageFactory {

    @Override
    public EditMessageText buildMenu(String callbackData) {

        EditMessageText systemMenuMessage = new EditMessageText();
        systemMenuMessage.setParseMode("markdown");
        systemMenuMessage.setText("""
                *Общая информация о системе*
                                
                Здесь пока ничего нет, но очень скоро будет!
                """);

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardBuilder()
                .addButton("Назад", MAIN_MENU_SELECTOR)
                .build();

        systemMenuMessage.setReplyMarkup(keyboardMarkup);

        return systemMenuMessage;
    }

}
