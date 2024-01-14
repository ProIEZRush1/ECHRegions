package com.proiezrush.echregions.database.mysql

import com.proiezrush.echregions.database.Database
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class MySQL(private val host: String, private val port: Int, private val username: String, private val password: String, private val database: String) :
    Database {
    private var connection: Connection? = null
    private var mySQLDatabaseManagerImpl: MySQLDatabaseManagerImpl? = null

    @Throws(SQLException::class)
    override fun connect() {
        if (connection != null && !connection!!.isClosed) {
            return
        }

        synchronized(this) {
            if (connection != null && !connection!!.isClosed) {
                return
            }
            Class.forName("com.mysql.cj.jdbc.Driver")
            connection = DriverManager.getConnection(
                "jdbc:mysql://$host:$port/$database?useSSL=false",
                username, password
            )
            setup()
        }
    }

    override fun setup() {
        /*
        Table structure:
            - Regions(ech_regions):
                - id
                - ownerUUID(varchar(36))
                - name(TEXT)
            - Positions(ech_positions):
                - id
                - regionId(INT)
                - type(INT)
                - world(TEXT)
                - x(DOUBLE)
                - y(DOUBLE)
                - z(DOUBLE)
            - WhitelistedPlayers(ech_whitelisted_players):
                - id
                - regionId(INT)
                - uuid(varchar(36))
         */
        this.mySQLDatabaseManagerImpl = MySQLDatabaseManagerImpl(connection!!)

        connection?.let { conn ->
            conn.createStatement().use { stmt ->
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ech_regions (id INT AUTO_INCREMENT PRIMARY KEY, ownerUUID VARCHAR(36), name TEXT)")
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ech_positions (id INT AUTO_INCREMENT PRIMARY KEY, regionId INT, type INT, world TEXT, x DOUBLE, y DOUBLE, z DOUBLE)")
                stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ech_whitelisted_players (id INT AUTO_INCREMENT PRIMARY KEY, regionId INT, uuid VARCHAR(36))")
            }
        }
    }

    override fun getConnection(): Connection? {
        return connection
    }

    override fun getDatabaseManager(): MySQLDatabaseManagerImpl {
        return mySQLDatabaseManagerImpl!!
    }

    override fun close() {
        try {
            connection?.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }
}