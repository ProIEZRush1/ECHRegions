package com.proiezrush.echregions.conversations.rename_region

import com.proiezrush.echregions.ECHRegions
import com.proiezrush.echregions.config.Config
import com.proiezrush.echregions.database.DatabaseManager
import com.proiezrush.echregions.database.local.LocalDatabaseManager
import com.proiezrush.echregions.utils.MessageUtils
import org.bukkit.conversations.ConversationContext
import org.bukkit.conversations.Prompt
import org.bukkit.conversations.StringPrompt
import org.bukkit.conversations.ValidatingPrompt
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

class RenameRegionPrompt(private val plugin: ECHRegions, private val uuid: String, private val name: String) : ValidatingPrompt() {

    private val config: Config = plugin.getDConfig()
    private val localDatabaseManager: LocalDatabaseManager = plugin.getDatabaseImpl().getLocalDatabaseManager()
    private val databaseManager: DatabaseManager = plugin.getDatabaseImpl().getDatabase().getDatabaseManager()

    override fun getPromptText(p0: ConversationContext): String {
        val renamingRegionMessage = config.getRenamingRegion(name)
        return MessageUtils.sParseColors(renamingRegionMessage)
    }

    override fun isInputValid(p0: ConversationContext, newName: String): Boolean {
        val regionExists = localDatabaseManager.searchRegionByName(uuid, newName)
        if (regionExists != null) {
            val regionAlreadyExistsMessage = config.getRegionAlreadyExists(newName)
            MessageUtils.sendPlayerMessage(p0.forWhom as Player, regionAlreadyExistsMessage)

            return false
        }
        return true
    }

    override fun acceptValidatedInput(p0: ConversationContext, newName: String): Prompt? {
        // Rename region locally
        localDatabaseManager.renameRegion(uuid, name, newName)

        // Rename region in database with async task
        val runner = object : BukkitRunnable() {
            override fun run() {
                databaseManager.renameRegion(uuid, name, newName)
            }
        }
        runner.runTaskAsynchronously(plugin)

        // Send message
        val regionRenamedMessage = config.getRegionRenamed(name, newName)
        MessageUtils.sendPlayerMessage(p0.forWhom as Player, regionRenamedMessage)

        return Prompt.END_OF_CONVERSATION
    }

}