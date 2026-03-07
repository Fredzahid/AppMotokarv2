package com.fred.motocontrolapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.fred.motocontrolapp.data.AppDatabase
import com.fred.motocontrolapp.data.entity.CarreraEntity
import com.fred.motocontrolapp.data.entity.GastoEntity
import com.fred.motocontrolapp.repository.MotoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MotoRepository

    val carreras: StateFlow<List<CarreraEntity>>
    val gastos: StateFlow<List<GastoEntity>>
    
    // Meta diaria (por defecto 100)
    private val _metaDiaria = MutableStateFlow(100.0)
    val metaDiaria: StateFlow<Double> = _metaDiaria

    init {
        val db = AppDatabase.getDatabase(application)
        repository = MotoRepository(db.carreraDao(), db.gastoDao())
        
        carreras = repository.allCarreras.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
        
        gastos = repository.allGastos.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    fun setMetaDiaria(nuevaMeta: Double) {
        _metaDiaria.value = nuevaMeta
    }

    fun agregarCarrera(origen: String, destino: String, precio: Double, metodoPago: String) {
        viewModelScope.launch {
            repository.insertCarrera(
                CarreraEntity(
                    origen = origen, 
                    destino = destino, 
                    precio = precio,
                    metodoPago = metodoPago
                )
            )
        }
    }

    fun agregarGasto(tipo: String, monto: Double) {
        viewModelScope.launch {
            repository.insertGasto(
                GastoEntity(tipo = tipo, monto = monto)
            )
        }
    }

    fun eliminarCarrera(carrera: CarreraEntity) {
        viewModelScope.launch {
            repository.deleteCarrera(carrera)
        }
    }

    fun eliminarGasto(gasto: GastoEntity) {
        viewModelScope.launch {
            repository.deleteGasto(gasto)
        }
    }
}
