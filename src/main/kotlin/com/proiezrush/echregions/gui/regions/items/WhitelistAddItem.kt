package com.proiezrush.echregions.gui.regions.items

import com.proiezrush.echregions.ECHRegions
import com.proiezrush.echregions.gui.whitelist.WhitelistPlayerAction
import com.proiezrush.echregions.gui.whitelist.WhitelistPlayersGUI
import com.proiezrush.echregions.objects.Region
import com.proiezrush.echregions.utils.MessageUtils
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.builder.SkullBuilder
import xyz.xenondevs.invui.item.impl.SimpleItem

class WhitelistAddItem(private val plugin: ECHRegions, private val region: Region) : SimpleItem(
    SkullBuilder(SkullBuilder.HeadTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWZmMzE0MzFkNjQ1ODdmZjZlZjk4YzA2NzU4MTA2ODFmOGMxM2JmOTZmNTFkOWNiMDdlZDc4NTJiMmZmZDEifX19"))
        .setDisplayName(MessageUtils.sParseColors("<blue><b>WHITELIST ADD</b></blue>"))
        .addLoreLines("", MessageUtils.sParseColors("<gray>Click to add user to whitelist</gray>"))
) {
    override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent) {
        event.isCancelled = true

        // Open add whitelist GUI
        val onlinePlayers = Bukkit.getOnlinePlayers()
        val whitelistedPlayers = region.whitelistedPlayers
        val onlinePlayersUUIDs = onlinePlayers.map { it.uniqueId.toString() }.filter { !whitelistedPlayers.contains(it) }

        val addWhitelistPlayersGUI = WhitelistPlayersGUI(plugin, region, onlinePlayersUUIDs, WhitelistPlayerAction.ADD)
        addWhitelistPlayersGUI.open(player)
    }
}