package com.fred.motocontrolapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "carreras")
data class CarreraEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val origen: String = "",
    val destino: String = "",
    val precio: Double,
    val metodoPago: String = "Efectivo",
    val fecha: Long = System.currentTimeMillis()
)
