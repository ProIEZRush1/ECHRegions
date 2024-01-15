package com.proiezrush.echregions.listeners

import com.proiezrush.echregions.ECHRegions
import com.proiezrush.echregions.config.Config
import com.proiezrush.echregions.database.DatabaseManager
import com.proiezrush.echregions.database.local.LocalDatabaseManager
import com.proiezrush.echregions.utils.MessageUtils
import io.papermc.paper.event.player.AsyncChatEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.scheduler.BukkitRunnable

class RenameRegionListener(private val plugin: ECHRegions) : Listener {

    private val localDatabaseManager: LocalDatabaseManager = plugin.getDatabaseImpl().getLocalDatabaseManager()
    private val databaseManager: DatabaseManager = plugin.getDatabaseImpl().getDatabase().getDatabaseManager()
    private val config: Config = plugin.getDConfig()

    @EventHandler
    fun onRenameRegion(event: AsyncChatEvent) {
        val player = event.player
        val uuid = player.uniqueId.toString()

        val renamingRegionName = localDatabaseManager.getPlayerRenamingRegion(uuid)
        if (renamingRegionName != null) {
            val newName = MessageUtils.componentToString(event.message())

            // Not renaming anymore
            localDatabaseManager.removePlayerRenamingRegion(uuid)

            // Rename region locally
            localDatabaseManager.renameRegion(uuid, renamingRegionName, newName)

            // Rename region in database with async task
            val runner = object : BukkitRunnable() {
                override fun run() {
                    databaseManager.renameRegion(uuid, renamingRegionName, newName)
                }
            }
            runner.runTaskAsynchronously(plugin)

            // Send message
            val regionRenamedMessage = config.getRegionRenamed(renamingRegionName, newName)
            MessageUtils.sendPlayerMessage(player, regionRenamedMessage)

            event.isCancelled = true
        }
    }

}