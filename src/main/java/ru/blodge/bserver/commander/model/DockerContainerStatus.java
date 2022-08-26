package ru.blodge.bserver.commander.model;

public record DockerContainerStatus(
        String statusEmoji,
        String statusCaption,
        String statusDuration
) {}
