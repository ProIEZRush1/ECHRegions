package com.proiezrush.echregions.commands

import com.proiezrush.echregions.ECHRegions
import com.proiezrush.echregions.utils.MessageUtils
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player

class RegionCommand(private val plugin: ECHRegions) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (sender is ConsoleCommandSender) {
            MessageUtils.sendConsoleMessage("<red>This command can only be executed in-game!</red>")
            return true
        }

        val player = sender as Player
        if (args.isNullOrEmpty()) {
            // Open all player regions menu
        }
        else if (args.size > 1) {

        }

        return false
    }

}