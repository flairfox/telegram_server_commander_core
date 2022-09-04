package ru.blodge.bserver.commander.model.system;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DriveInfo(
        @JsonProperty("mount-point")
        String mountPoint,
        @JsonProperty("total")
        long total,
        @JsonProperty("used")
        long used,
        @JsonProperty("free")
        long free,
        @JsonProperty("percent")
        float percent
) {}
