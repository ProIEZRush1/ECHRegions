package com.proiezrush.echregions.gui.regions.items

import com.proiezrush.echregions.ECHRegions
import com.proiezrush.echregions.config.Config
import com.proiezrush.echregions.database.local.LocalDatabaseManager
import com.proiezrush.echregions.utils.MessageUtils
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.builder.SkullBuilder
import xyz.xenondevs.invui.item.impl.SimpleItem

class RedefineLocationItem(private val plugin: ECHRegions, private val name: String) : SimpleItem(
    SkullBuilder(SkullBuilder.HeadTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjRiYTQ5Mzg0ZGJhN2I3YWNkYjRmNzBlOTM2MWU2ZDU3Y2JiY2JmNzIwY2Y0ZjE2YzJiYjgzZTQ1NTcifX19"))
        .setDisplayName(MessageUtils.sParseColors("<blue><b>REDEFINE LOCATION</b></blue>"))
        .addLoreLines("", MessageUtils.sParseColors("<gray>Click to redefine the location</gray>"))
) {
    private val localDatabaseManager: LocalDatabaseManager = plugin.getDatabaseImpl().getLocalDatabaseManager()
    private val config: Config = plugin.getDConfig()

    override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent) {
        event.isCancelled = true

        // Add player to renaming region
        val playerUUID = player.uniqueId.toString()
        if (localDatabaseManager.getPlayerRedefiningRegion(playerUUID) != null) {
            localDatabaseManager.removePlayerRedefiningRegion(playerUUID)
        }

        localDatabaseManager.addPlayerRedefiningRegion(playerUUID, name)

        val redefiningRegionMessage = config.getRedefiningRegion(name)
        MessageUtils.sendPlayerMessage(player, redefiningRegionMessage)

        player.closeInventory()
    }
}