package ru.blodge.bserver.commander.telegram.menu.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.blodge.bserver.commander.model.system.ResourceUtilizationInfo;
import ru.blodge.bserver.commander.services.SystemService;
import ru.blodge.bserver.commander.telegram.CommanderBot;
import ru.blodge.bserver.commander.telegram.menu.MessageContext;
import ru.blodge.bserver.commander.telegram.menu.MessageView;
import ru.blodge.bserver.commander.utils.builders.InlineKeyboardBuilder;
import ru.blodge.bserver.commander.utils.factories.TelegramMessageFactory;

import java.io.IOException;

import static ru.blodge.bserver.commander.telegram.menu.MenuRouter.MAIN_MENU_SELECTOR;
import static ru.blodge.bserver.commander.telegram.menu.MenuRouter.RESOURCE_UTILIZATION_MENU_SELECTOR;
import static ru.blodge.bserver.commander.utils.Emoji.BACK_EMOJI;
import static ru.blodge.bserver.commander.utils.Emoji.REFRESH_EMOJI;
import static ru.blodge.bserver.commander.utils.Text.asciiProgressBar;

public class ResourceUtilizationView implements MessageView {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemInfoView.class);

    @Override
    public void display(MessageContext context) {

        InlineKeyboardMarkup keyboard = new InlineKeyboardBuilder()
                .addButton(REFRESH_EMOJI + " Обновить", RESOURCE_UTILIZATION_MENU_SELECTOR)
                .addButton(BACK_EMOJI + " Назад", MAIN_MENU_SELECTOR)
                .build();

        try {
            ResourceUtilizationInfo resourceUtilizationInfo = SystemService.instance().getResourceUtilizationInfo();

            StringBuilder perCoreUtilization = new StringBuilder();
            for (int i = 0; i < resourceUtilizationInfo.cpuUtilization().length; i++) {
                perCoreUtilization.append("\tC" + i + " ")
                        .append(asciiProgressBar(resourceUtilizationInfo.cpuUtilization()[i]))
                        .append("\n");
            }

            EditMessageText mainMenuMessage = TelegramMessageFactory.buildEditMessage(
                    context.chatId(),
                    context.messageId(),
                    """
                            *Утилизация CPU, RAM и SWAP*
                                                        
                            Вот так загружена твоя система:
                                                        
                            *CPU:*
                            `%s`
                            *RAM:*
                            `%s`
                                                        
                            *SWAP:*
                            `%s`
                            """.formatted( // todo по аналогии с информацией о дисках вывести общий объем и свободный для RAM и SWAP
                            perCoreUtilization,
                            asciiProgressBar(resourceUtilizationInfo.memoryUtilization()),
                            asciiProgressBar(resourceUtilizationInfo.swapUtilization())),
                    keyboard);

            CommanderBot.instance().execute(mainMenuMessage);

        } catch (IOException e) {
            LOGGER.error("Error retrieving resource utilization info.", e);
        } catch (TelegramApiException e) {
            LOGGER.error("Error executing system menu message.", e);
        }

    }

}
