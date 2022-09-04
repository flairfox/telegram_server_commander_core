package ru.blodge.bserver.commander.configuration;

import java.util.Arrays;
import java.util.List;

public class TelegramBotConfig {

    public static final List<Long> ADMIN_USERS_IDS = Arrays.stream(System.getenv("ADMIN_USERS_IDS").split(","))
            .map(String::trim)
            .map(Long::parseLong)
            .toList();
    public static final String TELEGRAM_BOT_TOKEN = System.getenv("TELEGRAM_BOT_TOKEN");
    public static final String TELEGRAM_BOT_USERNAME = System.getenv("TELEGRAM_BOT_USERNAME");
    public static final String DOCKER_HOST = System.getenv("DOCKER_HOST");
    public static final String SYSTEM_INFO_HOST = System.getenv("SYSTEM_INFO_HOST");

    public static final String ACCESS_DENIED_FILE = "media/access_denied.mp4";

    private TelegramBotConfig() {}

}
