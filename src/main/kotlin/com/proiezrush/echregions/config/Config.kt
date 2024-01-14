package com.proiezrush.echregions.config

import com.proiezrush.echregions.ECHRegions
import org.bukkit.configuration.file.FileConfiguration

class Config(private val plugin: ECHRegions) {

    fun load() {
        plugin.saveDefaultConfig()
    }

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

    fun getConfig(): FileConfiguration {
        return plugin.config
    }

}