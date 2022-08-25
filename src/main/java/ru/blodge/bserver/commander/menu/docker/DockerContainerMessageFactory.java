package ru.blodge.bserver.commander.menu.docker;

import com.github.dockerjava.api.command.InspectContainerResponse;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.blodge.bserver.commander.docker.DockerAgent;
import ru.blodge.bserver.commander.menu.InlineKeyboardBuilder;
import ru.blodge.bserver.commander.menu.MessageFactory;

import java.time.Duration;
import java.time.OffsetDateTime;

import static ru.blodge.bserver.commander.menu.MenuMessageFactory.DOCKER_CONTAINERS_MENU_SELECTOR;
import static ru.blodge.bserver.commander.menu.MenuMessageFactory.DOCKER_CONTAINER_MENU_SELECTOR;

public class DockerContainerMessageFactory implements MessageFactory {

    @Override
    public EditMessageText buildMenu(String callbackData) {
        String[] callbackDataArr = callbackData.split("\\.");
        String containerId = callbackDataArr[1];
        String action = callbackDataArr[2];

        InspectContainerResponse containerResponse = DockerAgent.instance().getContainer(containerId);

        switch (action) {
            case "r?" -> {
                return buildContainerRestartConfirmationMenu(containerResponse);
            }
            case "r!" -> {
                DockerAgent.instance().restartContainer(containerId);
                return buildContainerRestartingMenu(containerResponse);
            }
            default -> {
                return buildContainerInfoMenu(containerResponse);
            }
        }

    }

    private EditMessageText buildContainerInfoMenu(InspectContainerResponse containerResponse) {
        EditMessageText mainMenuMessage = new EditMessageText();

        InspectContainerResponse.ContainerState containerState = containerResponse.getState();
        String containerStatus = switch (containerState.getStatus()) {
            case "running" -> "Работает " + formatDuration(getUptime(containerState.getStartedAt()));
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
                .addButton("Обновить", buildContainerCallbackData(containerResponse.getId(), "d"))
                .addButton("Назад", DOCKER_CONTAINERS_MENU_SELECTOR)
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

    private long getUptime(String containerStartStr) {
        OffsetDateTime containerStartedAt = OffsetDateTime.parse(containerStartStr);
        return OffsetDateTime.now().toEpochSecond() - containerStartedAt.toEpochSecond();
    }

    private String formatDuration(long duration) {
        Duration d = Duration.ofSeconds(duration);
        long days = d.toDays();
        d = d.minusDays(days);
        long hours = d.toHours();
        d = d.minusHours(hours);
        long minutes = d.toMinutes();
        d = d.minusMinutes(minutes);
        long seconds = d.getSeconds();
        return
                (days == 0 ? "" : days + " д., ") +
                        (hours == 0 ? "" : hours + " ч., ") +
                        (minutes == 0 ? "" : minutes + " м., ") +
                        (seconds == 0 ? "" : seconds + " с.");
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
