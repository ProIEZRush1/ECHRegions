package com.proiezrush.echregions.utils

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class MessageUtils {

    companion object {

        const val PREFIX = "<gray>[<blue><b>ECHRegions</b></blue>]</gray> "

        fun sendConsoleMessage(message: String) {
            Bukkit.getConsoleSender().sendMessage(parseColors(message))
        }

        fun sendMultipleConsoleMessages(message: String, separator: String = "\n") {
            message.split(separator).forEach { sendConsoleMessage(it) }
        }

        fun sendPlayerMessage(player: Player, message: String) {
            player.sendMessage(parseColors(message))
        }

        // Only 1 MiniMessage instance
        private val miniMessage = MiniMessage.miniMessage()

        // New colors with PaperMC using <green> tags
        fun parseColors(message: String): Component {
            return miniMessage.deserialize(message)
        }

    }

}