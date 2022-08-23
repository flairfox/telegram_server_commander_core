package ru.blodge.bserver.commander.menu;

import java.util.List;

import static ru.blodge.bserver.commander.menu.docker.DockerMenuEntry.DOCKER_MENU_SELECTOR;
import static ru.blodge.bserver.commander.menu.system.SystemMenuEntry.SYSTEM_MENU_SELECTOR;

public class MainMenuEntry implements MenuEntry {

    public static final String MAIN_MENU_SELECTOR = "main-menu-selector";

    @Override
    public String getSelector() {
        return MAIN_MENU_SELECTOR;
    }

    @Override
    public String getTitle() {
        return "Главное меню";
    }

    @Override
    public String getHtmlBody() {
        return """
                <b>Главное меню</b>
                                
                <pre>Это главное меню Бобрового Сервера, здесь можно узнать о:</pre>
                """;
    }

    @Override
    public String getPreviousMenuSelector() {
        return null;
    }

    @Override
    public List<String> getSubMenuSelectors() {
        return List.of(
                SYSTEM_MENU_SELECTOR,
                DOCKER_MENU_SELECTOR
        );
    }


}
