package com.proiezrush.echregions.listeners

import com.proiezrush.echregions.ECHRegions
import com.proiezrush.echregions.config.Config
import com.proiezrush.echregions.database.local.LocalDatabaseManager
import com.proiezrush.echregions.objects.Position
import com.proiezrush.echregions.objects.Region
import com.proiezrush.echregions.objects.SpatialKey
import com.proiezrush.echregions.utils.MessageUtils
import com.proiezrush.echregions.utils.RegionUtils
import net.kyori.adventure.text.BlockNBTComponent.Pos
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerInteractEvent

class RegionInteractionListener(private val plugin: ECHRegions) : Listener {

    private val localDatabaseManager: LocalDatabaseManager = plugin.getDatabaseImpl().getLocalDatabaseManager()
    private val config: Config = plugin.getDConfig()

    @EventHandler
    fun onRegionInteraction(event: PlayerInteractEvent) {
        val player = event.player

        val action = event.action
        val clickedBlock = event.clickedBlock
        if (action == Action.RIGHT_CLICK_BLOCK && clickedBlock != null) {
            when (clickedBlock.type) {
                Material.CHEST, Material.TRAPPED_CHEST,
                Material.ENDER_CHEST, Material.SHULKER_BOX,
                Material.FURNACE, Material.FURNACE_MINECART,
                Material.BLAST_FURNACE -> {
                    handleRegionInteraction(clickedBlock.location, player, event)
                }
                else -> {}
            }
        }
    }

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        val player = event.player

        val location = event.block.location
        handleRegionInteraction(location, player, event)
    }

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        val player = event.player

        val location = event.block.location
        handleRegionInteraction(location, player, event)
    }

    @EventHandler
    fun onPlayerDamage(event: EntityDamageEvent) {
        val entity = event.entity
        if (entity !is Player) {
            return
        }

        val location = entity.location
        //handleRegionInteraction(location, entity, event) // Not protecting now
    }

    @EventHandler
    fun onEntityDamagedByPlayer(event: EntityDamageByEntityEvent) {
        val damager = event.damager
        if (damager !is Player) {
            return
        }

        val location = damager.location
        //handleRegionInteraction(location, damager, event) // Not protecting now
    }

    @EventHandler
    fun onPlayerDamagedByEntity(event: EntityDamageByEntityEvent) {
        val entity = event.entity
        if (entity !is Player) {
            return
        }

        val location = entity.location
        //handleRegionInteraction(location, entity, event) // Not protecting now
    }

    private fun handleRegionInteraction(location: Location, player: Player, event: Cancellable) {
        val uuid = player.uniqueId.toString()

        // Get player region
        val spatialRegions = localDatabaseManager.spatialRegions


        val position = Position(player.world.name, location.x, location.y, location.z)

        val playerRegion = RegionUtils.findPlayerRegion(spatialRegions, config.getSectorSize(), position)
        if (playerRegion != null) {
            val whitelistedPlayers = playerRegion.whitelistedPlayers

            if (!whitelistedPlayers.contains(uuid) && !player.hasPermission("region.bypass.all") && !player.hasPermission("region.bypass." + playerRegion.name)) {
                event.isCancelled = true

                val regionNotAllowed = config.getRegionNotAllowed()
                //MessageUtils.sendPlayerMessage(player, regionNotAllowed)
            }
        }
    }


}