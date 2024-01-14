package com.proiezrush.echregions.gui.items

import com.proiezrush.echregions.utils.MessageUtils
import org.bukkit.Material
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.impl.SimpleItem

class WandItem : SimpleItem(ItemBuilder(Material.IRON_AXE)
    .setDisplayName(MessageUtils.sParseColors("<gray>[<yellow><b>WAND</b></yellow>]</gray>")))