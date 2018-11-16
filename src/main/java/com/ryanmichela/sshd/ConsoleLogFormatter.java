package com.ryanmichela.sshd;

/**
 * Copyright 2013 Ryan Michela
 */

import cn.nukkit.utils.TextFormat;
import org.fusesource.jansi.Ansi;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.EnumMap;
import java.util.Map;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class ConsoleLogFormatter extends Formatter {

    private SimpleDateFormat dateFormat;

    public ConsoleLogFormatter() {
        this.dateFormat = new SimpleDateFormat("HH:mm:ss");
    }

    public String format(LogRecord logrecord) {
        try {
            Class.forName("org.bukkit.craftbukkit.command.ColouredConsoleSender");
        } catch (ClassNotFoundException ignored) {
            // MEANS WE'RE ON PAPER/TACO/OTHER SHIT
            colorize(logrecord);
        }
        StringBuilder stringbuilder = new StringBuilder();

        stringbuilder.append(" [");
        stringbuilder.append(this.dateFormat.format(logrecord.getMillis())).append(" ");

        stringbuilder.append(logrecord.getLevel().getName()).append("]: ");
        stringbuilder.append(this.formatMessage(logrecord));
        stringbuilder.append('\n');
        Throwable throwable = logrecord.getThrown();

        if (throwable != null) {
            StringWriter stringwriter = new StringWriter();

            throwable.printStackTrace(new PrintWriter(stringwriter));
            stringbuilder.append(stringwriter.toString());
        }

        return stringbuilder.toString();
    }

    private void colorize(LogRecord logrecord) {
        // ORIGINAL CODE FROM org.bukkit.craftbukkit.command.ColouredConsoleSender
        // EDITED FOR NUKKIT
        final Map<TextFormat, String> replacements = new EnumMap<TextFormat, String>(TextFormat.class);

        replacements
                .put(TextFormat.BLACK, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLACK).boldOff().toString());
        replacements
                .put(TextFormat.DARK_BLUE, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLUE).boldOff().toString());
        replacements.put(TextFormat.DARK_GREEN,
                Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.GREEN).boldOff().toString());
        replacements
                .put(TextFormat.DARK_AQUA, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.CYAN).boldOff().toString());
        replacements
                .put(TextFormat.DARK_RED, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.RED).boldOff().toString());
        replacements.put(TextFormat.DARK_PURPLE,
                Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.MAGENTA).boldOff().toString());
        replacements
                .put(TextFormat.GOLD, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.YELLOW).boldOff().toString());
        replacements.put(TextFormat.GRAY, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.WHITE).boldOff().toString());
        replacements
                .put(TextFormat.DARK_GRAY, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLACK).bold().toString());
        replacements.put(TextFormat.BLUE, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.BLUE).bold().toString());
        replacements.put(TextFormat.GREEN, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.GREEN).bold().toString());
        replacements.put(TextFormat.AQUA, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.CYAN).bold().toString());
        replacements.put(TextFormat.RED, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.RED).bold().toString());
        replacements.put(TextFormat.LIGHT_PURPLE,
                Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.MAGENTA).bold().toString());
        replacements.put(TextFormat.YELLOW, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.YELLOW).bold().toString());
        replacements.put(TextFormat.WHITE, Ansi.ansi().a(Ansi.Attribute.RESET).fg(Ansi.Color.WHITE).bold().toString());
        replacements.put(TextFormat.OBFUSCATED, Ansi.ansi().a(Ansi.Attribute.BLINK_SLOW).toString());
        replacements.put(TextFormat.BOLD, Ansi.ansi().a(Ansi.Attribute.UNDERLINE_DOUBLE).toString());
        replacements.put(TextFormat.STRIKETHROUGH, Ansi.ansi().a(Ansi.Attribute.STRIKETHROUGH_ON).toString());
        replacements.put(TextFormat.UNDERLINE, Ansi.ansi().a(Ansi.Attribute.UNDERLINE).toString());
        replacements.put(TextFormat.ITALIC, Ansi.ansi().a(Ansi.Attribute.ITALIC).toString());
        replacements.put(TextFormat.RESET, Ansi.ansi().a(Ansi.Attribute.RESET).toString());

        String result = logrecord.getMessage();
        for (TextFormat color : TextFormat.values()) {
            if (replacements.containsKey(color)) {
                result = result.replaceAll("(?i)" + color.toString(), replacements.get(color));
            } else {
                result = result.replaceAll("(?i)" + color.toString(), "");
            }
        }
        result += Ansi.ansi().reset().toString();
        logrecord.setMessage(result);
    }
}

