package com.fred.motocontrolapp.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fred.motocontrolapp.data.entity.CarreraEntity
import com.fred.motocontrolapp.data.entity.GastoEntity
import com.fred.motocontrolapp.ui.viewmodel.MainViewModel
import com.fred.motocontrolapp.util.DateUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorialScreen(
    navController: NavController,
    viewModel: MainViewModel
) {
    val carreras by viewModel.carreras.collectAsState()
    val gastos by viewModel.gastos.collectAsState()
    
    var filtroSeleccionado by remember { mutableStateOf("Hoy") }

    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF0F2027),
            Color(0xFF203A43),
            Color(0xFF2C5364)
        )
    )

    // Filtrar datos
    val carrerasFiltradas = carreras.filter { 
        when (filtroSeleccionado) {
            "Hoy" -> DateUtils.isToday(it.fecha)
            "Ayer" -> DateUtils.isYesterday(it.fecha)
            "Semana" -> DateUtils.isThisWeek(it.fecha)
            "Mes" -> DateUtils.isThisMonth(it.fecha)
            else -> true
        }
    }
    
    val gastosFiltrados = gastos.filter { 
        when (filtroSeleccionado) {
            "Hoy" -> DateUtils.isToday(it.fecha)
            "Ayer" -> DateUtils.isYesterday(it.fecha)
            "Semana" -> DateUtils.isThisWeek(it.fecha)
            "Mes" -> DateUtils.isThisMonth(it.fecha)
            else -> true
        }
    }

    val totalGanado = carrerasFiltradas.sumOf { it.precio }
    val totalGastos = gastosFiltrados.sumOf { it.monto }
    val limpioParaCasa = totalGanado - totalGastos

    // Combinar y ordenar actividades por fecha
    val actividades = (carrerasFiltradas.map { ActivityItem.Carrera(it) } + 
                      gastosFiltrados.map { ActivityItem.Gasto(it) })
                      .sortedByDescending { it.timestamp }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Historial y Reportes", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().background(gradient).padding(paddingValues)) {
            Column(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)) {
                
                // Filtros Rápidos
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val filtros = listOf("Hoy", "Ayer", "Semana", "Mes")
                    filtros.forEach { filtro ->
                        FilterChip(
                            selected = filtroSeleccionado == filtro,
                            onClick = { filtroSeleccionado = filtro },
                            label = { Text(filtro) },
                            colors = FilterChipDefaults.filterChipColors(
                                labelColor = Color.White.copy(alpha = 0.7f),
                                selectedLabelColor = Color.Black,
                                selectedContainerColor = Color.White
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                borderColor = Color.White.copy(alpha = 0.3f),
                                selectedBorderColor = Color.White,
                                enabled = true,
                                selected = filtroSeleccionado == filtro
                            )
                        )
                    }
                }

                // Tarjetas de Resumen
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SummaryMiniCard("Ganado", totalGanado, Color(0xFFA5D6A7), Modifier.weight(1f)) // Verde más brillante
                    SummaryMiniCard("Inversión", totalGastos, Color(0xFFFF8A80), Modifier.weight(1f)) // Rojo más brillante
                    SummaryMiniCard("Limpio", limpioParaCasa, Color(0xFF90CAF9), Modifier.weight(1f)) // Azul más brillante
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    "Actividades de $filtroSeleccionado",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Lista de Actividades
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 20.dp)
                ) {
                    items(actividades) { actividad ->
                        ActivityRow(actividad)
                    }
                }
            }
        }
    }
}

@Composable
fun SummaryMiniCard(label: String, amount: Double, color: Color, modifier: Modifier = Modifier) {
    Surface(
        color = Color.White.copy(alpha = 0.1f),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(label, style = MaterialTheme.typography.labelSmall, color = Color.White.copy(0.7f))
            Text("S/ $amount", style = MaterialTheme.typography.titleSmall, color = color, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ActivityRow(actividad: ActivityItem) {
    val (icon, color, label, amount, time) = when (actividad) {
        is ActivityItem.Carrera -> {
            val c = actividad.carrera
            Quint(Icons.Default.DirectionsBike, Color(0xFFA5D6A7), "Carrera", "S/ ${c.precio}", DateUtils.formatTime(c.fecha))
        }
        is ActivityItem.Gasto -> {
            val g = actividad.gasto
            val gIcon = when(g.tipo) {
                "Combustible" -> Icons.Default.LocalGasStation
                "Taller" -> Icons.Default.Build
                "Alquiler" -> Icons.Default.Home
                "Comida" -> Icons.Default.Restaurant
                else -> Icons.Default.DirectionsBike
            }
            Quint(gIcon, Color(0xFFFF8A80), g.tipo, "S/ ${g.monto}", DateUtils.formatTime(g.fecha))
        }
    }

    Surface(
        color = Color.White.copy(alpha = 0.05f),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = color.copy(alpha = 0.2f),
                shape = CircleShape,
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(label, style = MaterialTheme.typography.bodyLarge, color = Color.White, fontWeight = FontWeight.Medium)
                Text(time, style = MaterialTheme.typography.labelSmall, color = Color.White.copy(0.5f))
            }
            
            Text(amount, style = MaterialTheme.typography.titleMedium, color = color, fontWeight = FontWeight.Bold)
        }
    }
}

sealed class ActivityItem {
    abstract val timestamp: Long
    data class Carrera(val carrera: CarreraEntity) : ActivityItem() {
        override val timestamp = carrera.fecha
    }
    data class Gasto(val gasto: GastoEntity) : ActivityItem() {
        override val timestamp = gasto.fecha
    }
}

data class Quint<A, B, C, D, E>(val first: A, val second: B, val third: C, val fourth: D, val fifth: E)
