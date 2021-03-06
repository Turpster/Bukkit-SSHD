/*
 * Edited from the Bukkit Repository
 * https://hub.spigotmc.org/stash/users/aikar/repos/bukkit/browse
 */

package com.ryanmichela.sshd.nukkitutils.conversations;

import cn.nukkit.Player;
import cn.nukkit.plugin.Plugin;

/**
 * PlayerNamePrompt is the base class for any prompt that requires the player
 * to enter another player's name.
 */
public abstract class PlayerNamePrompt extends ValidatingPrompt {
    private Plugin plugin;

    public PlayerNamePrompt(Plugin plugin) {
        super();
        this.plugin = plugin;
    }

    @Override
    protected boolean isInputValid(ConversationContext context, String input) {
        return plugin.getServer().getPlayer(input) != null;
    }

    @Override
    protected Prompt acceptValidatedInput(ConversationContext context, String input) {
        return acceptValidatedInput(context, plugin.getServer().getPlayer(input));
    }

    /**
     * Override this method to perform some action with the user's player name
     * response.
     *
     * @param context Context information about the conversation.
     * @param input The user's player name response.
     * @return The next {@link Prompt} in the prompt graph.
     */
    protected abstract Prompt acceptValidatedInput(ConversationContext context, Player input);
}
