package com.proiezrush.echregions.database.mysql

import com.proiezrush.echregions.database.DatabaseManagerImpl
import com.proiezrush.echregions.objects.Position
import com.proiezrush.echregions.objects.Region
import com.proiezrush.echregions.objects.WPlayer
import java.sql.Connection
import java.sql.Statement

class MySQLDatabaseManager(private val connection: Connection) : DatabaseManagerImpl {
    override fun createRegion(name: String, position1: Position, position2: Position): Region? {
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
            val stmt = conn.createStatement()

            var regionName: String? = null

            // First get region, then get positions, then get whitelisted players
            val regionSet = stmt.executeQuery("SELECT * FROM ech_regions WHERE id = $regionId")
            while (regionSet.next()) {
                regionName = regionSet.getString("name")
            }

            var position1: Position? = null
            var position2: Position? = null

            val positionsSet = stmt.executeQuery("SELECT * FROM ech_positions WHERE regionId = $regionId")
            while (positionsSet.next()) {
                val positionType = positionsSet.getInt("type")
                val world = positionsSet.getString("world")
                val x = positionsSet.getDouble("x")
                val y = positionsSet.getDouble("y")
                val z = positionsSet.getDouble("z")

                if (positionType == 1) {
                    position1 = Position(1, world, x, y, z)
                }
                else if (positionType == 2) {
                    position2 = Position(2, world, x, y, z)

                }
            }

            val whitelistedPlayers = mutableListOf<WPlayer>()

            val whitelistedPlayersSet = stmt.executeQuery("SELECT * FROM ech_whitelisted_players WHERE regionId = $regionId")
            while (whitelistedPlayersSet.next()) {
                val uuid = whitelistedPlayersSet.getString("uuid")

                whitelistedPlayers.add(WPlayer(uuid))
            }

            if (regionName == null || position1 == null || position2 == null) {
                return null
            }

            return Region(regionName, position1, position2, whitelistedPlayers)
        }
    }

    override fun deleteRegion(regionId: Int) {
        TODO("Not yet implemented")
    }

    override fun addWhitelistedPlayer(whiteListedPlayer: WPlayer): WPlayer? {
        TODO("Not yet implemented")
    }

    override fun removeWhitelistedPlayer(whiteListedPlayer: WPlayer) {
        TODO("Not yet implemented")
    }

    override fun getWhitelistedPlayers(regionId: Int): List<WPlayer?> {
        TODO("Not yet implemented")
    }
}