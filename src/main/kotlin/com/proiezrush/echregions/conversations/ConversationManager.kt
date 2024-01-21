package com.proiezrush.echregions.conversations

import com.proiezrush.echregions.ECHRegions
import com.proiezrush.echregions.conversations.rename_region.RenameRegionConversation

class ConversationManager(plugin: ECHRegions) {

    private val renameRegionConversation: RenameRegionConversation = RenameRegionConversation(plugin)

    fun getRenameRegionConversation(): RenameRegionConversation {
        return renameRegionConversation
    }

}