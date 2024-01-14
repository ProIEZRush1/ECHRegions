package com.proiezrush.echregions.gui.regions.items

import com.proiezrush.echregions.utils.MessageUtils
import org.bukkit.Material
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.SimpleItem

class WhitelistAddItem : SimpleItem(
    ItemBuilder(Material.IRON_PICKAXE)
        .setDisplayName(MessageUtils.sParseColors("<blue><b>WHITELIST ADD</b></blue>"))
        .addLoreLines("", MessageUtils.sParseColors("<gray>Click to add user to whitelist</gray>"))
)