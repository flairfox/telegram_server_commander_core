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

import static ru.blodge.bserver.commander.telegram.menu.MenuRouter.*;
import static ru.blodge.bserver.commander.utils.Emoji.BACK_EMOJI;

public class ShutdownMenuView implements MessageView {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShutdownMenuView.class);


    @Override
    public void display(MessageContext context) {

        if (context.args().length < 1) {
            displayConfirmation(context);
        } else if (Objects.equals(context.args()[0], "!")) {
            shutdownServer(context);
        }

    }

    private void displayConfirmation(MessageContext context) {

        InlineKeyboardMarkup keyboard = new InlineKeyboardBuilder()
                .addButton("Да", SHUTDOWN_MENU_SELECTOR + ".!")
                .addButton("Отмена", MAIN_MENU_SELECTOR)
                .build();

        EditMessageText rebootConfirmation = TelegramMessageFactory.buildEditMessage(
                context.chatId(),
                context.messageId(),
                """
                        *Завершение работы*
                                                
                        Действительно выключить сервер?
                        """,
                keyboard);

        try {
            CommanderBot.instance().execute(rebootConfirmation);

        } catch (TelegramApiException e) {
            LOGGER.error("Error executing shutdown confirmation menu message.", e);
        }
    }

    private void shutdownServer(MessageContext context) {

        InlineKeyboardMarkup keyboard = new InlineKeyboardBuilder()
                .addButton(BACK_EMOJI + " Назад", MAIN_MENU_SELECTOR)
                .build();

        try {
            EditMessageText mainMenuMessage = TelegramMessageFactory.buildEditMessage(
                    context.chatId(),
                    context.messageId(),
                    """
                            *Сервер будет выключен!*
                            """,
                    keyboard);

            CommanderBot.instance().execute(mainMenuMessage);

            SystemService.instance().shutdown();

        } catch (IOException e) {
            LOGGER.error("Error shutting down system.", e);
        } catch (TelegramApiException e) {
            LOGGER.error("Error executing shut down menu message.", e);
        }

    }
}
