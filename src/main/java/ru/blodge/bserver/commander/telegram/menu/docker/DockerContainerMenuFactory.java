package ru.blodge.bserver.commander.telegram.menu.docker;

import com.github.dockerjava.api.command.InspectContainerResponse;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.blodge.bserver.commander.services.DockerService;
import ru.blodge.bserver.commander.telegram.menu.MessageFactory;
import ru.blodge.bserver.commander.utils.InlineKeyboardBuilder;

import static ru.blodge.bserver.commander.telegram.menu.MenuFactory.DOCKER_CONTAINERS_MENU_SELECTOR;
import static ru.blodge.bserver.commander.telegram.menu.MenuFactory.DOCKER_CONTAINER_MENU_SELECTOR;
import static ru.blodge.bserver.commander.utils.TimeUtils.formatDuration;
import static ru.blodge.bserver.commander.utils.TimeUtils.getDuration;

public class DockerContainerMenuFactory implements MessageFactory {

    @Override
    public EditMessageText buildMenu(String callbackData) {
        String[] callbackDataArr = callbackData.split("\\.");
        String containerId = callbackDataArr[1];
        String action = callbackDataArr[2];

        InspectContainerResponse containerResponse = DockerService.instance().getContainer(containerId);

        return switch (action) {
            case "r?" -> buildContainerRestartConfirmationMenu(containerResponse);
            case "r!" -> {
                DockerService.instance().restartContainer(containerId);
                yield buildContainerRestartingMenu(containerResponse);
            }
            default -> buildContainerInfoMenu(containerResponse);
        };

    }

    private EditMessageText buildContainerInfoMenu(InspectContainerResponse containerResponse) {
        EditMessageText mainMenuMessage = new EditMessageText();

        InspectContainerResponse.ContainerState containerState = containerResponse.getState();
        String containerStatus = switch (containerState.getStatus()) {
            case "running" -> "Работает " + formatDuration(getDuration(containerState.getStartedAt()));
            default -> "Неизвестно";
        };

        mainMenuMessage.setParseMode("markdown");
        mainMenuMessage.setText("""
                *Docker-контейнер %s*
                                
                Состояние: %s
                """.formatted(
                containerResponse.getName(),
                containerStatus
        ));

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardBuilder()
                .addButton("Перезапустить", buildContainerCallbackData(containerResponse.getId(), "r?"))
                .nextRow()
                .addButton("\uD83D\uDD04 Обновить", buildContainerCallbackData(containerResponse.getId(), "d"))
                .addButton("◀️ Назад", DOCKER_CONTAINERS_MENU_SELECTOR)
                .build();

        mainMenuMessage.setReplyMarkup(keyboardMarkup);

        return mainMenuMessage;
    }

    private EditMessageText buildContainerRestartConfirmationMenu(InspectContainerResponse containerResponse) {
        EditMessageText mainMenuMessage = new EditMessageText();
        mainMenuMessage.setParseMode("markdown");
        mainMenuMessage.setText("""
                *Docker-контейнер %s будет перезапущен!*
                                
                Вы действительно хотите продолжить?
                """.formatted(
                containerResponse.getName()
        ));

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardBuilder()
                .addButton("Да", buildContainerCallbackData(containerResponse.getId(), "r!"))
                .addButton("Отмена", buildContainerCallbackData(containerResponse.getId(), "d"))
                .build();

        mainMenuMessage.setReplyMarkup(keyboardMarkup);

        return mainMenuMessage;
    }

    private EditMessageText buildContainerRestartingMenu(InspectContainerResponse containerResponse) {
        EditMessageText mainMenuMessage = new EditMessageText();
        mainMenuMessage.setParseMode("markdown");
        mainMenuMessage.setText("""
                *Docker-контейнер %s перезапускается!*
                """.formatted(
                containerResponse.getName()
        ));

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardBuilder()
                .addButton("Назад", buildContainerCallbackData(containerResponse.getId(), "d"))
                .build();

        mainMenuMessage.setReplyMarkup(keyboardMarkup);

        return mainMenuMessage;
    }

    private String buildContainerCallbackData(String containerId, String action) {
        StringBuilder sb = new StringBuilder();
        sb.append(DOCKER_CONTAINER_MENU_SELECTOR)
                .append(".").append(containerId, 0, 12);
        if (action != null) {
            sb.append(".").append(action);
        }

        return sb.toString();
    }

}