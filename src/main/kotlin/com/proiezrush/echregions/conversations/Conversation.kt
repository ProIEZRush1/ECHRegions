package com.proiezrush.echregions.conversations

import org.bukkit.conversations.ConversationFactory

interface Conversation {

    fun setup(uuid: String, name: String) : ConversationFactory

}