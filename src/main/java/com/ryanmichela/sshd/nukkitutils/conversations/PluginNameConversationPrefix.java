/*
 * Edited from the Bukkit Repository
 * https://hub.spigotmc.org/stash/users/aikar/repos/bukkit/browse
 */

package com.ryanmichela.sshd.nukkitutils.conversations;

import cn.nukkit.plugin.Plugin;
import cn.nukkit.utils.TextFormat;

/**
 * PluginNameConversationPrefix is a {@link ConversationPrefix} implementation
 * that displays the plugin name in front of conversation output.
 */
public class PluginNameConversationPrefix implements ConversationPrefix {
    
    protected String separator;
    protected TextFormat prefixColor;
    protected Plugin plugin;
    
    private String cachedPrefix;
    
    public PluginNameConversationPrefix(Plugin plugin) {
        this(plugin, " > ", TextFormat.LIGHT_PURPLE);
    }
    
    public PluginNameConversationPrefix(Plugin plugin, String separator, TextFormat prefixColor) {
        this.separator = separator;
        this.prefixColor = prefixColor;
        this.plugin = plugin;

        cachedPrefix = prefixColor + plugin.getDescription().getName() + separator + TextFormat.WHITE;
    }

    /**
     * Prepends each conversation message with the plugin name.
     *
     * @param context Context information about the conversation.
     * @return An empty string.
     */
    public String getPrefix(ConversationContext context) {
        return cachedPrefix;
    }
}
