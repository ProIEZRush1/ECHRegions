package com.proiezrush.echregions.database.mysql

import com.proiezrush.echregions.database.DatabaseManagerImpl
import com.proiezrush.echregions.objects.Position
import com.proiezrush.echregions.objects.Region
import com.proiezrush.echregions.objects.WPlayer
import java.sql.Connection
import java.sql.Statement

class MySQLDatabaseManager(private val connection: Connection) : DatabaseManagerImpl {

    override fun createRegion(name: String, position1: Position, position2: Position): Region {
        val region = Region(name, position1, position2, listOf())

        this.connection.let { conn ->
            val insertRegionSql = "INSERT INTO ech_regions (name) VALUES (?)"
            val insertPositionSql = "INSERT INTO ech_positions (regionId, world, x, y, z) VALUES (?, ?, ?, ?, ?)"

            conn.prepareStatement(insertRegionSql, Statement.RETURN_GENERATED_KEYS).use { insertRegionStmt ->
                insertRegionStmt.setString(1, name)
                insertRegionStmt.executeUpdate()

                val generatedKeys = insertRegionStmt.generatedKeys
                if (generatedKeys.next()) {
                    val regionId = generatedKeys.getInt(1)

                    conn.prepareStatement(insertPositionSql).use { insertPositionStmt ->
                        insertPositionStmt.setInt(1, regionId)
                        insertPositionStmt.setString(2, position1.world)
                        insertPositionStmt.setDouble(3, position1.x)
                        insertPositionStmt.setDouble(4, position1.y)
                        insertPositionStmt.setDouble(5, position1.z)
                        insertPositionStmt.executeUpdate()
                    }
                }
            }
        }

        return region
    }

    override fun getRegion(regionId: Int): Region? {
        this.connection.let { conn ->
            val whitelistedPlayers = mutableListOf<WPlayer>()

            // Get region
            val regionQuery = "SELECT * FROM ech_regions WHERE id = ?"
            val regionName = conn.prepareStatement(regionQuery).use { regionStmt ->
                regionStmt.setInt(1, regionId)
                regionStmt.executeQuery().use { regionSet ->
                    if (regionSet.next()) regionSet.getString("name") else null
                }
            }

            // Get positions
            val position1 = getPositionByType(conn, regionId, 1)
            val position2 = getPositionByType(conn, regionId, 2)

            // Get whitelisted players
            val whitelistedPlayersQuery = "SELECT * FROM ech_whitelisted_players WHERE regionId = ?"
            conn.prepareStatement(whitelistedPlayersQuery).use { whitelistedPlayersStmt ->
                whitelistedPlayersStmt.setInt(1, regionId)
                val whitelistedPlayersSet = whitelistedPlayersStmt.executeQuery()
                while (whitelistedPlayersSet.next()) {
                    val uuid = whitelistedPlayersSet.getString("uuid")
                    whitelistedPlayers.add(WPlayer(uuid))
                }
            }

            if (regionName == null || position1 == null || position2 == null) {
                return null
            }

            return Region(regionName, position1, position2, whitelistedPlayers)
        }
    }

    private fun getPositionByType(conn: Connection, regionId: Int, type: Int): Position? {
        val positionQuery = "SELECT * FROM ech_positions WHERE regionId = ? AND type = ?"
        return conn.prepareStatement(positionQuery).use { positionStmt ->
            positionStmt.setInt(1, regionId)
            positionStmt.setInt(2, type)
            positionStmt.executeQuery().use { positionsSet ->
                if (positionsSet.next()) {
                    val world = positionsSet.getString("world")
                    val x = positionsSet.getDouble("x")
                    val y = positionsSet.getDouble("y")
                    val z = positionsSet.getDouble("z")
                    Position(type, world, x, y, z)
                } else null
            }
        }
    }

    override fun deleteRegion(regionId: Int) {

    }

    override fun addWhitelistedPlayer(whiteListedPlayer: WPlayer): WPlayer? {

    }

    override fun removeWhitelistedPlayer(whiteListedPlayer: WPlayer) {

    }

    override fun getWhitelistedPlayers(regionId: Int): List<WPlayer?> {

    }
}