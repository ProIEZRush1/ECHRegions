package com.proiezrush.echregions.gui.regions.items

import com.proiezrush.echregions.gui.regions.RegionGUI
import com.proiezrush.echregions.objects.Region
import com.proiezrush.echregions.utils.MessageUtils
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import xyz.xenondevs.invui.item.builder.SkullBuilder
import xyz.xenondevs.invui.item.impl.SimpleItem

class RegionItem(private val region: Region) : SimpleItem(
    SkullBuilder(SkullBuilder.HeadTexture(RandomRegionHead.getRandomHead()))
        .setDisplayName(MessageUtils.sParseColors("<blue><b>${region.name}</b></blue>"))
        .addLoreLines("", MessageUtils.sParseColors("<yellow>Whitelisted players: ${region.whitelistedPlayers.size}</yellow>"),
            MessageUtils.sParseColors("<gray>Click to open region menu</gray>"))
) {
    override fun handleClick(clickType: ClickType, player: Player, event: InventoryClickEvent) {
        super.handleClick(clickType, player, event)

        // Open region GUI
        val regionGUI = RegionGUI(region)
        regionGUI.open(player)
    }
}

private class RandomRegionHead {
    companion object {
        private val headList = listOf(
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDNkMTJlOGFlMzBlMzA2N2Q3MmMyZWM5ZWUyMWRmNzFjYjlhNDQwNDEzNDA3MTA1ZjBkNmUxNzE2NWEzMDllYSJ9fX0=",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDU4ZjFhNzJlYjUyYzdkNjgxZjc5NTk5YzNiZjdlYTkxMmY4YzhhZmM4ZWU5NTRiZTMzZWFhNmJiNGNlNGZiIn19fQ==",
            "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTlhYjZiMDkzZjliNDY4MTIyZmVjY2Q3ZDc0MmFlMTQwNjBiOWQ2YjNmN2YxOTI3MmE5NTMzY2UwMzA2NDc5YyJ9fX0="
        )

        fun getRandomHead(): String {
            return headList.random()
        }
    }

}