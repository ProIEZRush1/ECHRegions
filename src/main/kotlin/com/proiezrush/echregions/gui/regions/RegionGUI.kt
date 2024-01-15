package com.proiezrush.echregions.gui.regions

import com.proiezrush.echregions.ECHRegions
import com.proiezrush.echregions.gui.regions.items.*
import com.proiezrush.echregions.objects.Region
import com.proiezrush.echregions.utils.MessageUtils
import org.bukkit.entity.Player
import xyz.xenondevs.invui.gui.Gui
import xyz.xenondevs.invui.window.Window

class RegionGUI(plugin: ECHRegions, private val region: Region) {

    private val regionName: String = region.name
    private val gui: Gui = Gui.normal()
        .setStructure("#########",
                      "#12345..#",
                      "#########")
        .addIngredient('#', PanelItem())
        .addIngredient('1', RenameRegionItem(plugin, regionName))
        .addIngredient('2', WhitelistAddItem(plugin, region))
        .addIngredient('3', WhitelistRemoveItem(plugin, region))
        .addIngredient('4', RedefineLocationItem(plugin, regionName))
        .addIngredient('5', DeleteRegionItem(plugin, region.ownerUUID, regionName))
        .build()

    fun open(player: Player) {
        val window = Window.single()
            .setViewer(player)
            .setTitle(MessageUtils.sParseColors("<gray>Region</gray> <yellow>${regionName}</yellow>"))
            .setGui(gui)
            .build()
        window.open()
    }
}