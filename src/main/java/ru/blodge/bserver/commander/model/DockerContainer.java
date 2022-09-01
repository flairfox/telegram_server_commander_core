package ru.blodge.bserver.commander.model;

import java.util.Map;
import java.util.Set;

public record DockerContainer(
        String names,
        String id,
        Map<String, Set<String>> portBindings,
        Set<String> networks,
        DockerContainerStatus status
) {}
