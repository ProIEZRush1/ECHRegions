package com.proiezrush.echregions.database.mysql

import com.proiezrush.echregions.database.DatabaseManager
import com.proiezrush.echregions.objects.Position
import com.proiezrush.echregions.objects.Region
import com.proiezrush.echregions.objects.SpatialKey
import java.sql.Connection
import java.sql.Statement

class MySQLDatabaseManagerImpl(private val connection: Connection) : DatabaseManager {

    override fun loadRegions(sectorSize: Int): Pair<MutableMap<SpatialKey, MutableList<Region?>>, Map<String, MutableList<Region?>>> {
        val regionsBySpatialKey = mutableMapOf<SpatialKey, MutableList<Region?>>()
        val regionsByUUID = mutableMapOf<String, MutableList<Region?>>()

        this.connection.let { conn ->
            val whitelistedPlayers = mutableListOf<String>()

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
                        whitelistedPlayers.add(uuid)
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

    override fun createRegion(ownerUUID: String, name: String, position1: Position, position2: Position, sectorSize: Int): Set<SpatialKey> {
        val region = Region(ownerUUID, name, position1, position2, mutableListOf())

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

        val keys = determineSpatialKeysForRegion(region, sectorSize)
        keys.forEach { _ ->
            return keys
        }

        return emptySet()
    }

    override fun deleteRegion(uuid: String, name: String) {
        val regionId = getRegionID(uuid, name)

        this.connection.let { conn ->
            val deleteRegionSql = "DELETE FROM ech_regions WHERE id = ?"

            conn.prepareStatement(deleteRegionSql).use { deleteRegionStmt ->
                deleteRegionStmt.setInt(1, regionId)
                deleteRegionStmt.executeUpdate()
            }

            val deletePositionsSql = "DELETE FROM ech_positions WHERE regionId = ?"

            conn.prepareStatement(deletePositionsSql).use { deletePositionsStmt ->
                deletePositionsStmt.setInt(1, regionId)
                deletePositionsStmt.executeUpdate()
            }

            val deleteWhitelistedPlayersSql = "DELETE FROM ech_whitelisted_players WHERE regionId = ?"

            conn.prepareStatement(deleteWhitelistedPlayersSql).use { deleteWhitelistedPlayersStmt ->
                deleteWhitelistedPlayersStmt.setInt(1, regionId)
                deleteWhitelistedPlayersStmt.executeUpdate()
            }
        }
    }

    override fun addWhitelistedPlayer(whitelistedPlayerUUID: String, uuid: String, name: String) {
        this.connection.let { conn ->
            val insertWhitelistedPlayerSql = "INSERT INTO ech_whitelisted_players (regionId, uuid) VALUES (?, ?)"

            conn.prepareStatement(insertWhitelistedPlayerSql).use { insertWhitelistedPlayerStmt ->
                insertWhitelistedPlayerStmt.setInt(1, getRegionID(uuid, name))
                insertWhitelistedPlayerStmt.setString(2, whitelistedPlayerUUID)
                insertWhitelistedPlayerStmt.executeUpdate()
            }
        }
    }

    override fun removeWhitelistedPlayer(whitelistedPlayerUUID: String, uuid: String, name: String) {
        this.connection.let { conn ->
            val deleteWhitelistedPlayerSql = "DELETE FROM ech_whitelisted_players WHERE regionId = ? AND uuid = ?"

            conn.prepareStatement(deleteWhitelistedPlayerSql).use { deleteWhitelistedPlayerStmt ->
                deleteWhitelistedPlayerStmt.setInt(1, getRegionID(uuid, name))
                deleteWhitelistedPlayerStmt.setString(2, whitelistedPlayerUUID)
                deleteWhitelistedPlayerStmt.executeUpdate()
            }
        }
    }

    override fun renameRegion(uuid: String, oldName: String, newName: String) {
        this.connection.let { conn ->
            val updateRegionSql = "UPDATE ech_regions SET name = ? WHERE id = ?"

            conn.prepareStatement(updateRegionSql).use { updateRegionStmt ->
                updateRegionStmt.setString(1, newName)
                updateRegionStmt.setInt(2, getRegionID(uuid, oldName))
                updateRegionStmt.executeUpdate()
            }
        }
    }

    override fun redefineRegionPositions(uuid: String, name: String, position1: Position, position2: Position) {
        val regionId = getRegionID(uuid, name)

        this.connection.let { conn ->
            val updatePositionSql =
                "UPDATE ech_positions SET world = ?, x = ?, y = ?, z = ? WHERE regionId = ? AND type = ?"

            conn.prepareStatement(updatePositionSql).use { updatePositionStmt ->
                updatePositionStmt.setString(1, position1.world)
                updatePositionStmt.setDouble(2, position1.x)
                updatePositionStmt.setDouble(3, position1.y)
                updatePositionStmt.setDouble(4, position1.z)
                updatePositionStmt.setInt(5, regionId)
                updatePositionStmt.setInt(6, 1)
                updatePositionStmt.executeUpdate()

                updatePositionStmt.setString(1, position2.world)
                updatePositionStmt.setDouble(2, position2.x)
                updatePositionStmt.setDouble(3, position2.y)
                updatePositionStmt.setDouble(4, position2.z)
                updatePositionStmt.setInt(5, regionId)
                updatePositionStmt.setInt(6, 2)
                updatePositionStmt.executeUpdate()
            }
        }
    }

    private fun getRegionID(uuid: String, name: String): Int {
        this.connection.let { conn ->
            val getRegionIDSql = "SELECT id FROM ech_regions WHERE ownerUUID = ? AND name = ?"

            conn.prepareStatement(getRegionIDSql).use { getRegionIDStmt ->
                getRegionIDStmt.setString(1, uuid)
                getRegionIDStmt.setString(2, name)
                val resultSet = getRegionIDStmt.executeQuery()

                if (resultSet.next()) {
                    return resultSet.getInt("id")
                }
            }
        }
        return -1
    }
}