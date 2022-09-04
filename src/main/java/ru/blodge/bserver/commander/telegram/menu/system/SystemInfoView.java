package ru.blodge.bserver.commander.telegram.menu.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.blodge.bserver.commander.model.system.SystemInfo;
import ru.blodge.bserver.commander.services.SystemService;
import ru.blodge.bserver.commander.telegram.CommanderBot;
import ru.blodge.bserver.commander.telegram.menu.MessageContext;
import ru.blodge.bserver.commander.telegram.menu.MessageView;
import ru.blodge.bserver.commander.utils.builders.InlineKeyboardBuilder;
import ru.blodge.bserver.commander.utils.factories.TelegramMessageFactory;

import java.io.IOException;

import static ru.blodge.bserver.commander.telegram.menu.MenuRouter.MAIN_MENU_SELECTOR;
import static ru.blodge.bserver.commander.telegram.menu.MenuRouter.SYSTEM_INFO_MENU_SELECTOR;
import static ru.blodge.bserver.commander.utils.Emoji.BACK_EMOJI;
import static ru.blodge.bserver.commander.utils.Emoji.REFRESH_EMOJI;

@Deprecated
public class SystemInfoView implements MessageView {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemInfoView.class);

    @Override
    public void display(MessageContext context) {

        InlineKeyboardMarkup keyboard = new InlineKeyboardBuilder()
                .addButton(REFRESH_EMOJI + " Обновить", SYSTEM_INFO_MENU_SELECTOR)
                .addButton(BACK_EMOJI + " Назад", MAIN_MENU_SELECTOR)
                .build();

        try {
            SystemInfo systemInfo = SystemService.instance().getSystemInfo();

            EditMessageText mainMenuMessage = TelegramMessageFactory.buildEditMessage(
                    context.chatId(),
                    context.messageId(),
                    """
                            *Информация о системе*
                                            
                            *Платформа:*\t`%s`
                            *Релиз:*\t`%s`
                            *Версия:*\t`%s`
                            *Архитектура:*\t`%s`
                            *Имя хоста:*\t`%s`
                            *IP адрес:*\t`%s`
                            *MAC адрес:*\t`%s`
                            *Процессор:*\t`%s`
                            *Память:*\t`%s`
                            """.formatted(
                            systemInfo.platform(),
                            systemInfo.platformRelease(),
                            systemInfo.platformVersion(),
                            systemInfo.architecture(),
                            systemInfo.hostname(),
                            systemInfo.ipAddress(),
                            systemInfo.macAddress(),
                            systemInfo.processor(),
                            systemInfo.ram()),
                    keyboard);

            CommanderBot.instance().execute(mainMenuMessage);

        } catch (IOException e) {
            LOGGER.error("Error retrieving system info.", e);
        } catch (TelegramApiException e) {
            LOGGER.error("Error executing system menu message.", e);
        }

    }

}
