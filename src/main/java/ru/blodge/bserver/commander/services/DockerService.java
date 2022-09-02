package ru.blodge.bserver.commander.services;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.exception.NotModifiedException;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.blodge.bserver.commander.mappers.DockerContainerMapper;
import ru.blodge.bserver.commander.model.DockerContainerInfo;
import ru.blodge.bserver.commander.model.DockerContainer;

import java.util.List;

import static ru.blodge.bserver.commander.configuration.TelegramBotConfig.DOCKER_HOST;

public class DockerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DockerService.class);

    private static final DockerService instance = new DockerService();

    public static DockerService instance() {
        return instance;
    }

    private final DockerClient dockerClient;


    private final DockerContainerMapper containerMapper = new DockerContainerMapper();

    private DockerService() {
        DockerClientConfig dockerClientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost(DOCKER_HOST)
                .build();

        DockerHttpClient dockerHttpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(dockerClientConfig.getDockerHost())
                .build();

        this.dockerClient = DockerClientImpl.getInstance(dockerClientConfig, dockerHttpClient);
    }


    public void startContainer(String containerId) throws NotFoundException, NotModifiedException {
        LOGGER.debug("Starting container with ID {}", containerId);
        try (StartContainerCmd startContainerCmd = dockerClient.startContainerCmd(containerId)) {
            startContainerCmd.exec();
        }
    }

    public void stopContainer(String containerId) throws NotFoundException, NotModifiedException {
        LOGGER.debug("Stopping container with ID {}", containerId);
        try (StopContainerCmd stopContainerCmd = dockerClient.stopContainerCmd(containerId)) {
            stopContainerCmd.exec();
        }
    }

    public void restartContainer(String containerId) throws NotFoundException {
        LOGGER.debug("Restarting container with ID {}", containerId);
        try (RestartContainerCmd restartContainerCmd = dockerClient.restartContainerCmd(containerId)) {
            restartContainerCmd.exec();
        }
    }

    public DockerContainerInfo getContainer(String containerId) throws NotFoundException {
        LOGGER.debug("Inspecting container with ID {}", containerId);
        try (InspectContainerCmd inspectContainerCmd = dockerClient.inspectContainerCmd(containerId)) {
            return containerMapper.toDockerContainerInfo(inspectContainerCmd.exec());
        }
    }

    public void getLogs(
            String containerId,
            ResultCallback<Frame> resultCallback,
            int logsPeriod
    ) throws NotFoundException {
        int currentTs = Math.toIntExact(System.currentTimeMillis() / 1000L);
        try (LogContainerCmd logContainerCmd = dockerClient
                .logContainerCmd(containerId)
                .withContainerId(containerId)
                .withStdOut(true)
                .withStdErr(true)
                .withSince(currentTs - logsPeriod)) {

            logContainerCmd.exec(resultCallback);
        }
    }

    public List<DockerContainer> getContainers() {
        LOGGER.debug("Listing all containers");
        try (ListContainersCmd listContainersCmd = dockerClient.listContainersCmd().withShowAll(true)) {
            return listContainersCmd.exec().stream()
                    .map(containerMapper::toDockerContainer)
                    .toList();
        }
    }

}
