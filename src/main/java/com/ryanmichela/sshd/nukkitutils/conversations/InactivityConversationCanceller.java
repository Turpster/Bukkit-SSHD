/*
 * Edited from the Bukkit Repository
 * https://hub.spigotmc.org/stash/users/aikar/repos/bukkit/browse
 */

package com.ryanmichela.sshd.nukkitutils.conversations;

import cn.nukkit.plugin.Plugin;
import cn.nukkit.scheduler.TaskHandler;

/**
 * An InactivityConversationCanceller will cancel a {@link Conversation} after
 * a period of inactivity by the user.
 */
public class InactivityConversationCanceller implements ConversationCanceller {
    protected Plugin plugin;
    protected int timeoutSeconds;
    protected Conversation conversation;
    private TaskHandler taskHandler = null;

    /**
     * Creates an InactivityConversationCanceller.
     *
     * @param plugin The owning plugin.
     * @param timeoutSeconds The number of seconds of inactivity to wait.
     */
    public InactivityConversationCanceller(Plugin plugin, int timeoutSeconds) {
        this.plugin = plugin;
        this.timeoutSeconds = timeoutSeconds;
    }

    public void setConversation(Conversation conversation) {
        this.conversation = conversation;
        startTimer();
    }

    public boolean cancelBasedOnInput(ConversationContext context, String input) {
        // Reset the inactivity timer
        stopTimer();
        startTimer();
        return false;
    }

    public ConversationCanceller clone() {
        return new InactivityConversationCanceller(plugin, timeoutSeconds);
    }

    /**
     * Starts an inactivity timer.
     */
    private void startTimer() {
        taskHandler = plugin.getServer().getScheduler().scheduleRepeatingTask(plugin, new Runnable() {
            public void run() {
                if (conversation.getState() == Conversation.ConversationState.UNSTARTED) {
                    startTimer();
                } else if (conversation.getState() == Conversation.ConversationState.STARTED) {
                    cancelling(conversation);
                    conversation.abandon(new ConversationAbandonedEvent(conversation, InactivityConversationCanceller.this));
                }
            }
        }, timeoutSeconds * 20);
    }

    /**
     * Stops the active inactivity timer.
     */
    private void stopTimer() {
        if (taskHandler.getTaskId() != -1) {
            plugin.getServer().getScheduler().cancelTask(taskHandler.getTaskId());
            taskHandler = null;
        }
    }

    /**
     * Subclasses of InactivityConversationCanceller can override this method
     * to take additional actions when the inactivity timer abandons the
     * conversation.
     *
     * @param conversation The conversation being abandoned.
     */
    protected void cancelling(Conversation conversation) {

    }
}
