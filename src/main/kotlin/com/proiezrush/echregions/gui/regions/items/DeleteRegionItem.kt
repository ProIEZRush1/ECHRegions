package com.proiezrush.echregions.gui.regions.items

import com.proiezrush.echregions.ECHRegions
import com.proiezrush.echregions.config.Config
import com.proiezrush.echregions.database.DatabaseManager
import com.proiezrush.echregions.database.local.LocalDatabaseManager
import com.proiezrush.echregions.utils.MessageUtils
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.scheduler.BukkitRunnable
import xyz.xenondevs.invui.item.builder.SkullBuilder
import xyz.xenondevs.invui.item.impl.SimpleItem

class DeleteRegionItem(private val plugin: ECHRegions, private val uuid: String, private val name: String) : SimpleItem(
    SkullBuilder(SkullBuilder.HeadTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmViNTg4YjIxYTZmOThhZDFmZjRlMDg1YzU1MmRjYjA1MGVmYzljYWI0MjdmNDYwNDhmMThmYzgwMzQ3NWY3In19fQ=="))
        .setDisplayName(MessageUtils.sParseColors("<blue><b>DELETE REGION</b></blue>"))
        .addLoreLines("", MessageUtils.sParseColors("<gray>Click to delete region</gray>"), MessageUtils.sParseColors("<red>This action cannot be undone!</red>"))
) {
    private val localDatabaseManager: LocalDatabaseManager = plugin.getDatabaseImpl().getLocalDatabaseManager()
    private val databaseManager: DatabaseManager = plugin.getDatabaseImpl().getDatabase().getDatabaseManager()
    private val config: Config = plugin.getDConfig()

    override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent) {
        event.isCancelled = true

        // Delete region locally
        localDatabaseManager.deleteRegion(uuid, name)

        // Delete region from database with async task
        val runnable = object : BukkitRunnable() {
            override fun run() {
                databaseManager.deleteRegion(uuid, name)
            }
        }
        runnable.runTaskAsynchronously(plugin)

        // Send message
        val regionDeletedMessage = config.getRegionDeleted(name)
        MessageUtils.sendPlayerMessage(player, regionDeletedMessage)

        // Close menu
        player.closeInventory()
    }
}