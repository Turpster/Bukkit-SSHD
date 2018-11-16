package com.ryanmichela.sshd.implementations;

import cn.nukkit.utils.LogLevel;
import com.ryanmichela.sshd.SshdPlugin;
import com.ryanmichela.sshd.nukkitutils.conversations.Conversation;
import com.ryanmichela.sshd.nukkitutils.conversations.ConversationAbandonedEvent;
import com.ryanmichela.sshd.nukkitutils.conversations.ManuallyAbandonedConversationCanceller;

import java.util.LinkedList;

public class SSHDConversationTracker {
    private LinkedList<Conversation> conversationQueue = new LinkedList<Conversation>();

    synchronized boolean beginConversation(Conversation conversation) {
        if (!this.conversationQueue.contains(conversation)) {
            this.conversationQueue.addLast(conversation);
            if (this.conversationQueue.getFirst() == conversation) {
                conversation.begin();
                conversation.outputNextPrompt();
                return true;
            }
        }

        return true;
    }

    synchronized void abandonConversation(Conversation conversation, ConversationAbandonedEvent details) {
        if (!this.conversationQueue.isEmpty()) {
            if (this.conversationQueue.getFirst() == conversation) {
                conversation.abandon(details);
            }

            if (this.conversationQueue.contains(conversation)) {
                this.conversationQueue.remove(conversation);
            }

            if (!this.conversationQueue.isEmpty()) {
                this.conversationQueue.getFirst().outputNextPrompt();
            }
        }

    }

    public synchronized void abandonAllConversations() {
        LinkedList<Conversation> oldQueue = this.conversationQueue;
        this.conversationQueue = new LinkedList<>();

        for (Conversation conversation : oldQueue) {
            try {
                conversation.abandon(new ConversationAbandonedEvent(conversation, new ManuallyAbandonedConversationCanceller()));
            } catch (Throwable var5) {
                SshdPlugin.instance.getLogger().log(LogLevel.EMERGENCY, "Unexpected exception while abandoning a conversation", var5);
            }
        }

    }

    synchronized void acceptConversationInput(String input) {
        if (this.isConversing()) {
            Conversation conversation = this.conversationQueue.getFirst();

            try {
                conversation.acceptInput(input);
            } catch (Throwable var4) {
                SshdPlugin.instance.getLogger().log(LogLevel.WARNING, String.format("Plugin %s generated an exception whilst handling conversation input", conversation.getContext().getPlugin().getDescription().getFullName()), var4);
            }
        }

    }

    synchronized boolean isConversing() {
        return !this.conversationQueue.isEmpty();
    }
}
