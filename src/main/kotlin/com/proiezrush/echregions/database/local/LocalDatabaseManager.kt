package com.proiezrush.echregions.database.local

import com.proiezrush.echregions.objects.Position
import com.proiezrush.echregions.objects.Region
import com.proiezrush.echregions.objects.SpatialKey
import com.proiezrush.echregions.utils.MessageUtils
import org.bukkit.entity.Player

class LocalDatabaseManager {

    var regionsByUUID = emptyMap<String, MutableList<Region?>>()

    fun addRegion(uuid: String, region: Region) {
        regionsByUUID[uuid]?.add(region)
    }

    fun removeRegion(uuid: String, region: Region) {
        regionsByUUID[uuid]?.remove(region)
    }

    fun searchRegionByName(uuid: String, name: String): Region? {
        return regionsByUUID[uuid]?.firstOrNull { it?.name == name }
    }

    var spatialRegions = emptyMap<SpatialKey, MutableList<Region?>>()

    fun getPlayerRegions(uuid: String): List<Region?> {
        return regionsByUUID[uuid] ?: emptyList()
    }

    private var playerSelectedPositions = mutableMapOf<String, MutableList<Position>>()

    fun addPlayerPosition(uuid: String, position: Position) {
        playerSelectedPositions.computeIfAbsent(uuid) { mutableListOf() }.add(position)
    }

    fun clearPlayerPositions(uuid: String) {
        playerSelectedPositions.remove(uuid)
    }

    fun getPlayerFirstPosition(uuid: String): Position? {
        return playerSelectedPositions[uuid]?.firstOrNull { it.type == 1 }
    }

    fun getPlayerSecondPosition(uuid: String): Position? {
        return playerSelectedPositions[uuid]?.firstOrNull { it.type == 2 }
    }
}