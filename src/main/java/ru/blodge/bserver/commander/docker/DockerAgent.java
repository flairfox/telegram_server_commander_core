package ru.blodge.bserver.commander.docker;

import com.github.dockerjava.api.DockerClient;
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

public class DockerAgent {

    private static final Logger LOGGER = LoggerFactory.getLogger(DockerAgent.class);

    private static final DockerAgent instance = new DockerAgent();

    public static DockerAgent instance() {
        return instance;
    }

    private final DockerClient dockerClient;

    private DockerAgent() {
        DockerClientConfig dockerClientConfig = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost("tcp://0.0.0.0:2374")
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

    public List<Container> getContainers() {
        try {
            return dockerClient.listContainersCmd().exec();
        } catch (Exception e) {
            LOGGER.error("Error while trying to list docker containers", e);
            throw e;
        }
    }

}
