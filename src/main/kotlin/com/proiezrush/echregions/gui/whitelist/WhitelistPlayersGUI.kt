package com.proiezrush.echregions.gui.whitelist

import com.proiezrush.echregions.ECHRegions
import com.proiezrush.echregions.gui.regions.items.PanelItem
import com.proiezrush.echregions.gui.regions.items.ScrollDownItem
import com.proiezrush.echregions.gui.regions.items.ScrollUpItem
import com.proiezrush.echregions.gui.whitelist.items.WhitelistPlayerItem
import com.proiezrush.echregions.objects.Region
import com.proiezrush.echregions.utils.MessageUtils
import org.bukkit.entity.Player
import xyz.xenondevs.invui.gui.Gui
import xyz.xenondevs.invui.gui.ScrollGui
import xyz.xenondevs.invui.gui.structure.Markers
import xyz.xenondevs.invui.window.Window

class WhitelistPlayersGUI(
    private val plugin: ECHRegions,
    private val region: Region,
    playersUUIDs: List<String>,
    private val whitelistPlayerAction: WhitelistPlayerAction
) {

    private val gui: Gui = ScrollGui.items()
        .setStructure("xxxxxxxxu",
                      "xxxxxxxx#",
                      "xxxxxxxx#",
                      "xxxxxxxxd",)
        .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL)
        .addIngredient('u', ScrollUpItem())
        .addIngredient('d', ScrollDownItem())
        .addIngredient('#', PanelItem())
        .setContent(playersUUIDs.map { playerUUID ->
            WhitelistPlayerItem(plugin, region.ownerUUID, region.name, playerUUID, whitelistPlayerAction)
        })
        .build()

    fun open(player: Player) {
        val window = Window.single()
            .setViewer(player)
            .setTitle(MessageUtils.sParseColors("${if (whitelistPlayerAction == WhitelistPlayerAction.ADD) "Add" else "Remove"} Whitelisted Players"))
            .setGui(gui)
            .build()
        window.open()
    }
}

enum class WhitelistPlayerAction {
    ADD, REMOVE
}