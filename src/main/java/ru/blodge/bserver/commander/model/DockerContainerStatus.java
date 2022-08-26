package ru.blodge.bserver.commander.model;

public record DockerContainerStatus(
        boolean isRunning,
        String statusEmoji,
        String statusCaption,
        String statusDuration
) {}
