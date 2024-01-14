package com.proiezrush.echregions.database

import com.proiezrush.echregions.objects.Position
import com.proiezrush.echregions.objects.Region
import com.proiezrush.echregions.objects.SpatialKey
import com.proiezrush.echregions.objects.WPlayer

interface DatabaseManager {

    fun loadRegions(sectorSize: Int = 16) : Pair<Map<SpatialKey, MutableList<Region?>>, Map<String, MutableList<Region?>>>
    fun saveRegions(regions: Map<SpatialKey, MutableList<Region?>>)

    fun createRegion(ownerUUID: String, name: String, position1: Position, position2: Position) : Region?
    fun deleteRegion(regionId: Int)

    fun addWhitelistedPlayer(whiteListedPlayer: WPlayer) : WPlayer?
    fun removeWhitelistedPlayer(whiteListedPlayer: WPlayer)

}