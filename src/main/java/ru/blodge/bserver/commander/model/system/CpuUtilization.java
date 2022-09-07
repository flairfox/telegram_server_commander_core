package ru.blodge.bserver.commander.model.system;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CpuUtilization(
        @JsonProperty("cpu-load-percent")
        float cpuLoadPercent,
        @JsonProperty("cpu-load-percent-per-core")
        float[] cpuLoadPercentPerCore,
        @JsonProperty("cpu-temperature")
        float cpuTemperature
) {}
