package com.proiezrush.echregions.database

import java.sql.Connection

interface DatabaseImpl {

    fun connect()
    fun setup()
    fun getConnection(): Connection?
    fun close()

}