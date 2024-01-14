package com.proiezrush.echregions.commands

import com.proiezrush.echregions.ECHRegions
import com.proiezrush.echregions.config.Config
import com.proiezrush.echregions.database.DatabaseManager
import com.proiezrush.echregions.database.local.LocalDatabaseManager
import com.proiezrush.echregions.gui.items.WandItem
import com.proiezrush.echregions.gui.regions.RegionGUI
import com.proiezrush.echregions.gui.regions.RegionsGUI
import com.proiezrush.echregions.objects.Region
import com.proiezrush.echregions.utils.MessageUtils
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

class RegionCommand(private val plugin: ECHRegions) : CommandExecutor {

    private val config: Config = plugin.getDConfig()
    private val localDatabaseManager: LocalDatabaseManager = plugin.getDatabaseImpl().getLocalDatabaseManager()
    private val databaseManager: DatabaseManager = plugin.getDatabaseImpl().getDatabase().getDatabaseManager()

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (sender is ConsoleCommandSender) {
            MessageUtils.sendConsoleMessage("<red>This command can only be executed in-game!</red>")
            return true
        }

        /*
        Commands:
            /region - Opens the regions menu
            /region create <name> - Creates a region at the selected location
            /region wand - Gives the user a stick with a custom name to select locations to create a region
            /region add <name> <username> - Whitelist a user to a region
            /region remove <name> <username> - Removes a user from the region whitelist
            /region whitelist <name> - Lists the users in the region whitelist
            /region <name> - Opens the region menu
         */

        val player = sender as Player
        val uuid = player.uniqueId.toString()
        if (args.isNullOrEmpty()) {
            // Open all player regions menu
            val regionsGUI = RegionsGUI(plugin, uuid)
            regionsGUI.open(player)
        }
        else if (args.size == 1) {
            val first = args[0]

            when (first) {
                "wand" -> {
                    val wandItem = WandItem()
                    player.inventory.addItem(wandItem.itemProvider.get())

                    val wandGivenMessage = config.getWandGiven()
                    MessageUtils.sendPlayerMessage(player, wandGivenMessage)

                    return true
                }
                else -> {
                    // Search region with arg name
                    val region = localDatabaseManager.searchRegionByName(uuid, first)
                    if (region == null) {
                        val regionNotFoundMessage = config.getRegionNotFound(first)
                        MessageUtils.sendPlayerMessage(player, regionNotFoundMessage)

                        return true
                    }

                    // Open region gui
                    val regionGUI = RegionGUI(region)
                    regionGUI.open(player)

                    return true
                }
            }
        }
        else if (args.size == 2) {
            val first = args[0]
            val second = args[1]
            
            when (first) {
                "create" -> {
                    val regionExists = localDatabaseManager.searchRegionByName(uuid, second)
                    if (regionExists != null) {
                        val regionAlreadyExistsMessage = config.getRegionAlreadyExists(second)
                        MessageUtils.sendPlayerMessage(player, regionAlreadyExistsMessage)

                        return true
                    }

                    val p1 = localDatabaseManager.getPlayerFirstPosition(uuid)
                    if (p1 == null) {
                        val positionNotSetMessage = config.getPositionNotSet("first")
                        MessageUtils.sendPlayerMessage(player, positionNotSetMessage)

                        return true
                    }

                    val p2 = localDatabaseManager.getPlayerSecondPosition(uuid)
                    if (p2 == null) {
                        val positionNotSetMessage = config.getPositionNotSet("second")
                        MessageUtils.sendPlayerMessage(player, positionNotSetMessage)

                        return true
                    }

                    val region = Region(uuid, second, p1, p2, mutableMapOf())
                    localDatabaseManager.addRegion(uuid, region)

                    // Add region to database async
                    val runnable = object : BukkitRunnable() {
                        override fun run() {
                            databaseManager.createRegion(uuid, second, p1, p2)
                        }
                    }
                    runnable.runTaskAsynchronously(plugin)

                    // Delete old positions
                    localDatabaseManager.clearPlayerPositions(uuid)

                    val regionCreatedMessage = config.getRegionCreated(second)
                    MessageUtils.sendPlayerMessage(player, regionCreatedMessage)

                    return true
                }
                "whitelist" -> {

                }
                else -> {
                    sendCommandsHelpMessage(player)

                    return true
                }
            }
        }
        sendCommandsHelpMessage(player)

        return true
    }

    private fun sendCommandsHelpMessage(player: Player) {
        val commandsHelpMessage = config.getCommandsHelp()
        MessageUtils.sendMultiplePlayerMessages(player, commandsHelpMessage)
    }

}