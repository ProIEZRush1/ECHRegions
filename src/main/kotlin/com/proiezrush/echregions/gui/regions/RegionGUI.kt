package com.proiezrush.echregions.gui.regions

import com.proiezrush.echregions.gui.regions.items.*
import com.proiezrush.echregions.objects.Region
import com.proiezrush.echregions.utils.MessageUtils
import org.bukkit.entity.Player
import xyz.xenondevs.invui.gui.Gui
import xyz.xenondevs.invui.window.Window

class RegionGUI(private val region: Region) {

    private val gui: Gui = Gui.normal()
        .setStructure("#########",
                      "#1234...#",
                      "#########")
        .addIngredient('#', PanelItem())
        .addIngredient('1', RenameRegionItem())
        .addIngredient('2', WhitelistAddItem())
        .addIngredient('3', WhitelistRemoveItem())
        .addIngredient('4', RedefineLocationItem())
        .build()

    fun open(player: Player) {
        val window = Window.single()
            .setViewer(player)
            .setTitle(MessageUtils.sParseColors("<gray>Region</gray> <yellow>${region.name}</yellow>"))
            .setGui(gui)
            .build()
        window.open()
    }
}