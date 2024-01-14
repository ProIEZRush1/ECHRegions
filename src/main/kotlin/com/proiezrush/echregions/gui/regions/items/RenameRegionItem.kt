package com.proiezrush.echregions.gui.regions.items

import com.proiezrush.echregions.utils.MessageUtils
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.SimpleItem

class RenameRegionItem : SimpleItem(
    ItemBuilder(Material.NAME_TAG)
        .setDisplayName(MessageUtils.sParseColors("<blue><b>RENAME REGION</b></blue>"))
        .addLoreLines("", MessageUtils.sParseColors("<gray>Click to rename region</gray>"))
) {
    override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent) {
        event.isCancelled = true


    }
}