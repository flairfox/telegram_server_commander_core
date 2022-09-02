package ru.blodge.bserver.commander.model.system;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SystemInfo(
        @JsonProperty("platform")
        String platform,
        @JsonProperty("platform-release")
        String platformRelease,
        @JsonProperty("platform-version")
        String platformVersion,
        @JsonProperty("architecture")
        String architecture,
        @JsonProperty("hostname")
        String hostname,
        @JsonProperty("ip-address")
        String ipAddress,
        @JsonProperty("mac-address")
        String macAddress,
        @JsonProperty("processor")
        String processor,
        @JsonProperty("ram")
        String ram
) {}
