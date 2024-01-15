package com.proiezrush.echregions.utils

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import kotlin.math.min

@Suppress("DEPRECATION")
class MessageUtils {

    companion object {

        fun sendConsoleMessage(message: String, tagResolver: TagResolver.Builder? = null) {
            Bukkit.getConsoleSender().sendMessage(parseColors(message, tagResolver))
        }

        fun sendMultipleConsoleMessages(messages: List<String>, tagResolver: TagResolver.Builder? = null) {
            messages.forEach { sendConsoleMessage(it, tagResolver) }
        }

        fun sendPlayerMessage(player: Player, message: String, tagResolver: TagResolver.Builder? = null) {
            player.sendMessage(parseColors(message, tagResolver))
        }

        fun sendMultiplePlayerMessages(player: Player, messages: List<String>, tagResolver: TagResolver.Builder? = null) {
            messages.forEach { sendPlayerMessage(player, it, tagResolver) }
        }
        
        private val miniMessage = MiniMessage.miniMessage()

        // New colors with PaperMC using <green> tagResolver
        fun parseColors(message: String, tagResolver: TagResolver.Builder? = null): Component {
            val miniMessageTagResolverBuilder = tagResolver?.let { 
                tagResolver.resolver(StandardTags.color())
                MiniMessage.builder().tags(it.build()).build()
            } ?: miniMessage
            return miniMessageTagResolverBuilder.deserialize(message)
        }

        fun sParseColors(message: String): String {
            val component = parseColors(message)
            val serialize = LegacyComponentSerializer.legacyAmpersand().serialize(component)
            return ChatColor.translateAlternateColorCodes('&', serialize)
        }

        fun componentToString(component: Component): String {
            return PlainTextComponentSerializer.plainText().serialize(component)
        }

    }

}