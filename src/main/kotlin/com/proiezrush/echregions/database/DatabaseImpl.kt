package com.proiezrush.echregions.database

import com.proiezrush.echregions.database.local.LocalDatabaseManager
import com.proiezrush.echregions.database.mysql.MySQL

class DatabaseImpl(host: String, port: Int, username: String, password: String, database: String, private val databaseType: DatabaseType) {

    private val localDatabaseManager: LocalDatabaseManager = LocalDatabaseManager();
    private val mySQL: MySQL;

    init {
        when (databaseType) {
            DatabaseType.MYSQL -> {
                this.mySQL = MySQL(host, port, username, password, database)
            }

            else -> {
                this.mySQL = MySQL(host, port, username, password, database)
            }
        }
    }

    fun getLocalDatabaseManager() : LocalDatabaseManager {
        return localDatabaseManager
    }

    fun getDatabase() : Database {
        when (databaseType) {
            DatabaseType.MYSQL -> {
                return mySQL
            }
            else -> {
                return mySQL
            }
        }
    }

}

enum class DatabaseType {
    MYSQL
}