package com.proiezrush.echregions.utils

import com.proiezrush.echregions.objects.Position
import com.proiezrush.echregions.objects.Region
import com.proiezrush.echregions.objects.SpatialKey

class RegionUtils {

    companion object {
        fun findPlayerRegion(regionsBySpatialKey: Map<SpatialKey, MutableList<Region?>>, sectorSize: Int, playerPosition: Position): Region? {
            val playerKey = SpatialKey(
                (playerPosition.x / sectorSize).toInt(),
                (playerPosition.y / sectorSize).toInt(),
                (playerPosition.z / sectorSize).toInt()
            )

            regionsBySpatialKey[playerKey]?.forEach { region ->
                if (isPositionInsideRegion(playerPosition, region!!)) {
                    return region
                }
            }

            return null
        }

        private fun isPositionInsideRegion(position: Position, region: Region): Boolean {
            // Check world names, p1 and p2 will always be in the same world
            if (position.world != region.position1.world) {
                return false
            }

            return position.x in minOf(region.position1.x, region.position2.x)..maxOf(region.position1.x, region.position2.x) &&
                    position.y in minOf(region.position1.y, region.position2.y)..maxOf(region.position1.y, region.position2.y) &&
                    position.z in minOf(region.position1.z, region.position2.z)..maxOf(region.position1.z, region.position2.z)
        }
    }

}