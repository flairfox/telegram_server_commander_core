package ru.blodge.bserver.commander.model.system;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MemoryUtilization(
        @JsonProperty("total")
        long total,
        @JsonProperty("used")
        long used,
        @JsonProperty("free")
        long free,
        @JsonProperty("percent")
        float percent
) {}
