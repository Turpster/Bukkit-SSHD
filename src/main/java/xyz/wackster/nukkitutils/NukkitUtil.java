package xyz.wackster.nukkitutils;

import cn.nukkit.utils.LogLevel;

import java.util.logging.Level;
import java.util.regex.Pattern;
public class NukkitUtil
{
    private static final char COLOR_CHAR = '\u00A7';
    private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + String.valueOf(COLOR_CHAR) + "[0-9A-FK-OR]");

    /**
     * Strips the given message of all color codes
     *
     * @param input String to strip of color
     * @return A copy of the input string, without any coloring
     */
    public static String stripColor(final String input)
    {
        if (input == null)
        {
            return null;
        }

        return STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
    }

    public static LogLevel toLogLevel(final Level logLevel)
    {
        switch(logLevel.intValue())
        {
            case 1000: // SEVERE
                return LogLevel.EMERGENCY;
            case 900: // WARNING
                return LogLevel.WARNING;
            case 800: // INFO
                return LogLevel.INFO;

            case 500: // FINE
            case 400: // FINER
            case 300: // FINEST
                return LogLevel.NOTICE;
            case Integer.MIN_VALUE: // ALL
            case Integer.MAX_VALUE: // OFF
            case 700: // CONFIG
            default:
                return LogLevel.DEFAULT_LEVEL;
        }
    }
}
