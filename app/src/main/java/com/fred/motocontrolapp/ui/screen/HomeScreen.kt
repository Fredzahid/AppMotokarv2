package com.fred.motocontrolapp.ui.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fred.motocontrolapp.ui.viewmodel.MainViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: MainViewModel
) {
    val carreras by viewModel.carreras.collectAsState()
    val gastos by viewModel.gastos.collectAsState()
    val metaDiaria by viewModel.metaDiaria.collectAsState()

    val totalIngresos = carreras.sumOf { it.precio }
    val totalGastos = gastos.sumOf { it.monto }
    val ganancia = totalIngresos - totalGastos
    val progreso = (totalIngresos / metaDiaria).coerceIn(0.0, 1.0).toFloat()

    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF0F2027),
            Color(0xFF203A43),
            Color(0xFF2C5364)
        )
    )

    val (motivationMessage, motivationColor) = when {
        progreso >= 1f -> "¡Meta cumplida! Eres un crack 🏆" to Color(0xFFFDD835)
        ganancia > 0 -> "¡Buen trabajo hoy! 💪" to Color(0xFF81C784)
        ganancia < 0 -> "Cuidado con los gastos ⚠️" to Color(0xFFE57373)
        totalIngresos == 0.0 -> "Registra tu primera carrera 🚀" to Color(0xFF64B5F6)
        else -> "Día equilibrado ⚖️" to Color.White
    }

    val smartAdvice = when {
        totalIngresos == 0.0 -> "Empieza tu primera carrera para ver estadísticas 🚀"
        totalGastos > totalIngresos * 0.4 -> "Tus gastos superan el 40% de tus ingresos. ¡Ojo ahí! ⚠️"
        ganancia > metaDiaria * 0.5 -> "Excelente rendimiento, vas por muy buen camino 💪"
        else -> "Sigue así, cada carrera cuenta para tu meta 🏁"
    }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF1B2D36),
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.DirectionsBike, contentDescription = "Carrera") },
                    label = { Text("Carrera") },
                    selected = false,
                    onClick = { navController.navigate("add_carrera") },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = Color.White,
                        unselectedTextColor = Color.White,
                        indicatorColor = Color(0xFF2C5364)
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.AttachMoney, contentDescription = "Gasto") },
                    label = { Text("Gasto") },
                    selected = false,
                    onClick = { navController.navigate("add_gasto") },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = Color.White,
                        unselectedTextColor = Color.White,
                        indicatorColor = Color(0xFF2C5364)
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.List, contentDescription = "Historial") },
                    label = { Text("Historial") },
                    selected = false,
                    onClick = { navController.navigate("historial") },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = Color.White,
                        unselectedTextColor = Color.White,
                        indicatorColor = Color(0xFF2C5364)
                    )
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(gradient)
        ) {
            // Icono de fondo sutil
            Icon(
                imageVector = Icons.Default.DirectionsBike,
                contentDescription = null,
                modifier = Modifier
                    .size(300.dp)
                    .align(Alignment.BottomEnd)
                    .offset(x = 80.dp, y = 80.dp)
                    .graphicsLayer(alpha = 0.05f),
                tint = Color.White
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    "MotoControl",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = motivationMessage,
                    color = motivationColor,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(30.dp))

                // Tarjeta de Meta del Día
                Card(
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f)),
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "🎯 Meta del Día",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White
                            )
                            Text(
                                "S/ $metaDiaria",
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        LinearProgressIndicator(
                            progress = { progreso },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(12.dp),
                            color = if (progreso >= 1f) Color(0xFFFDD835) else Color(0xFF64B5F6),
                            trackColor = Color.White.copy(alpha = 0.2f),
                            strokeCap = StrokeCap.Round
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Progreso: S/ $totalIngresos (${(progreso * 100).toInt()}%)",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Mini Gráfico y Resumen
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Mini Gráfico
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f)),
                        modifier = Modifier
                            .weight(0.4f)
                            .height(160.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Balance", style = MaterialTheme.typography.labelMedium, color = Color.White.copy(0.7f))
                            Spacer(modifier = Modifier.weight(1f))
                            MiniBarChart(
                                ingresos = totalIngresos.toFloat(),
                                gastos = totalGastos.toFloat()
                            )
                        }
                    }

                    // Tarjeta de Ganancia Principal
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD).copy(alpha = 0.95f)),
                        modifier = Modifier
                            .weight(0.6f)
                            .height(160.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(Icons.Default.Analytics, contentDescription = null, tint = Color(0xFF1565C0))
                            Text("Ganancia", style = MaterialTheme.typography.titleMedium, color = Color(0xFF1565C0).copy(0.7f))
                            Text(
                                "S/ $ganancia",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF1565C0)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Tarjetas de Ingresos y Gastos (Versión reducida)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    CompactSummaryCard(
                        title = "Ingresos",
                        amount = totalIngresos,
                        containerColor = Color(0xFFE8F5E9).copy(alpha = 0.95f),
                        contentColor = Color(0xFF2E7D32),
                        modifier = Modifier.weight(1f)
                    )
                    CompactSummaryCard(
                        title = "Gastos",
                        amount = totalGastos,
                        containerColor = Color(0xFFFFEBEE).copy(alpha = 0.95f),
                        contentColor = Color(0xFFC62828),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Consejo Inteligente
                Surface(
                    color = Color.White.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Lightbulb, contentDescription = null, tint = Color(0xFFFDD835))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = smartAdvice,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    "Estadísticas Rápidas",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    StatBox(
                        label = "Carreras",
                        value = "${carreras.size}",
                        containerColor = Color.White.copy(alpha = 0.15f),
                        modifier = Modifier.weight(1f)
                    )
                    StatBox(
                        label = "Gastos",
                        value = "${gastos.size}",
                        containerColor = Color.White.copy(alpha = 0.15f),
                        modifier = Modifier.weight(1f)
                    )
                }
                
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun MiniBarChart(ingresos: Float, gastos: Float) {
    val maxVal = maxOf(ingresos, gastos, 1f)
    val hIngresos = (ingresos / maxVal).coerceIn(0.1f, 1f)
    val hGastos = (gastos / maxVal).coerceIn(0.1f, 1f)

    Canvas(modifier = Modifier
        .fillMaxWidth()
        .height(80.dp)) {
        val barWidth = size.width * 0.25f
        val gap = size.width * 0.15f
        
        // Barra Ingresos
        drawRoundRect(
            color = Color(0xFF81C784),
            topLeft = Offset(size.width * 0.2f, size.height * (1 - hIngresos)),
            size = Size(barWidth, size.height * hIngresos),
            cornerRadius = CornerRadius(4.dp.toPx())
        )

        // Barra Gastos
        drawRoundRect(
            color = Color(0xFFE57373),
            topLeft = Offset(size.width * 0.2f + barWidth + gap, size.height * (1 - hGastos)),
            size = Size(barWidth, size.height * hGastos),
            cornerRadius = CornerRadius(4.dp.toPx())
        )
    }
}

@Composable
fun CompactSummaryCard(
    title: String,
    amount: Double,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = title, style = MaterialTheme.typography.labelLarge, color = contentColor.copy(0.7f))
            Text(
                text = "S/ $amount",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = contentColor
            )
        }
    }
}

@Composable
fun StatBox(
    label: String,
    value: String,
    containerColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        color = containerColor,
        shape = RoundedCornerShape(24.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.7f)
            )
        }
    }
}
