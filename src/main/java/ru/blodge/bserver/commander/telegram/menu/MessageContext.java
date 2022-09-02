package ru.blodge.bserver.commander.telegram.menu;

public record MessageContext(
        long chatId,
        int messageId,
        String[] args
) {}
