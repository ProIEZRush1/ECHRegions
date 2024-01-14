package com.proiezrush.echregions.gui.regions.items

import com.proiezrush.echregions.utils.MessageUtils
import org.bukkit.Material
import xyz.xenondevs.invui.gui.ScrollGui
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.ItemBuilder
import xyz.xenondevs.invui.item.builder.SkullBuilder
import xyz.xenondevs.invui.item.builder.SkullBuilder.HeadTexture
import xyz.xenondevs.invui.item.impl.controlitem.ScrollItem

class ScrollUpItem : ScrollItem(-1) {

    override fun getItemProvider(p0: ScrollGui<*>?): ItemProvider {
        val texture = HeadTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzhhMjU4MDkzODFjY2I3MTI2YzBkZjVjM2QxOWM0MWFmNTM4YWFiY2FlMjc4MTM0M2IwMzI0YjFkZGUwNzhlMCJ9fX0=")
        val builder = SkullBuilder(texture)
        builder.setDisplayName(MessageUtils.sParseColors("<green><b>SCROLL UP</b></green>"))
        builder.addLoreLines("", MessageUtils.sParseColors("<gray>Click to scroll up</gray>"))
        if (!gui.canScroll(1)) {
            builder.addLoreLines(MessageUtils.sParseColors("<red><b>Cannot scroll up</b></red>"))
        }
        return builder
    }
}