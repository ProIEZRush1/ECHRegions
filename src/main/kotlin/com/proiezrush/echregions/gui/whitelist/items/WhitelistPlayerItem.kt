package com.proiezrush.echregions.gui.whitelist.items

import com.proiezrush.echregions.ECHRegions
import com.proiezrush.echregions.config.Config
import com.proiezrush.echregions.database.DatabaseManager
import com.proiezrush.echregions.database.local.LocalDatabaseManager
import com.proiezrush.echregions.gui.whitelist.WhitelistPlayerAction
import com.proiezrush.echregions.utils.MessageUtils
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.scheduler.BukkitRunnable
import xyz.xenondevs.invui.item.builder.SkullBuilder
import xyz.xenondevs.invui.item.impl.SimpleItem
import java.util.UUID

class WhitelistPlayerItem(private val plugin: ECHRegions, private val uuid: String, private val name: String, private val playerUUID: String, private val action: WhitelistPlayerAction) : SimpleItem(
    SkullBuilder(UUID.fromString(playerUUID))
        .setDisplayName(MessageUtils.sParseColors("<blue><b>${Bukkit.getOfflinePlayer(UUID.fromString(playerUUID)).name}</b></blue>"))
        .addLoreLines("", MessageUtils.sParseColors("<gray>Click to ${if (action == WhitelistPlayerAction.ADD) "add user to" else "remove user from"} whitelist</gray>"))
) {
    private val localDatabaseManager: LocalDatabaseManager = plugin.getDatabaseImpl().getLocalDatabaseManager()
    private val databaseManager: DatabaseManager = plugin.getDatabaseImpl().getDatabase().getDatabaseManager()
    private val config: Config = plugin.getDConfig()

    override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent) {
        event.isCancelled = true

        if (action == WhitelistPlayerAction.ADD) {
            // Add player to whitelist
            localDatabaseManager.addWhitelistedPlayer(playerUUID, uuid, name)

            // Add player to database
            val runnable = object : BukkitRunnable() {
                override fun run() {
                    databaseManager.addWhitelistedPlayer(playerUUID, uuid, name)
                }
            }
            runnable.runTaskAsynchronously(plugin)

            // Send message
            val playerWhitelistedMessage = config.getPlayerWhitelisted(name)
            MessageUtils.sendPlayerMessage(player, playerWhitelistedMessage)
        }

        if (action == WhitelistPlayerAction.REMOVE) {
            // Remove player from whitelist
            localDatabaseManager.removeWhitelistedPlayer(playerUUID, uuid, name)

            // Remove player from database
            val runnable = object : BukkitRunnable() {
                override fun run() {
                    databaseManager.removeWhitelistedPlayer(playerUUID, uuid, name)
                }
            }
            runnable.runTaskAsynchronously(plugin)

            // Send message
            val playerRemovedFromWhitelistMessage = config.getPlayerRemovedFromWhitelist(name)
            MessageUtils.sendPlayerMessage(player, playerRemovedFromWhitelistMessage)
        }

        player.closeInventory()
    }
}