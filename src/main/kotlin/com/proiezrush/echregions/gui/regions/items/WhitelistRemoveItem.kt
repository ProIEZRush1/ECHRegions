package com.proiezrush.echregions.gui.regions.items

import com.proiezrush.echregions.utils.MessageUtils
import org.bukkit.Material
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.SimpleItem

class WhitelistRemoveItem : SimpleItem(
    ItemBuilder(Material.BARRIER)
        .setDisplayName(MessageUtils.sParseColors("<blue><b>WHITELIST REMOVE</b></blue>"))
        .addLoreLines("", MessageUtils.sParseColors("<gray>Click to remove user from whitelist</gray>"))
)