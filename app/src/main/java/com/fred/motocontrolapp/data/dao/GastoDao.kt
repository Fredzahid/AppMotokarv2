package com.fred.motocontrolapp.data.dao

import androidx.room.*
import com.fred.motocontrolapp.data.entity.GastoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GastoDao {
    @Query("SELECT * FROM gastos ORDER BY fecha DESC")
    fun getAllGastos(): Flow<List<GastoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGasto(gasto: GastoEntity)

    @Delete
    suspend fun deleteGasto(gasto: GastoEntity)
}