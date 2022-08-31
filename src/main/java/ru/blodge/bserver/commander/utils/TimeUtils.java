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
        if (days != 0) {
            return days + " дн.";
        }

        long hours = duration.toHours();
        if (hours != 0) {
            return hours + " ч.";
        }

        long minutes = duration.toMinutes();
        if (minutes != 0) {
            return minutes + " мин.";
        }

        long seconds = duration.getSeconds();
        return seconds + " сек.";
    }

}
