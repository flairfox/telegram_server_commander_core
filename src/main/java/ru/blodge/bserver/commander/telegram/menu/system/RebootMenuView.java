package ru.blodge.bserver.commander.telegram.menu.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.blodge.bserver.commander.services.SystemService;
import ru.blodge.bserver.commander.telegram.CommanderBot;
import ru.blodge.bserver.commander.telegram.menu.MessageContext;
import ru.blodge.bserver.commander.telegram.menu.MessageView;
import ru.blodge.bserver.commander.utils.builders.InlineKeyboardBuilder;
import ru.blodge.bserver.commander.utils.factories.TelegramMessageFactory;

import java.io.IOException;
import java.util.Objects;

import static ru.blodge.bserver.commander.telegram.menu.MenuRouter.MAIN_MENU_SELECTOR;
import static ru.blodge.bserver.commander.telegram.menu.MenuRouter.REBOOT_MENU_SELECTOR;
import static ru.blodge.bserver.commander.utils.Emoji.BACK_EMOJI;

public class RebootMenuView implements MessageView {

    private static final Logger LOGGER = LoggerFactory.getLogger(RebootMenuView.class);

    @Override
    public void display(MessageContext context) {

        if (context.args().length < 1) {
            displayConfirmation(context);
        } else if (Objects.equals(context.args()[0], "!")) {
            rebootServer(context);
        }

    }

    private void displayConfirmation(MessageContext context) {

        InlineKeyboardMarkup keyboard = new InlineKeyboardBuilder()
                .addButton("Да", REBOOT_MENU_SELECTOR + ".!")
                .addButton("Отмена", MAIN_MENU_SELECTOR)
                .build();

        EditMessageText rebootConfirmation = TelegramMessageFactory.buildEditMessage(
                context.chatId(),
                context.messageId(),
                """
                        *Перезагрузка*
                                                
                        Действительно перезагрузить сервер?
                        """,
                keyboard);

        try {
            CommanderBot.instance().execute(rebootConfirmation);

        } catch (TelegramApiException e) {
            LOGGER.error("Error executing reboot confirmation menu message.", e);
        }
    }

    private void rebootServer(MessageContext context) {

        InlineKeyboardMarkup keyboard = new InlineKeyboardBuilder()
                .addButton(BACK_EMOJI + " Назад", MAIN_MENU_SELECTOR)
                .build();

        try {
            EditMessageText mainMenuMessage = TelegramMessageFactory.buildEditMessage(
                    context.chatId(),
                    context.messageId(),
                    """
                            *Сервер будет перезагружен!*
                            """,
                    keyboard);

            CommanderBot.instance().execute(mainMenuMessage);

            SystemService.instance().reboot();

        } catch (IOException e) {
            LOGGER.error("Error rebooting system.", e);
        } catch (TelegramApiException e) {
            LOGGER.error("Error executing reboot menu message.", e);
        }

    }
}
