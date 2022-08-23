package ru.blodge.bserver.commander.menu.docker;

import ru.blodge.bserver.commander.docker.DockerAgent;
import ru.blodge.bserver.commander.menu.MenuEntry;

import java.util.List;

import static ru.blodge.bserver.commander.menu.docker.DockerMenuEntry.DOCKER_MENU_SELECTOR;

public class DockerImagesMenuEntry implements MenuEntry {

    public static final String DOCKER_IMAGES_MENU_SELECTOR = "docker-images-menu-selector";


    @Override
    public String getSelector() {
        return DOCKER_IMAGES_MENU_SELECTOR;
    }

    @Override
    public String getTitle() {
        return "Список docker-образов";
    }

    @Override
    public String getDescription() {
        StringBuilder sb = new StringBuilder();
        DockerAgent.instance().getImages().forEach(image -> {
            sb.append(image.getLabels());
            sb.append("\n");
        });

        return sb.toString();
    }

    @Override
    public String getPreviousMenuSelector() {
        return DOCKER_MENU_SELECTOR;
    }

    @Override
    public List<String> getSubMenuSelectors() {
        return null;
    }


}
