package com.proiezrush.echregions.config

import com.proiezrush.echregions.ECHRegions
import org.bukkit.configuration.file.FileConfiguration

class Config(private val plugin: ECHRegions) {

    fun load() {
        plugin.saveDefaultConfig()
    }

    /*
     * Database functions
     */
    fun getStorage(): String {
        return plugin.config.getString("database.storage") ?: "mysql"
    }

    fun getHost(): String {
        return plugin.config.getString("database.host") ?: "localhost"
    }

    fun getPort(): Int {
        return plugin.config.getInt("database.port")
    }

    fun getUsername(): String {
        return plugin.config.getString("database.username") ?: "username"
    }

    fun getPassword(): String {
        return plugin.config.getString("database.password") ?: "password"
    }

    fun getDatabase(): String {
        return plugin.config.getString("database.database") ?: "echregions"
    }

    /*
     * Region functions
     */
    fun getSectorSize(): Int {
        return plugin.config.getInt("region.sectorSize")
    }

    /*
     * Messages
     */

    /*
    PREFIX (messages.prefix)
     */
    fun getPrefix(): String {
        return plugin.config.getString("messages.prefix") ?: "<gray>[<blue><b>ECHRegions</b></blue>]</gray> "
    }

    /*
    COMMANDS_HELP (messages.commands-help)
     */
    fun getCommandsHelp(): List<String> {
        return plugin.config.getStringList("messages.commands-help")
    }

    /*
    Wand given (messages.wand-given)
     */
    fun getWandGiven(): String {
        return plugin.config.getString("messages.wand-given") ?: "<white>The wand has been given to you!</white>"
    }

    /*
    Region not found (messages.region-not-found)
     */
    fun getRegionNotFound(regionName: String): String {
        return plugin.config.getString("messages.region-not-found")?.replace("{region}", regionName)
            ?: "<white>The region <red><b>$regionName</b></red> does not exist!</white>"
    }

    /*
    Region already exists (messages.region-already-exists)
     */
    fun getRegionAlreadyExists(regionName: String): String {
        return plugin.config.getString("messages.region-already-exists")?.replace("{region}", regionName)
            ?: "<white>The region <red><b>$regionName</b></red> already exists!</white>"
    }

    /*
    Position not set (messages.position-not-set)
     */
    fun getPositionNotSet(positionName: String): String {
        return plugin.config.getString("messages.position-not-set")?.replace("{position}", positionName)
            ?: "<white>The position <red><b>$positionName</b></red> has not been set!</white>"
    }

    /*
    Positions need to be same world (messages.positions-need-to-be-same-world)
     */
    fun getPositionsNeedToBeSameWorld(): String {
        return plugin.config.getString("messages.positions-need-to-be-same-world")
            ?: "<white>The positions need to be in the same world!</white>"
    }

    /*
    Position set correctly (messages.position-set-correctly)
     */
    fun getPositionSetCorrectly(positionName: String): String {
        return plugin.config.getString("messages.position-set-correctly")?.replace("{position}", positionName)
            ?: "<white>The position <red><b>$positionName</b></red> has been set correctly!</white>"
    }

    /*
    Region created (messages.region-created)
     */
    fun getRegionCreated(regionName: String): String {
        return plugin.config.getString("messages.region-created")?.replace("{region}", regionName)
            ?: "<white>The region <red><b>$regionName</b></red> has been created!</white>"
    }

    fun getConfig(): FileConfiguration {
        return plugin.config
    }

}