package com.proiezrush.echregions.gui.regions.items

import com.proiezrush.echregions.ECHRegions
import com.proiezrush.echregions.conversations.ConversationManager
import com.proiezrush.echregions.conversations.rename_region.RenameRegionConversation
import com.proiezrush.echregions.database.local.LocalDatabaseManager
import com.proiezrush.echregions.utils.MessageUtils
import org.bukkit.conversations.Conversation
import org.bukkit.conversations.ConversationFactory
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.builder.SkullBuilder
import xyz.xenondevs.invui.item.impl.SimpleItem

class RenameRegionItem(plugin: ECHRegions, private val name: String) : SimpleItem(
    SkullBuilder(SkullBuilder.HeadTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGM2MTExNGMxZWNiNThhNjVkOTQ5N2M4ZmQ4YmJhNjlmNTk1ZjY3ODc4NTU5YjE2MmY1YmM1ODVmNGZlY2JmMyJ9fX0="))
        .setDisplayName(MessageUtils.sParseColors("<blue><b>RENAME REGION</b></blue>"))
        .addLoreLines("", MessageUtils.sParseColors("<gray>Click to rename region</gray>"))
) {
    private val localDatabaseManager: LocalDatabaseManager = plugin.getDatabaseImpl().getLocalDatabaseManager()
    private val conversationManager: ConversationManager = plugin.getConversationManager()

    override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent) {
        event.isCancelled = true

        // Add player to renaming region
        val playerUUID = player.uniqueId.toString()

        // Start rename conversation
        val renameRegionConversation: RenameRegionConversation = conversationManager.getRenameRegionConversation()
        val conversationFactory: ConversationFactory = renameRegionConversation.setup(playerUUID, name)
        val conversation: Conversation = conversationFactory.buildConversation(player)

        // Clear previous conversation
        localDatabaseManager.getPlayerRenamingRegionConversation(playerUUID)?.abandon()
        localDatabaseManager.removePlayerRenamingRegionConversation(playerUUID)

        // Add conversation to local database
        localDatabaseManager.addPlayerRenamingRegionConversation(playerUUID, conversation)

        conversation.begin()

        player.closeInventory()
    }
}