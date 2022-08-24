package ru.blodge.bserver.commander.menu.docker;

import ru.blodge.bserver.commander.docker.DockerAgent;
import ru.blodge.bserver.commander.menu.MenuEntry;

import java.util.List;

import static ru.blodge.bserver.commander.menu.MenuEntryFactory.DOCKER_MENU_ENTRY_SELECTOR;

public class DockerImagesMenuEntry extends MenuEntry {

    public DockerImagesMenuEntry(String menuEntrySelector) {
        super(menuEntrySelector);
    }

    @Override
    public String getTitleMarkdown() {
        return "Список docker-образов";
    }

    @Override
    public String getBodyMarkdown() {
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
    public String getPreviousMenuEntrySelector() {
        return DOCKER_MENU_ENTRY_SELECTOR;
    }

    @Override
    public List<String> getSubMenuEntriesSelectors() {
        return null;
    }


}
