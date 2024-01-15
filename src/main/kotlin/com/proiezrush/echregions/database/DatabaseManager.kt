package com.proiezrush.echregions.database

import com.proiezrush.echregions.objects.Position
import com.proiezrush.echregions.objects.Region
import com.proiezrush.echregions.objects.SpatialKey

interface DatabaseManager {

    fun loadRegions(sectorSize: Int = 16) : Pair<MutableMap<SpatialKey, MutableList<Region?>>, Map<String, MutableList<Region?>>>

    fun createRegion(ownerUUID: String, name: String, position1: Position, position2: Position, sectorSize: Int) : Set<SpatialKey>
    fun deleteRegion(uuid: String, name: String)

    fun addWhitelistedPlayer(whitelistedPlayerUUID: String, uuid: String, name: String)
    fun removeWhitelistedPlayer(whitelistedPlayerUUID: String, uuid: String, name: String)

    fun renameRegion(uuid: String, oldName: String, newName: String)

    fun redefineRegionPositions(uuid: String, name: String, position1: Position, position2: Position)

}