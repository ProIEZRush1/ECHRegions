package com.proiezrush.echregions.conversations.rename_region

import com.proiezrush.echregions.ECHRegions
import com.proiezrush.echregions.conversations.Conversation
import org.bukkit.conversations.ConversationFactory

class RenameRegionConversation(private val plugin: ECHRegions) : Conversation {

    private val conversationFactory: ConversationFactory = ConversationFactory(plugin)

    override fun setup(uuid: String, name: String) : ConversationFactory {
        conversationFactory.withLocalEcho(false)
        conversationFactory.withFirstPrompt(RenameRegionPrompt(plugin, uuid, name))

        return conversationFactory
    }

}