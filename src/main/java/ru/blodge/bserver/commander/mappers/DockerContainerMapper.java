package ru.blodge.bserver.commander.mappers;

import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Ports;
import ru.blodge.bserver.commander.model.DockerContainer;
import ru.blodge.bserver.commander.model.DockerContainerLite;
import ru.blodge.bserver.commander.model.DockerContainerStatus;

import java.util.*;
import java.util.stream.Collectors;

import static ru.blodge.bserver.commander.utils.Emoji.GREEN_CIRCLE_EMOJI;
import static ru.blodge.bserver.commander.utils.Emoji.RED_CIRCLE_EMOJI;
import static ru.blodge.bserver.commander.utils.TimeUtils.formatDuration;
import static ru.blodge.bserver.commander.utils.TimeUtils.getDuration;

public class DockerContainerMapper {

    public DockerContainerLite toDockerContainer(Container container) {
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

        return new DockerContainerLite(
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

    public DockerContainer toDockerContainer(InspectContainerResponse container) {
        String name = container.getName().startsWith("/") ? container.getName().substring(1) : container.getName();
        Set<String> networks = container.getNetworkSettings().getNetworks().keySet();
        Map<String, Set<String>> portBindings = buildPortBindings(container);


        return new DockerContainer(
                name,
                container.getId().substring(0, 12),
                portBindings,
                networks,
                buildContainerStatus(container)
        );
    }

    private Map<String, Set<String>> buildPortBindings(InspectContainerResponse container) {
        Map<String, Set<String>> result = new HashMap<>();

        if (container.getHostConfig().getPortBindings() == null) {
            return result;
        }

        Map<ExposedPort, Ports.Binding[]> bindings = container.getHostConfig().getPortBindings().getBindings();
        for (ExposedPort exposedPort : bindings.keySet()) {
            Set<String> ports = Arrays.stream(bindings.get(exposedPort))
                    .map(Ports.Binding::getHostPortSpec)
                    .collect(Collectors.toSet());

            result.put(exposedPort.getPort() + "/" + exposedPort.getProtocol().name(), ports);
        }

        return result;
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
