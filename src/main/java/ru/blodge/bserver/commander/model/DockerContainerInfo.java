package ru.blodge.bserver.commander.model;

import java.util.Set;

public record DockerContainerInfo(
        String names,
        String id,
        Set<String> portBindings,
        Set<String> networks,
        DockerContainerStatus status
) {}
