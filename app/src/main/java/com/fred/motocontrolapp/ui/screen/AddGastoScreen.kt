package com.fred.motocontrolapp.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fred.motocontrolapp.ui.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddGastoScreen(
    navController: NavController,
    viewModel: MainViewModel
) {
    var monto by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("Combustible") }
    var showError by remember { mutableStateOf(false) }
    var isErrorActive by remember { mutableStateOf(false) }
    
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()
    val focusRequester = remember { FocusRequester() }

    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF0F2027),
            Color(0xFF203A43),
            Color(0xFF2C5364)
        )
    )
    
    val isMontoValid = try {
        monto.isNotBlank() && monto.toDouble() > 0
    } catch (e: Exception) {
        false
    }

    val amountTextColor by animateColorAsState(
        targetValue = if (isErrorActive) Color.Red else Color.White,
        animationSpec = tween(durationMillis = 300)
    )

    LaunchedEffect(Unit) {
        delay(300)
        focusRequester.requestFocus()
    }

    val suggestionLabel = when (categoria) {
        "Combustible" -> "Llenar Tanque"
        "Comida" -> "Pagar Menú"
        "Taller" -> "Pagar Reparación"
        "Alquiler" -> "Pagar Diario"
        else -> "Gasto"
    }
    
    val suggestionIcon = when (categoria) {
        "Combustible" -> Icons.Default.LocalGasStation
        "Comida" -> Icons.Default.Restaurant
        "Taller" -> Icons.Default.Build
        "Alquiler" -> Icons.Default.Home
        else -> Icons.Default.Lightbulb
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuevo Gasto 📉", color = Color.White) },
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.weight(1f))

                // Área de Edición Manual del Monto (CENTRO)
                Row(
                    modifier = Modifier.padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "S/ ",
                        style = MaterialTheme.typography.displayLarge,
                        color = amountTextColor,
                        fontWeight = FontWeight.Bold
                    )
                    Box(contentAlignment = Alignment.CenterStart) {
                        if (monto.isEmpty()) {
                            Text(
                                text = "0.0",
                                style = MaterialTheme.typography.displayLarge,
                                color = amountTextColor.copy(alpha = 0.3f),
                                fontWeight = FontWeight.Bold
                            )
                        }
                        BasicTextField(
                            value = monto,
                            onValueChange = { if (it.length <= 6) monto = it },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.focusRequester(focusRequester),
                            textStyle = TextStyle(
                                fontSize = 57.sp,
                                color = amountTextColor,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Start
                            ),
                            cursorBrush = SolidColor(Color.White),
                            singleLine = true
                        )
                    }
                }

                // Burbuja Dinámica
                Surface(
                    color = Color.White.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(suggestionIcon, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "$suggestionLabel (S/ ${if (monto.isEmpty()) "0.0" else monto})",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(48.dp)) // Espacio ajustado para coincidir con la jerarquía visual

                Text(
                    "Selecciona Categoría",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Start
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Selector de Categorías
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        CategoryButton(
                            label = "Combustible",
                            icon = Icons.Default.LocalGasStation,
                            selected = categoria == "Combustible",
                            onClick = { categoria = "Combustible" },
                            modifier = Modifier.weight(1f)
                        )
                        CategoryButton(
                            label = "Taller",
                            icon = Icons.Default.Build,
                            selected = categoria == "Taller",
                            onClick = { categoria = "Taller" },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        CategoryButton(
                            label = "Alquiler",
                            icon = Icons.Default.Home,
                            selected = categoria == "Alquiler",
                            onClick = { categoria = "Alquiler" },
                            modifier = Modifier.weight(1f)
                        )
                        CategoryButton(
                            label = "Comida",
                            icon = Icons.Default.Restaurant,
                            selected = categoria == "Comida",
                            onClick = { categoria = "Comida" },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1.2f))

                // Botón GUARDAR
                Button(
                    onClick = {
                        if (isMontoValid) {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            viewModel.agregarGasto(
                                tipo = categoria,
                                monto = monto.toDouble()
                            )
                            navController.popBackStack()
                        } else {
                            scope.launch {
                                isErrorActive = true
                                showError = true
                                haptic.performHapticFeedback(HapticFeedbackType.Reject)
                                delay(500)
                                isErrorActive = false
                                delay(1500)
                                showError = false
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isMontoValid) Color(0xFFE57373) else Color.Gray.copy(alpha = 0.4f),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Icon(Icons.Default.Save, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "GUARDAR GASTO 📉",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Mensaje de Error Superior
            AnimatedVisibility(
                visible = showError,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 8.dp)
            ) {
                Surface(
                    color = Color.Red.copy(alpha = 0.9f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.padding(horizontal = 24.dp)
                ) {
                    Text(
                        text = "⚠️ Ingresa un monto para guardar",
                        color = Color.White,
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryButton(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        color = if (selected) Color(0xFFE57373) else Color.White.copy(alpha = 0.1f),
        shape = RoundedCornerShape(20.dp),
        modifier = modifier,
        border = if (selected) null else BorderStroke(1.dp, Color.White.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (selected) Color.Black else Color.White,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = label,
                color = if (selected) Color.Black else Color.White,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
