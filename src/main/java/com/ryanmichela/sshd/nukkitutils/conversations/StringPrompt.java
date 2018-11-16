/*
 * Edited from the Bukkit Repository
 * https://hub.spigotmc.org/stash/users/aikar/repos/bukkit/browse
 */

package com.ryanmichela.sshd.nukkitutils.conversations;

/**
 * StringPrompt is the base class for any prompt that accepts an arbitrary
 * string from the user.
 */
public abstract class StringPrompt implements Prompt {

    /**
     * Ensures that the prompt waits for the user to provide input.
     *
     * @param context Context information about the conversation.
     * @return True.
     */
    public boolean blocksForInput(ConversationContext context) {
        return true;
    }
}
