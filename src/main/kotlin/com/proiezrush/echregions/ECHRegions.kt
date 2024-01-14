package com.proiezrush.echregions

import com.proiezrush.echregions.config.Config
import com.proiezrush.echregions.database.Database
import com.proiezrush.echregions.database.DatabaseType
import com.proiezrush.echregions.utils.MessageUtils
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class ECHRegions : JavaPlugin() {

    private val config = Config(this)
    private lateinit var database: Database;

    override fun onEnable() {
        super.onEnable()

        val consoleWelcomeMessage = MessageUtils.PREFIX + "<green>Welcome to</green> <rainbow>ECHRegions!</rainbow>"
        MessageUtils.sendConsoleMessage(consoleWelcomeMessage)

        val loadingConfigMessage = MessageUtils.PREFIX + "<gray>Loading config...</gray>"
        MessageUtils.sendConsoleMessage(loadingConfigMessage)

        config.load()

        val configLoadedMessage = MessageUtils.PREFIX + "<green>Config loaded!</green>"
        MessageUtils.sendConsoleMessage(configLoadedMessage)

        val connectingToDatabaseMessage = MessageUtils.PREFIX + "<gray>Connecting to database...</gray>"
        MessageUtils.sendConsoleMessage(connectingToDatabaseMessage)

        val storage = config.getStorage()
        val host = config.getHost()
        val port = config.getPort()
        val username = config.getUsername()
        val password = config.getPassword()
        val db = config.getDatabase()
        val databaseInfoMessage = MessageUtils.PREFIX + "<gray>Database info:</gray>\n" +
                MessageUtils.PREFIX + "<gray>  Storage:</gray> <yellow>$storage</yellow>\n" +
                MessageUtils.PREFIX + "<gray>  Host:</gray> <yellow>$host</yellow>\n" +
                MessageUtils.PREFIX + "<gray>  Port:</gray> <yellow>$port</yellow>\n" +
                MessageUtils.PREFIX + "<gray>  Username:</gray> <yellow>$username</yellow>\n" +
                MessageUtils.PREFIX + "<gray>  Password:</gray> <yellow>$password</yellow>\n" +
                MessageUtils.PREFIX + "<gray>  Database:</gray> <yellow>$db</yellow>"
        MessageUtils.sendMultipleConsoleMessages(databaseInfoMessage)

        database = Database(host, port, username, password, db, DatabaseType.valueOf(storage.uppercase(Locale.getDefault())))
        try {
            database.getDatabase().connect()
        }
        catch (e: Exception) {
            val databaseConnectionFailedMessage = MessageUtils.PREFIX + "<red>Database connection failed!</red>\n" +
                    MessageUtils.PREFIX + "<gray>Error message:</gray> <yellow>${e.message?.split("\n")?.get(0)}</yellow>\n" +
                    MessageUtils.PREFIX + "<red>Disabling plugin...</red>"
            MessageUtils.sendMultipleConsoleMessages(databaseConnectionFailedMessage)

            server.pluginManager.disablePlugin(this)
            return
        }

        val databaseConnectedMessage = MessageUtils.PREFIX + "<green>Database connected!</green>"
        MessageUtils.sendConsoleMessage(databaseConnectedMessage)

        val pluginEnabledMessage = MessageUtils.PREFIX + "<green>Plugin enabled!</green>"
        MessageUtils.sendConsoleMessage(pluginEnabledMessage)
    }

    override fun onDisable() {
        super.onDisable()

        val disablingPluginMessage = MessageUtils.PREFIX + "<gray>Disabling plugin...</gray>"
        MessageUtils.sendConsoleMessage(disablingPluginMessage)

        val pluginDisabledMessage = MessageUtils.PREFIX + "<red>Plugin disabled!</red>"
        MessageUtils.sendConsoleMessage(pluginDisabledMessage)
    }

    fun getDConfig(): Config {
        return config
    }

}