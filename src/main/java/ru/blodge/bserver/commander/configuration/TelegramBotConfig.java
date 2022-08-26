package ru.blodge.bserver.commander.configuration;

public class TelegramBotConfig {

    public static final long ADMIN_USER_ID = Long.parseLong(System.getenv("ADMIN_USER_ID"));
    public static final String TELEGRAM_BOT_TOKEN = System.getenv("TELEGRAM_BOT_TOKEN");
    public static final String TELEGRAM_BOT_USERNAME = System.getenv("TELEGRAM_BOT_USERNAME");
    public static final String DOCKER_HOST = System.getenv("DOCKER_HOST");

    public static final String ACCESS_DENIED_FILE = "media/access_denied.mp4";

    private TelegramBotConfig() {}

}
