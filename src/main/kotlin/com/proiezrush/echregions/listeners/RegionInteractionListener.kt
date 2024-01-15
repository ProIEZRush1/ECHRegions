package com.proiezrush.echregions.listeners

import com.proiezrush.echregions.ECHRegions
import com.proiezrush.echregions.config.Config
import com.proiezrush.echregions.database.local.LocalDatabaseManager
import com.proiezrush.echregions.objects.Position
import com.proiezrush.echregions.objects.Region
import com.proiezrush.echregions.objects.SpatialKey
import com.proiezrush.echregions.utils.MessageUtils
import net.kyori.adventure.text.BlockNBTComponent.Pos
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent

class RegionInteractionListener(private val plugin: ECHRegions) : Listener {

    private val localDatabaseManager: LocalDatabaseManager = plugin.getDatabaseImpl().getLocalDatabaseManager()
    private val config: Config = plugin.getDConfig()

    @EventHandler
    fun onRegionInteraction(event: PlayerInteractEvent) {
        val player = event.player
        val uuid = player.uniqueId.toString()

        val action = event.action
        if (action.toString().uppercase().contains("AIR")) {
            return
        }

        // Get player region
        val spatialRegions = localDatabaseManager.spatialRegions

        val playerLocation = player.location
        val position = Position(player.world.name, playerLocation.x, playerLocation.y, playerLocation.z)

        val playerRegion = findPlayerRegion(spatialRegions, config.getSectorSize(), position)
        MessageUtils.sendPlayerMessage(player, playerRegion.toString())
        if (playerRegion != null) {
            val whitelistedPlayers = playerRegion.whitelistedPlayers

            if (!whitelistedPlayers.contains(uuid) && !player.hasPermission("region.bypass.all") && !player.hasPermission("region.bypass." + playerRegion.name)) {
                event.isCancelled = true

                val regionNotAllowed = config.getRegionNotAllowed()
                //MessageUtils.sendPlayerMessage(player, regionNotAllowed)
            }
        }
    }

    private fun findPlayerRegion(regionsBySpatialKey: Map<SpatialKey, MutableList<Region?>>, sectorSize: Int, playerPosition: Position): Region? {
        val playerKey = SpatialKey(
            (playerPosition.x / sectorSize).toInt(),
            (playerPosition.y / sectorSize).toInt(),
            (playerPosition.z / sectorSize).toInt()
        )

        regionsBySpatialKey[playerKey]?.forEach { region ->
            if (isPositionInsideRegion(playerPosition, region!!)) {
                return region
            }
        }

        return null
    }

    private fun isPositionInsideRegion(position: Position, region: Region): Boolean {
        // Check world names, p1 and p2 will always be in the same world
        if (position.world != region.position1.world) {
            return false
        }

        return position.x in minOf(region.position1.x, region.position2.x)..maxOf(region.position1.x, region.position2.x) &&
                position.y in minOf(region.position1.y, region.position2.y)..maxOf(region.position1.y, region.position2.y) &&
                position.z in minOf(region.position1.z, region.position2.z)..maxOf(region.position1.z, region.position2.z)
    }
}