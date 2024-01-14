package com.proiezrush.echregions.gui.regions.items

import com.proiezrush.echregions.utils.MessageUtils
import org.bukkit.Material
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.SimpleItem

class RedefineLocationItem : SimpleItem(
    ItemBuilder(Material.IRON_AXE)
        .setDisplayName(MessageUtils.sParseColors("<blue><b>REDEFINE LOCATION</b></blue>"))
        .addLoreLines("", MessageUtils.sParseColors("<gray>Click to redefine the location</gray>"))
)