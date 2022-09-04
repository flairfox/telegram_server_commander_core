package ru.blodge.bserver.commander.telegram.menu.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.blodge.bserver.commander.model.system.DriveInfo;
import ru.blodge.bserver.commander.services.SystemService;
import ru.blodge.bserver.commander.telegram.CommanderBot;
import ru.blodge.bserver.commander.telegram.menu.MessageContext;
import ru.blodge.bserver.commander.telegram.menu.MessageView;
import ru.blodge.bserver.commander.utils.builders.InlineKeyboardBuilder;
import ru.blodge.bserver.commander.utils.factories.TelegramMessageFactory;

import java.io.IOException;
import java.util.List;

import static ru.blodge.bserver.commander.telegram.menu.MenuRouter.DRIVES_INFO_MENU_SELECTOR;
import static ru.blodge.bserver.commander.telegram.menu.MenuRouter.MAIN_MENU_SELECTOR;
import static ru.blodge.bserver.commander.utils.Emoji.BACK_EMOJI;
import static ru.blodge.bserver.commander.utils.Emoji.REFRESH_EMOJI;
import static ru.blodge.bserver.commander.utils.Text.asciiProgressBar;
import static ru.blodge.bserver.commander.utils.Text.humanReadableByteCountSI;

public class DrivesInfoView implements MessageView {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemInfoView.class);

    @Override
    public void display(MessageContext context) {

        InlineKeyboardMarkup keyboard = new InlineKeyboardBuilder()
                .addButton(REFRESH_EMOJI + " Обновить", DRIVES_INFO_MENU_SELECTOR)
                .addButton(BACK_EMOJI + " Назад", MAIN_MENU_SELECTOR)
                .build();

        try {
            List<DriveInfo> drivesInfo = SystemService.instance().getDrivesInfo();

            StringBuilder drivesInformation = new StringBuilder();
            for (DriveInfo driveInfo : drivesInfo) {
                drivesInformation.append("*").append(driveInfo.mountPoint()).append("*")
                        .append("\n")
                        .append("`").append(asciiProgressBar(driveInfo.percent())).append("`")
                        .append("\n").append(humanReadableByteCountSI(driveInfo.free())).append(" свободно из ").append(humanReadableByteCountSI(driveInfo.total()))
                        .append("\n");
            }

            EditMessageText mainMenuMessage = TelegramMessageFactory.buildEditMessage(
                    context.chatId(),
                    context.messageId(),
                    """
                            *Информация о дисках*
                                                        
                            %s
                            """.formatted(drivesInformation),
                    keyboard);

            CommanderBot.instance().execute(mainMenuMessage);

        } catch (IOException e) {
            LOGGER.error("Error retrieving resource utilization info.", e);
        } catch (TelegramApiException e) {
            LOGGER.error("Error executing system menu message.", e);
        }

    }


}
