package ru.blodge.bserver.commander.menu.docker;

import com.github.dockerjava.api.command.InspectContainerResponse;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.blodge.bserver.commander.docker.DockerAgent;
import ru.blodge.bserver.commander.menu.InlineKeyboardBuilder;
import ru.blodge.bserver.commander.menu.MessageFactory;

import static ru.blodge.bserver.commander.menu.MenuMessageFactory.DOCKER_CONTAINERS_MENU_SELECTOR;
import static ru.blodge.bserver.commander.menu.MenuMessageFactory.DOCKER_CONTAINER_MENU_SELECTOR;

public class DockerContainerMessageFactory implements MessageFactory {

    @Override
    public EditMessageText buildMenu(String callbackData) {
        String[] callbackDataArr = callbackData.split("\\.");
        String containerId = callbackDataArr[1];

        if (callbackDataArr.length > 2) {
            String action = callbackDataArr[2];
            switch (action) {
                case "r" -> DockerAgent.instance().restartContainer(containerId);
            }
        }


        InspectContainerResponse containerResponse = DockerAgent.instance().getContainer(containerId);

        EditMessageText mainMenuMessage = new EditMessageText();
        mainMenuMessage.setParseMode("markdown");
        mainMenuMessage.setText("""
                *Docker-контейнер %s*
                """.formatted(
                containerResponse.getName()
        ));

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardBuilder()
                .addButton("Перезапустить", buildContainerCallbackData(containerId, "r"))
                .nextRow()
                .addButton("Обновить", buildContainerCallbackData(containerId, null))
                .addButton("Назад", DOCKER_CONTAINERS_MENU_SELECTOR)
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
