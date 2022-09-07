package ru.blodge.bserver.commander.model.system;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ResourceUtilizationInfo(
        @JsonProperty("cpu-utilization")
        CpuUtilization cpuUtilization,
        @JsonProperty("memory-utilization")
        MemoryUtilization memoryUtilization,
        @JsonProperty("swap-utilization")
        MemoryUtilization swapUtilization
) {}
