/*
package com.ryanmichela.sshd;
*/

/**
 * Copyright 2013 Ryan Michela
 */

/*
import cn.nukkit.Server;
import cn.nukkit.command.CommandMap;
import cn.nukkit.scheduler.TaskHandler;
import cn.nukkit.utils.LogLevel;
import jline.console.completer.Completer;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ConsoleCommandCompleter implements Completer {

    public int complete(final String buffer, final int cursor, final List<CharSequence> candidates) {
        Waitable<List<String>> waitable = new Waitable<List<String>>() {
            @Override
            protected List<String> evaluate() {
                CommandMap commandMap = ReflectionUtil.getProtectedValue(Server.getInstance(), "commandMap");
                return commandMap.tabComplete(Server.getInstance().getConsoleSender(), buffer);
            }
        }.run(Server.getInstance().getTick() + 1); // TODO Remove +1 and check it works afterwards - Turpster
        TaskHandler tHandle = Server.getInstance().getScheduler().scheduleTask(SshdPlugin.instance, waitable);

        try {
            List<String> offers = waitable.get();
            if (offers == null) {
                return cursor;
            }
            candidates.addAll(offers);

            final int lastSpace = buffer.lastIndexOf(' ');
            if (lastSpace == -1) {
                return cursor - buffer.length();
            } else {
                return cursor - (buffer.length() - lastSpace - 1);
            }
        } catch (ExecutionException e) {
            SshdPlugin.instance.getLogger().log(LogLevel.WARNING, "Unhandled exception when tab completing", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return cursor;
    }
}

TODO Get Tab completion to work with Nukkit
*/