package com.proiezrush.echregions.database.mysql

import com.proiezrush.echregions.database.DatabaseManager
import com.proiezrush.echregions.objects.Position
import com.proiezrush.echregions.objects.Region
import com.proiezrush.echregions.objects.SpatialKey
import com.proiezrush.echregions.objects.WPlayer
import com.proiezrush.echregions.utils.MessageUtils
import java.sql.Connection
import java.sql.Statement

class MySQLDatabaseManagerImpl(private val connection: Connection) : DatabaseManager {

    override fun loadRegions(sectorSize: Int): Pair<Map<SpatialKey, MutableList<Region?>>, Map<String, MutableList<Region?>>> {
        val regionsBySpatialKey = mutableMapOf<SpatialKey, MutableList<Region?>>()
        val regionsByUUID = mutableMapOf<String, MutableList<Region?>>()

        this.connection.let { conn ->
            val whitelistedPlayers = mutableMapOf<String, WPlayer>()

            // Get region
            val regionsQuery = "SELECT * FROM ech_regions"
            val regionsData = conn.prepareStatement(regionsQuery).use { regionStmt ->
                regionStmt.executeQuery().use { regionSet ->
                    val regionsData = mutableMapOf<Int, MutableMap<String, Any>>()
                    while (regionSet.next()) {
                        val regionId = regionSet.getInt("id")
                        val regionName = regionSet.getString("name")

                        val regionData = mutableMapOf<String, Any>()
                        regionData["id"] = regionId
                        regionData["ownerUUID"] = regionSet.getString("ownerUUID")
                        regionData["name"] = regionName
                        regionsData[regionId] = regionData
                    }
                    regionsData
                }
            }

            // Process all regions
            for (regionData in regionsData.values) {
                val regionId = regionData["id"] as Int

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
                        whitelistedPlayers[uuid] = WPlayer(uuid)
                    }
                }

                val ownerUUID = regionData["ownerUUID"] as String
                val regionName = regionData["name"] as String
                if (position1 == null || position2 == null) {
                    continue
                }

                val region = Region(ownerUUID, regionName, position1, position2, whitelistedPlayers)
                regionsByUUID.getOrPut(ownerUUID) { mutableListOf() }.add(region)

                val keys = determineSpatialKeysForRegion(region, sectorSize)
                keys.forEach { key ->
                    regionsBySpatialKey.getOrPut(key) { mutableListOf() }.add(region)
                }
            }
        }

        return Pair(regionsBySpatialKey, regionsByUUID)
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

    private fun determineSpatialKeysForRegion(region: Region, sectorSize: Int): Set<SpatialKey> {
        val keys = mutableSetOf<SpatialKey>()

        val minX = (minOf(region.position1.x, region.position2.x) / sectorSize).toInt()
        val maxX = (maxOf(region.position1.x, region.position2.x) / sectorSize).toInt()
        val minY = (minOf(region.position1.y, region.position2.y) / sectorSize).toInt()
        val maxY = (maxOf(region.position1.y, region.position2.y) / sectorSize).toInt()
        val minZ = (minOf(region.position1.z, region.position2.z) / sectorSize).toInt()
        val maxZ = (maxOf(region.position1.z, region.position2.z) / sectorSize).toInt()

        for (x in minX..maxX) {
            for (y in minY..maxY) {
                for (z in minZ..maxZ) {
                    keys.add(SpatialKey(x, y, z))
                }
            }
        }

        return keys
    }

    override fun saveRegions(regions: Map<SpatialKey, MutableList<Region?>>) {

    }

    override fun createRegion(ownerUUID: String, name: String, position1: Position, position2: Position): Region {
        val region = Region(ownerUUID, name, position1, position2, mutableMapOf())

        this.connection.let { conn ->
            val insertRegionSql = "INSERT INTO ech_regions (ownerUUID, name) VALUES (?, ?)"
            val insertPositionSql = "INSERT INTO ech_positions (regionId, type, world, x, y, z) VALUES (?, ?, ?, ?, ?, ?)"

            conn.prepareStatement(insertRegionSql, Statement.RETURN_GENERATED_KEYS).use { insertRegionStmt ->
                insertRegionStmt.setString(1, region.ownerUUID)
                insertRegionStmt.setString(2, region.name)
                insertRegionStmt.executeUpdate()

                val generatedKeys = insertRegionStmt.generatedKeys
                if (generatedKeys.next()) {
                    val regionId = generatedKeys.getInt(1)

                    conn.prepareStatement(insertPositionSql).use { insertPositionStmt ->
                        insertPositionStmt.setInt(1, regionId)
                        insertPositionStmt.setInt(2, 1)
                        insertPositionStmt.setString(3, position1.world)
                        insertPositionStmt.setDouble(4, position1.x)
                        insertPositionStmt.setDouble(5, position1.y)
                        insertPositionStmt.setDouble(6, position1.z)
                        insertPositionStmt.executeUpdate()
                    }

                    conn.prepareStatement(insertPositionSql).use { insertPositionStmt ->
                        insertPositionStmt.setInt(1, regionId)
                        insertPositionStmt.setInt(2, 2)
                        insertPositionStmt.setString(3, position2.world)
                        insertPositionStmt.setDouble(4, position2.x)
                        insertPositionStmt.setDouble(5, position2.y)
                        insertPositionStmt.setDouble(6, position2.z)
                        insertPositionStmt.executeUpdate()
                    }
                }
            }
        }

        return region
    }

    override fun deleteRegion(regionId: Int) {

    }

    override fun addWhitelistedPlayer(whiteListedPlayer: WPlayer): WPlayer? {
        return null
    }

    override fun removeWhitelistedPlayer(whiteListedPlayer: WPlayer) {

    }
}