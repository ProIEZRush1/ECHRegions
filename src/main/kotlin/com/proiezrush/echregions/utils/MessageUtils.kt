package com.proiezrush.echregions.utils

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player

@Suppress("DEPRECATION")
class MessageUtils {

    companion object {

        fun sendConsoleMessage(message: String) {
            Bukkit.getConsoleSender().sendMessage(parseColors(message))
        }

        fun sendMultipleConsoleMessages(messages: List<String>) {
            messages.forEach { sendConsoleMessage(it) }
        }

        fun sendPlayerMessage(player: Player, message: String) {
            player.sendMessage(parseColors(message))
        }

        fun sendMultiplePlayerMessages(player: Player, messages: List<String>) {
            messages.forEach { sendPlayerMessage(player, it) }
        }

        // Only 1 MiniMessage instance
        private val miniMessage = MiniMessage.miniMessage()

        // New colors with PaperMC using <green> tags
        fun parseColors(message: String): Component {
            return miniMessage.deserialize(message)
        }

        fun sParseColors(message: String): String {
            val component = parseColors(message)
            val serialize = LegacyComponentSerializer.legacyAmpersand().serialize(component)
            return ChatColor.translateAlternateColorCodes('&', serialize)
        }

    }

}