package ru.blodge.bserver.commander.services;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.InspectContainerCmd;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.command.ListContainersCmd;
import com.github.dockerjava.api.command.ListImagesCmd;
import com.github.dockerjava.api.command.RestartContainerCmd;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static ru.blodge.bserver.commander.configuration.TelegramBotConfig.DOCKER_HOST;

public class DockerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DockerService.class);

    private static final DockerService instance = new DockerService();

    public static DockerService instance() {
        return instance;
    }

    private final DockerClient dockerClient;

    private DockerService() {
        DockerClientConfig dockerClientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost(DOCKER_HOST)
                .build();

        DockerHttpClient dockerHttpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(dockerClientConfig.getDockerHost())
                .build();

        this.dockerClient = DockerClientImpl.getInstance(dockerClientConfig, dockerHttpClient);
    }

    public List<Image> getImages() {
        try (ListImagesCmd listImagesCmd = dockerClient.listImagesCmd()) {
            return listImagesCmd.exec();
        }
    }

    public void restartContainer(String containerId) throws NotFoundException {
        try (RestartContainerCmd restartContainerCmd = dockerClient.restartContainerCmd(containerId)) {
            restartContainerCmd.exec();
        }
    }

    public InspectContainerResponse getContainer(String containerId) throws NotFoundException {
        try (InspectContainerCmd inspectContainerCmd = dockerClient.inspectContainerCmd(containerId)) {
            return inspectContainerCmd.exec();
        }
    }

    public List<Container> getContainers() {
        try (ListContainersCmd listContainersCmd = dockerClient.listContainersCmd().withShowAll(true)) {
            return listContainersCmd.exec();
        }
    }

}
