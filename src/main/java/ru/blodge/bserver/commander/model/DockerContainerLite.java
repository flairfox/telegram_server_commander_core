package ru.blodge.bserver.commander.model;

public record DockerContainerLite(
        String names,
        String id,
        DockerContainerStatus status
) {}
