package ru.blodge.bserver.commander.telegram.menu.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.blodge.bserver.commander.telegram.CommanderBot;
import ru.blodge.bserver.commander.telegram.menu.MessageView;
import ru.blodge.bserver.commander.utils.builders.EditMessageBuilder;
import ru.blodge.bserver.commander.utils.builders.InlineKeyboardBuilder;

import static ru.blodge.bserver.commander.telegram.menu.MenuRouter.MAIN_MENU_SELECTOR;

public class SystemMenuView implements MessageView {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemMenuView.class);

    @Override
    public void display(CallbackQuery callbackQuery) {

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardBuilder()
                .addButton("Назад", MAIN_MENU_SELECTOR)
                .build();

        EditMessageText systemMenuMessage = new EditMessageBuilder(callbackQuery)
                .withMessageText("""
                        *Общая информация о системе*
                                        
                        Здесь пока ничего нет, но очень скоро будет!
                        """)
                .withReplyMarkup(keyboardMarkup)
                .build();

        try {
            CommanderBot.instance().execute(systemMenuMessage);
        } catch (TelegramApiException e) {
            LOGGER.error("Error executing system menu message", e);
        }
    }

}
