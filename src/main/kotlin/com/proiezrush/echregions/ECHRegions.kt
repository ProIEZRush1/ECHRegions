package com.proiezrush.echregions

import com.proiezrush.echregions.commands.RegionCommand
import com.proiezrush.echregions.config.Config
import com.proiezrush.echregions.conversations.ConversationManager
import com.proiezrush.echregions.database.DatabaseImpl
import com.proiezrush.echregions.database.DatabaseType
import com.proiezrush.echregions.listeners.RegionInteractionListener
import com.proiezrush.echregions.listeners.WandListener
import com.proiezrush.echregions.utils.MessageUtils
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class ECHRegions : JavaPlugin() {

    private val config = Config(this)
    private lateinit var databaseImpl: DatabaseImpl;

    private lateinit var conversationManager: ConversationManager;

    override fun onEnable() {
        super.onEnable()

        val consoleWelcomeMessage = config.getPrefix() + "<green>Welcome to</green> <rainbow>ECHRegions!</rainbow>"
        MessageUtils.sendConsoleMessage(consoleWelcomeMessage)

        val loadingConfigMessage = config.getPrefix() + "<gray>Loading config...</gray>"
        MessageUtils.sendConsoleMessage(loadingConfigMessage)

        config.load()

        val configLoadedMessage = config.getPrefix() + "<green>Config loaded!</green>"
        MessageUtils.sendConsoleMessage(configLoadedMessage)

        val connectingToDatabaseMessage = config.getPrefix() + "<gray>Connecting to database...</gray>"
        MessageUtils.sendConsoleMessage(connectingToDatabaseMessage)

        val storage = config.getStorage()
        val host = config.getHost()
        val port = config.getPort()
        val username = config.getUsername()
        val password = config.getPassword()
        val db = config.getDatabase()
        val databaseInfoMessage = config.getPrefix() + "<gray>Database info:</gray>\n" +
                config.getPrefix() + "<gray>  Storage:</gray> <yellow>$storage</yellow>\n" +
                config.getPrefix() + "<gray>  Host:</gray> <yellow>$host</yellow>\n" +
                config.getPrefix() + "<gray>  Port:</gray> <yellow>$port</yellow>\n" +
                config.getPrefix() + "<gray>  Username:</gray> <yellow>$username</yellow>\n" +
                config.getPrefix() + "<gray>  Password:</gray> <yellow>${password.map { "*" }.joinToString("") }</yellow>\n" +
                config.getPrefix() + "<gray>  Database:</gray> <yellow>$db</yellow>"
        MessageUtils.sendMultipleConsoleMessages(databaseInfoMessage.split("\n"))

        databaseImpl = DatabaseImpl(host, port, username, password, db, DatabaseType.valueOf(storage.uppercase(Locale.getDefault())))
        try {
            // Will connect and do initial setup first time only
            databaseImpl.getDatabase().connect()

            // Load all regions from database to local memory to use them
            val sectorSize = config.getSectorSize()
            val (regionsBySpatialKey, regionsByUUID) = databaseImpl.getDatabase().getDatabaseManager().loadRegions(sectorSize) ?: Pair(mutableMapOf(), mutableMapOf())

            databaseImpl.getLocalDatabaseManager().regionsByUUID = regionsByUUID
            databaseImpl.getLocalDatabaseManager().spatialRegions = regionsBySpatialKey
        }
        catch (e: Exception) {
            val databaseConnectionFailedMessage = config.getPrefix() + "<red>Database connection failed!</red>\n" +
                    config.getPrefix() + "<gray>Error message:</gray> <yellow>${e.message?.split("\n")?.get(0)}</yellow>\n" +
                    config.getPrefix() + "<red>Disabling plugin...</red>"
            MessageUtils.sendMultipleConsoleMessages(databaseConnectionFailedMessage.split("\n"))

            server.pluginManager.disablePlugin(this)
            return
        }

        val databaseConnectedMessage = config.getPrefix() + "<green>Database connected!</green>"
        MessageUtils.sendConsoleMessage(databaseConnectedMessage)

        // Register commands
        getCommand("region")?.setExecutor(RegionCommand(this))

        // Register listener
        server.pluginManager.registerEvents(WandListener(this), this)
        server.pluginManager.registerEvents(RegionInteractionListener(this), this)

        // Register conversations
        conversationManager = ConversationManager(this)

        val pluginEnabledMessage = config.getPrefix() + "<green>Plugin enabled!</green>"
        MessageUtils.sendConsoleMessage(pluginEnabledMessage)
    }

    override fun onDisable() {
        super.onDisable()

        val disablingPluginMessage = config.getPrefix() + "<gray>Disabling plugin...</gray>"
        MessageUtils.sendConsoleMessage(disablingPluginMessage)

        databaseImpl.getDatabase().close()

        // End all ongoing conversations
        val conversations = databaseImpl.getLocalDatabaseManager().getAllPlayerRenamingRegionConversations()
        for (conversation in conversations.values) {
            conversation.abandon()
        }

        val pluginDisabledMessage = config.getPrefix() + "<red>Plugin disabled!</red>"
        MessageUtils.sendConsoleMessage(pluginDisabledMessage)
    }

    fun getDConfig(): Config {
        return config
    }

    fun getDatabaseImpl(): DatabaseImpl {
        return databaseImpl
    }

    fun getConversationManager(): ConversationManager {
        return conversationManager
    }

}