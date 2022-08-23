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
    public String getHtmlBody() {
        StringBuilder sb = new StringBuilder();
        DockerAgent.instance().getImages().forEach(image -> {
            sb.append("- ");
            if (image.getRepoTags()[0].startsWith("<none>")) sb.append("---");
            else sb.append(image.getRepoTags()[0]);
            sb.append("\n");
        });

        return """
                <b>Список docker-образов</b>
                                
                <code>%s</code>
                """.formatted(sb);
    }

    @Override
    public boolean allowUpdate() {
        return true;
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
