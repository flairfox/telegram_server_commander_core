package ru.blodge.bserver.commander.model.docker;

public record DockerContainerStatus(
        boolean isRunning,
        String statusEmoji,
        String statusCaption,
        String statusDuration
) {}
