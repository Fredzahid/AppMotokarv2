package com.fred.motocontrolapp.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gastos")
data class GastoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tipo: String,
    val monto: Double,
    val fecha: Long = System.currentTimeMillis()
)