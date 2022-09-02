package ru.blodge.bserver.commander.mappers;

import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Ports;
import ru.blodge.bserver.commander.model.docker.DockerContainer;
import ru.blodge.bserver.commander.model.docker.DockerContainerInfo;
import ru.blodge.bserver.commander.model.docker.DockerContainerStatus;

import java.util.*;
import java.util.stream.Collectors;

import static ru.blodge.bserver.commander.utils.Emoji.GREEN_CIRCLE_EMOJI;
import static ru.blodge.bserver.commander.utils.Emoji.RED_CIRCLE_EMOJI;
import static ru.blodge.bserver.commander.utils.TimeUtils.formatDuration;
import static ru.blodge.bserver.commander.utils.TimeUtils.getDuration;

public class DockerContainerMapper {

    public DockerContainer toDockerContainer(Container container) {
        boolean isRunning;
        String statusEmoji;
        String statusCaption;

        if (container.getStatus().startsWith("Up")) {
            isRunning = true;
            statusEmoji = GREEN_CIRCLE_EMOJI;
            statusCaption = "Запущен";
        } else {
            isRunning = false;
            statusEmoji = RED_CIRCLE_EMOJI;
            statusCaption = "Остановлен";
        }

        List<String> names = Arrays.stream(container.getNames())
                .map(name -> name.startsWith("/") ? name.substring(1) : name)
                .toList();

        return new DockerContainer(
                String.join(", ", names),
                container.getId().substring(0, 12),
                new DockerContainerStatus(
                        isRunning,
                        statusEmoji,
                        statusCaption,
                        null
                )
        );
    }

    public DockerContainerInfo toDockerContainerInfo(InspectContainerResponse container) {

        String name = container.getName().startsWith("/") ? container.getName().substring(1) : container.getName();
        Set<String> networks = container.getNetworkSettings().getNetworks().keySet();
        Set<String> portBindings = buildPortBindings(container);
        Set<String> volumes = buildVolumes(container);

        return new DockerContainerInfo(
                name,
                container.getId().substring(0, 12),
                portBindings,
                networks,
                volumes,
                buildContainerStatus(container)
        );
    }

    private Set<String> buildPortBindings(InspectContainerResponse container) {
        Set<String> result = new HashSet<>();

        if (container.getHostConfig().getPortBindings() == null) {
            return result;
        }

        Map<ExposedPort, Ports.Binding[]> bindings = container.getHostConfig().getPortBindings().getBindings();
        for (ExposedPort containerPort : bindings.keySet()) {
            Set<String> hostPorts = Arrays.stream(bindings.get(containerPort))
                    .map(Ports.Binding::getHostPortSpec)
                    .collect(Collectors.toSet());

            String portBinding = String.join(", ", hostPorts) +
                    " -> " +
                    containerPort.getPort() +
                    "/" +
                    containerPort.getProtocol().name();

            result.add(portBinding);
        }

        return result;
    }

    private Set<String> buildVolumes(InspectContainerResponse container) {

        if (container.getConfig().getVolumes() == null) {
            return Set.of();
        }

        return container.getConfig().getVolumes().keySet();
    }

    private DockerContainerStatus buildContainerStatus(InspectContainerResponse container) {
        boolean isRunning;
        String statusEmoji;
        String statusCaption;
        String statusDuration;

        InspectContainerResponse.ContainerState state = container.getState();
        if ("running".equals(state.getStatus())) {
            isRunning = true;
            statusEmoji = GREEN_CIRCLE_EMOJI;
            statusCaption = "Запущен";
            statusDuration = formatDuration(getDuration(state.getStartedAt()));
        } else {
            isRunning = false;
            statusEmoji = RED_CIRCLE_EMOJI;
            statusCaption = "Остановлен";
            statusDuration = formatDuration(getDuration(state.getFinishedAt()));
        }

        return new DockerContainerStatus(
                isRunning,
                statusEmoji,
                statusCaption,
                statusDuration
        );
    }

}
