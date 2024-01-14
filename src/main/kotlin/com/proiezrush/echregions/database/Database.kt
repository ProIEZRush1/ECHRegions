package com.proiezrush.echregions.database

import com.proiezrush.echregions.database.mysql.MySQL

class Database {

    private val databaseType: DatabaseType
    private val mySQL: MySQL;

    constructor(host: String, port: Int, username: String, password: String, database: String,
                databaseType: DatabaseType) {
        this.databaseType = databaseType

        when (databaseType) {
            DatabaseType.MYSQL -> {
                this.mySQL = MySQL(host, port, username, password, database)
            }
            else -> {
                this.mySQL = MySQL(host, port, username, password, database)
            }
        }
    }

    fun getDatabase() : DatabaseImpl {
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