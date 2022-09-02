package ru.blodge.bserver.commander.model;

public record DockerContainer(
        String names,
        String id,
        DockerContainerStatus status
) {}
