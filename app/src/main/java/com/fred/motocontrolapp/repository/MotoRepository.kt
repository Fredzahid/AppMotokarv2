package com.fred.motocontrolapp.repository

import com.fred.motocontrolapp.data.dao.CarreraDao
import com.fred.motocontrolapp.data.dao.GastoDao
import com.fred.motocontrolapp.data.entity.CarreraEntity
import com.fred.motocontrolapp.data.entity.GastoEntity
import kotlinx.coroutines.flow.Flow

class MotoRepository(
    private val carreraDao: CarreraDao,
    private val gastoDao: GastoDao
) {
    val allCarreras: Flow<List<CarreraEntity>> = carreraDao.getAllCarreras()
    val allGastos: Flow<List<GastoEntity>> = gastoDao.getAllGastos()

    suspend fun insertCarrera(carrera: CarreraEntity) {
        carreraDao.insertCarrera(carrera)
    }

    suspend fun insertGasto(gasto: GastoEntity) {
        gastoDao.insertGasto(gasto)
    }

    suspend fun deleteCarrera(carrera: CarreraEntity) {
        carreraDao.deleteCarrera(carrera)
    }

    suspend fun deleteGasto(gasto: GastoEntity) {
        gastoDao.deleteGasto(gasto)
    }
}