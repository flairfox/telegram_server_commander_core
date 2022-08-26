package ru.blodge.bserver.commander.telegram.menu.docker;

import com.github.dockerjava.api.model.Container;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.blodge.bserver.commander.services.DockerService;
import ru.blodge.bserver.commander.telegram.CommanderBot;
import ru.blodge.bserver.commander.telegram.menu.MessageView;
import ru.blodge.bserver.commander.utils.EditMessageBuilder;
import ru.blodge.bserver.commander.utils.InlineKeyboardBuilder;

import static ru.blodge.bserver.commander.telegram.menu.MenuRouter.DOCKER_CONTAINERS_MENU_SELECTOR;
import static ru.blodge.bserver.commander.telegram.menu.MenuRouter.DOCKER_CONTAINER_MENU_SELECTOR;
import static ru.blodge.bserver.commander.telegram.menu.MenuRouter.DOCKER_MENU_SELECTOR;
import static ru.blodge.bserver.commander.utils.Emoji.BACK_EMOJI;
import static ru.blodge.bserver.commander.utils.Emoji.GREEN_CIRCLE_EMOJI;
import static ru.blodge.bserver.commander.utils.Emoji.RED_CIRCLE_EMOJI;
import static ru.blodge.bserver.commander.utils.Emoji.REFRESH_EMOJI;

public class DockerContainersListView implements MessageView {

    private static final Logger LOGGER = LoggerFactory.getLogger(DockerContainersListView.class);

    @Override
    public void display(CallbackQuery callbackQuery) {

        InlineKeyboardBuilder keyboardBuilder = new InlineKeyboardBuilder();
        for (Container container : DockerService.instance().getContainers()) {
            keyboardBuilder
                    .addButton(buildContainerName(container), buildContainerCallbackData(container))
                    .nextRow();
        }

        InlineKeyboardMarkup keyboardMarkup = keyboardBuilder
                .addButton(REFRESH_EMOJI + " Обновить", DOCKER_CONTAINERS_MENU_SELECTOR)
                .addButton(BACK_EMOJI + " Назад", DOCKER_MENU_SELECTOR)
                .build();

        EditMessageText dockerContainers = new EditMessageBuilder(callbackQuery)
                .withMessageText("""
                        *Docker-контейнеры*
                                        
                        Вот какие контейнеры я нашел:
                        """)
                .withReplyMarkup(keyboardMarkup)
                .build();

        try {
            CommanderBot.instance().execute(dockerContainers);
        } catch (TelegramApiException e) {
            LOGGER.error("Error executing docker containers list menu message", e);
        }
    }

    private String buildContainerName(Container container) {
        return (container.getStatus().startsWith("Up") ? GREEN_CIRCLE_EMOJI : RED_CIRCLE_EMOJI) + " " + container.getNames()[0].substring(1);
    }

    private String buildContainerCallbackData(Container container) {
        return DOCKER_CONTAINER_MENU_SELECTOR + "." + container.getId().substring(0, 12) + ".d";
    }

}
