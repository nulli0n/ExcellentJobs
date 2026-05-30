package su.nightexpress.excellentjobs.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jspecify.annotations.NullMarked;

import su.nightexpress.nightcore.util.Numbers;

@NullMarked
public class TimeParser {

    private static final Pattern TIME_PATTERN = Pattern.compile("(\\d+)([smhdw])");

    private TimeParser() {
    }

    public static long parseToMillis(String input) {
        if (input.isEmpty()) return 0;

        long totalMillis = 0;
        Matcher matcher = TIME_PATTERN.matcher(input.toLowerCase());

        while (matcher.find()) {
            long value = Numbers.parseLong(matcher.group(1)).orElse(0L);
            String unit = matcher.group(2);

            switch (unit) {
                case "s" -> totalMillis += value * 1000;
                case "m" -> totalMillis += value * 60000;
                case "h" -> totalMillis += value * 3600000;
                case "d" -> totalMillis += value * 86400000;
                case "w" -> totalMillis += value * 604800000;
                default -> {
                    /* Do nothing */
                }
            }
        }

        return totalMillis;
    }

    public static String formatMillis(long millis) {
        if (millis <= 0) {
            return "0s";
        }

        // Use modulo to get the remainder for each time unit
        long seconds = (millis / 1000) % 60;
        long minutes = (millis / (1000 * 60)) % 60;
        long hours = (millis / (1000 * 60 * 60)) % 24;
        long days = (millis / (1000 * 60 * 60 * 24)) % 7;
        // Weeks don't need a modulo since it's largest unit
        long weeks = millis / (1000L * 60 * 60 * 24 * 7);

        StringBuilder builder = new StringBuilder();

        if (weeks > 0) {
            builder.append(weeks).append("w ");
        }

        if (days > 0) {
            builder.append(days).append("d ");
        }

        if (hours > 0) {
            builder.append(hours).append("h ");
        }

        if (minutes > 0) {
            builder.append(minutes).append("m ");
        }

        if (seconds > 0) {
            builder.append(seconds).append("s");
        }

        return builder.toString().trim();
    }
}