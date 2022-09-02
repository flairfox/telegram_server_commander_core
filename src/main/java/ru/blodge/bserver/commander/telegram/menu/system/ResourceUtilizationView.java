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

            EditMessageText mainMenuMessage = TelegramMessageFactory.buildEditMessage(
                    context.chatId(),
                    context.messageId(),
                    """
                            *Использование ресурсов*
                                            
                            *CPU:*\t`%s`
                            *RAM:*\t`%s`
                            """.formatted(
                            displayProgressbar(resourceUtilizationInfo.cpuUtilization()),
                            displayProgressbar(resourceUtilizationInfo.memoryUtilization())),
                    keyboard);

            CommanderBot.instance().execute(mainMenuMessage);

        } catch (IOException e) {
            LOGGER.error("Error retrieving resource utilization info.", e);
        } catch (TelegramApiException e) {
            LOGGER.error("Error executing system menu message.", e);
        }

    }

    private String displayProgressbar(float value) {
        StringBuilder result = new StringBuilder()
                .append("[");

        int normalizedValue = Math.round(value / 5);
        for (int i = 0; i < 20; i++) {
            if (normalizedValue > i) {
                result.append("#");
            } else {
                result.append("-");
            }
        }

        return result.append("] ")
                .append(value)
                .append("%")
                .toString();
    }

}
