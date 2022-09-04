package ru.blodge.bserver.commander.utils;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

public class TextUtils {

    private TextUtils() {}

    public static String asciiProgressBar(float value) {
        StringBuilder result = new StringBuilder();

        int normalizedValue = Math.round(value / 5);
        for (int i = 0; i < 20; i++) {
            if (normalizedValue > i) {
                result.append("█");
            } else {
                result.append("▒");
            }
        }

        return result
                .append(" ")
                .append(value)
                .append("%")
                .toString();
    }

    public static String humanReadableByteCountSI(long bytes) {
        if (-1024 < bytes && bytes < 1024) {
            return bytes + " B";
        }
        CharacterIterator ci = new StringCharacterIterator("kMGTPE");
        while (bytes <= -999_950 || bytes >= 999_950) {
            bytes /= 1024;
            ci.next();
        }
        return String.format("%.1f %cB", bytes / 1024.0, ci.current());
    }

}