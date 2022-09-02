package ru.blodge.bserver.commander.model.docker;

public record DockerContainer(
        String names,
        String id,
        DockerContainerStatus status
) {}
