package com.proiezrush.echregions.gui.regions

import com.proiezrush.echregions.ECHRegions
import com.proiezrush.echregions.gui.regions.items.PanelItem
import com.proiezrush.echregions.gui.regions.items.RegionItem
import com.proiezrush.echregions.gui.regions.items.ScrollDownItem
import com.proiezrush.echregions.gui.regions.items.ScrollUpItem
import com.proiezrush.echregions.objects.Region
import com.proiezrush.echregions.utils.MessageUtils
import org.bukkit.entity.Player
import xyz.xenondevs.invui.gui.Gui
import xyz.xenondevs.invui.gui.ScrollGui
import xyz.xenondevs.invui.gui.structure.Markers
import xyz.xenondevs.invui.window.Window

class RegionsGUI(private val plugin: ECHRegions, uuid: String) {

    private val regionList: List<Region?>
    private val gui: Gui

    init {
        val localDatabaseManager = plugin.getDatabaseImpl().getLocalDatabaseManager()
        regionList = localDatabaseManager.getPlayerRegions(uuid)

        val items = regionList.map { region ->
            RegionItem(plugin, region!!)
        }

        gui = ScrollGui.items()
            .setStructure("xxxxxxxxu",
                          "xxxxxxxx#",
                          "xxxxxxxx#",
                          "xxxxxxxxd",)
            .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
            .addIngredient('u', ScrollUpItem())
            .addIngredient('d', ScrollDownItem())
            .addIngredient('#', PanelItem())
            .setContent(items)
            .build()
    }

    fun open(player: Player) {
        val window = Window.single()
            .setViewer(player)
            .setTitle(MessageUtils.sParseColors("<gray>Regions</gray>"))
            .setGui(gui)
            .build()
        window.open()
    }

}