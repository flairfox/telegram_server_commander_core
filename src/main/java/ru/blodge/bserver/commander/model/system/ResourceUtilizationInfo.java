package ru.blodge.bserver.commander.model.system;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ResourceUtilizationInfo(
        @JsonProperty("cpu-utilization")
        float cpuUtilization,
        @JsonProperty("memory-utilization")
        float memoryUtilization
) {}
