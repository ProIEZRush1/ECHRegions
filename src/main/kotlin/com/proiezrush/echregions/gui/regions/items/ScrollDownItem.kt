package com.proiezrush.echregions.gui.regions.items

import com.proiezrush.echregions.utils.MessageUtils
import xyz.xenondevs.invui.gui.ScrollGui
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.item.builder.SkullBuilder
import xyz.xenondevs.invui.item.impl.controlitem.ScrollItem

class ScrollDownItem : ScrollItem(1) {

    override fun getItemProvider(p0: ScrollGui<*>?): ItemProvider {
        val texture =
            SkullBuilder.HeadTexture("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzM5NDNjMTcxOTNjMDA0MDU0YWFjZWU5NmZjZWVjZDQ5OGY1MTc4YzY3ODYzOWRmNjdiOTRkYTVhNjY0NDlhMSJ9fX0=")
        val builder = SkullBuilder(texture)
        builder.setDisplayName(MessageUtils.sParseColors("<green><b>SCROLL DOWN</b></green>"))
        builder.addLoreLines("", MessageUtils.sParseColors("<gray>Click to scroll up</gray>"))
        if (!gui.canScroll(-1)) {
            builder.addLoreLines(MessageUtils.sParseColors("<red><b>Cannot scroll down</b></red>"))
        }
        return builder
    }
}