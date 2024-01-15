package com.proiezrush.echregions.gui.regions.items

import com.proiezrush.echregions.ECHRegions
import com.proiezrush.echregions.gui.whitelist.WhitelistPlayerAction
import com.proiezrush.echregions.gui.whitelist.WhitelistPlayersGUI
import com.proiezrush.echregions.objects.Region
import com.proiezrush.echregions.utils.MessageUtils
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.builder.SkullBuilder
import xyz.xenondevs.invui.item.impl.SimpleItem

class WhitelistRemoveItem(private val plugin: ECHRegions, private val region: Region) : SimpleItem(
    SkullBuilder(SkullBuilder.HeadTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzk3YzZmMjlhOTUwYTk0NDg5NDliMzY1N2QwZGVkYzgwNzA5Y2JhYTRjNzE0NTFhY2RiZTM4ODZmYzUwZDYxOCJ9fX0="))
        .setDisplayName(MessageUtils.sParseColors("<blue><b>WHITELIST REMOVE</b></blue>"))
        .addLoreLines("", MessageUtils.sParseColors("<gray>Click to remove user from whitelist</gray>"))
) {
    override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent) {
        event.isCancelled = true

        // Open remove whitelist GUI
        val addWhitelistPlayersGUI = WhitelistPlayersGUI(plugin, region, region.whitelistedPlayers, WhitelistPlayerAction.REMOVE)
        addWhitelistPlayersGUI.open(player)
    }
}