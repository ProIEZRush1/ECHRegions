package com.proiezrush.echregions.listeners

import com.proiezrush.echregions.ECHRegions
import com.proiezrush.echregions.config.Config
import com.proiezrush.echregions.database.DatabaseManager
import com.proiezrush.echregions.database.local.LocalDatabaseManager
import com.proiezrush.echregions.gui.items.WandItem
import com.proiezrush.echregions.objects.Position
import com.proiezrush.echregions.utils.MessageUtils
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.scheduler.BukkitRunnable

class WandListener(private val plugin: ECHRegions) : Listener {

    private val localDatabaseManager: LocalDatabaseManager = plugin.getDatabaseImpl().getLocalDatabaseManager()
    private val databaseManager: DatabaseManager = plugin.getDatabaseImpl().getDatabase().getDatabaseManager()
    private val config: Config = plugin.getDConfig()

    @EventHandler
    fun onWand(event: PlayerInteractEvent) {
        val item = event.item
        val wandItem = WandItem()
        if (item != wandItem.itemProvider.get()) {
            return
        }

        val player = event.player
        val uuid = player.uniqueId.toString()

        val pos = event.clickedBlock?.location
        val world = event.clickedBlock?.world?.name

        // Common function to set position
        fun setPosition(posNumber: Int, position: Position) {
            localDatabaseManager.addPlayerPosition(uuid, position)

            val positionSetCorrectlyMessage = config.getPositionSetCorrectly(if (posNumber == 1) "first" else "second")
            MessageUtils.sendPlayerMessage(player, positionSetCorrectlyMessage)

            val redefiningRegionName = localDatabaseManager.getPlayerRedefiningRegion(uuid)
            if (redefiningRegionName != null) {
                val p1 = localDatabaseManager.getPlayerFirstPosition(uuid)
                val p2 = localDatabaseManager.getPlayerSecondPosition(uuid)
                if (p1 == null || p2 == null) {
                    return
                }

                redefineRegion(player, redefiningRegionName, p1, p2)
            }
        }

        when (event.action) {
            Action.RIGHT_CLICK_BLOCK -> {
                val p1 = Position(1, world!!, pos!!.x, pos.y, pos.z)
                val p2 = localDatabaseManager.getPlayerSecondPosition(uuid)
                if (p2?.world != world && p2 != null) {
                    MessageUtils.sendPlayerMessage(player, config.getPositionsNeedToBeSameWorld())
                    return
                }
                setPosition(1, p1)
            }
            Action.LEFT_CLICK_BLOCK -> {
                val p2 = Position(2, world!!, pos!!.x, pos.y, pos.z)
                val p1 = localDatabaseManager.getPlayerFirstPosition(uuid)
                if (p1?.world != world && p1 != null) {
                    MessageUtils.sendPlayerMessage(player, config.getPositionsNeedToBeSameWorld())
                    return
                }
                setPosition(2, p2)
            }
            else -> {}
        }

        event.isCancelled = true
    }

    private fun redefineRegion(player: Player, name: String, p1: Position, p2: Position) {
        val uuid = player.uniqueId.toString()

        localDatabaseManager.removePlayerRedefiningRegion(uuid)

        // Update region locally
        localDatabaseManager.redefineRegionPositions(uuid, name, p1, p2)

        // Update region in database with async task
        val runner = object : BukkitRunnable() {
            override fun run() {
                databaseManager.redefineRegionPositions(uuid, name, p1, p2)
            }
        }
        runner.runTaskAsynchronously(plugin)

        // Send message
        val regionRedefinedMessage = config.getRegionRedefined(name)
        MessageUtils.sendPlayerMessage(player, regionRedefinedMessage)
    }
}