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

    /*
    Region deleted (messages.region-deleted)
     */
    fun getRegionDeleted(regionName: String): String {
        return plugin.config.getString("messages.region-deleted")?.replace("{region}", regionName)
            ?: "<white>The region <red><b>$regionName</b></red> has been deleted!</white>"
    }

    /*
    No whitelisted players (messages.no-whitelisted-players)
     */
    fun getNoWhitelistedPlayers(): String {
        return plugin.config.getString("messages.no-whitelisted-players")
            ?: "<white>No whitelisted players!</white>"
    }

    /*
    Whitelisted players list title (messages.whitelisted-players-list-title)
     */
    fun getWhitelistedPlayersListTitle(count: Int): String {
        return plugin.config.getString("messages.whitelisted-players-list-title")?.replace("{count}", count.toString())
            ?: "<gray>Users:</gray> <yellow><b>{count}</b></yellow>"
    }

    /*
    Whitelisted players list player (messages.whitelisted-players-list-player)
     */
    fun getWhitelistedPlayersListPlayer(player: String): String {
        return plugin.config.getString("messages.whitelisted-players-list-player")?.replace("{player}", player)
            ?: "<gray>-</gray> <yellow><b>{player}</b></yellow>"
    }

    /*
    Player not found (messages.player-not-found)
     */
    fun getPlayerNotFound(player: String): String {
        return plugin.config.getString("messages.player-not-found")?.replace("{player}", player)
            ?: "<white>The player <red><b>$player</b></red> does not exist!</white>"
    }

    /*
    Player already whitelisted (messages.player-already-whitelisted)
     */
    fun getPlayerAlreadyWhitelisted(player: String): String {
        return plugin.config.getString("messages.player-already-whitelisted")?.replace("{player}", player)
            ?: "<white>The player <red><b>$player</b></red> is already whitelisted!</white>"
    }

    /*
    Player whitelisted (messages.player-whitelisted)
     */
    fun getPlayerWhitelisted(player: String): String {
        return plugin.config.getString("messages.player-whitelisted")?.replace("{player}", player)
            ?: "<white>The player <red><b>$player</b></red> has been whitelisted!</white>"
    }

    /*
    Player not whitelisted (messages.player-not-whitelisted)
     */
    fun getPlayerNotWhitelisted(player: String): String {
        return plugin.config.getString("messages.player-not-whitelisted")?.replace("{player}", player)
            ?: "<white>The player <red><b>$player</b></red> is not whitelisted!</white>"
    }

    /*
    Player removed from whitelist (messages.player-removed-from-whitelist)
     */
    fun getPlayerRemovedFromWhitelist(player: String): String {
        return plugin.config.getString("messages.player-removed-from-whitelist")?.replace("{player}", player)
            ?: "<white>The player <red><b>$player</b></red> has been removed from the whitelist!</white>"
    }

    /*
    Renaming region (messages.renaming-region)
     */
    fun getRenamingRegion(player: String): String {
        return plugin.config.getString("messages.renaming-region")?.replace("{player}", player)
            ?: "<white>Renaming region...</white>"
    }

    /*
    Region renamed (messages.region-renamed)
     */
    fun getRegionRenamed(oldRegionName: String, regionName: String): String {
        return plugin.config.getString("messages.region-renamed")?.replace("{oldName}", oldRegionName)?.replace("{newName}", regionName)
            ?: "<green>The region <gray><b>${regionName}</b></gray> has been renamed to <gray><b>{newName}</b></gray>!</green>"
    }

    /*
    Redefining region (messages.redefining-region)
     */
    fun getRedefiningRegion(player: String): String {
        return plugin.config.getString("messages.redefining-region")?.replace("{player}", player)
            ?: "<white>Redefining region...</white>"
    }

    /*
    Region redefined (messages.region-redefined)
     */
    fun getRegionRedefined(regionName: String): String {
        return plugin.config.getString("messages.region-redefined")?.replace("{region}", regionName)
            ?: "<green>The region <gray><b>{region}</b></gray> positions have been redefined!</green>"
    }

    /*
    Region not allowed
     */
    fun getRegionNotAllowed(): String {
        return plugin.config.getString("messages.region-not-allowed")
            ?: "<red>You are not allowed to do this!</red>"
    }

    fun getConfig(): FileConfiguration {
        return plugin.config
    }

}