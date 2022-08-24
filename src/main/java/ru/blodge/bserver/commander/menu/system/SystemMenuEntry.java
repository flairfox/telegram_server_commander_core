package ru.blodge.bserver.commander.menu.system;

import ru.blodge.bserver.commander.menu.MenuEntry;

import java.util.List;

import static ru.blodge.bserver.commander.menu.MenuEntryFactory.MAIN_MENU_ENTRY_SELECTOR;

public class SystemMenuEntry extends MenuEntry {

    public SystemMenuEntry(String menuEntrySelector) {
        super(menuEntrySelector);
    }

    @Override
    public String getTitleMarkdown() {
        return "Система";
    }

    @Override
    public String getBodyMarkdown() {
        return """
                <b>Общая информация о системе</b>
                                
                <pre>Тут пока ничего нет</pre>
                """;
    }

    @Override
    public String getPreviousMenuEntrySelector() {
        return MAIN_MENU_ENTRY_SELECTOR;
    }

    @Override
    public List<String> getSubMenuEntriesSelectors() {
        return null;
    }


}
