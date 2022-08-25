package ru.blodge.bserver.commander.utils;

import java.time.Duration;
import java.time.OffsetDateTime;

public class TimeUtils {

    private TimeUtils() {
    }

    public static Duration getDuration(String containerStartStr) {
        OffsetDateTime containerStartedAt = OffsetDateTime.parse(containerStartStr);
        long durationInSeconds = OffsetDateTime.now().toEpochSecond() - containerStartedAt.toEpochSecond();

        return Duration.ofSeconds(durationInSeconds);
    }

    public static String formatDuration(Duration duration) {
        long days = duration.toDays();
        duration = duration.minusDays(days);
        long hours = duration.toHours();
        duration = duration.minusHours(hours);
        long minutes = duration.toMinutes();
        duration = duration.minusMinutes(minutes);
        long seconds = duration.getSeconds();
        return
                (days == 0 ? "" : days + " д., ") +
                        (hours == 0 ? "" : hours + " ч., ") +
                        (minutes == 0 ? "" : minutes + " м., ") +
                        (seconds == 0 ? "" : seconds + " с.");
    }

}
