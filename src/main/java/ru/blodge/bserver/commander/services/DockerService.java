package ru.blodge.bserver.commander.services;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.InspectContainerResponse;
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

public class DockerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DockerService.class);

    private static final DockerService instance = new DockerService();

    public static DockerService instance() {
        return instance;
    }

    private final DockerClient dockerClient;

    private DockerService() {
        DockerClientConfig dockerClientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost(System.getenv("DOCKER_HOST"))
                .build();

        DockerHttpClient dockerHttpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(dockerClientConfig.getDockerHost())
                .build();

        this.dockerClient = DockerClientImpl.getInstance(dockerClientConfig, dockerHttpClient);
    }

    public List<Image> getImages() {
        try {
            return dockerClient.listImagesCmd().exec();
        } catch (Exception e) {
            LOGGER.error("Error while trying to list docker images", e);
            throw e;
        }
    }

    public void restartContainer(String containerId) {
        try {
            dockerClient.restartContainerCmd(containerId).exec();
        } catch (Exception e) {
            LOGGER.error("Error while trying to list docker containers", e);
            throw e;
        }
    }

    public InspectContainerResponse getContainer(String containerId) {
        try {
            return dockerClient.inspectContainerCmd(containerId).exec();
        } catch (Exception e) {
            LOGGER.error("Error while trying to list docker containers", e);
            throw e;
        }
    }

    public List<Container> getContainers() {
        try {
            return dockerClient.listContainersCmd().withShowAll(true).exec();
        } catch (Exception e) {
            LOGGER.error("Error while trying to list docker containers", e);
            throw e;
        }
    }

}
