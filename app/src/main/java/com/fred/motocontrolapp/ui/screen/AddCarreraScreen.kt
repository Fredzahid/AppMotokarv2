package com.fred.motocontrolapp.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fred.motocontrolapp.ui.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCarreraScreen(
    navController: NavController,
    viewModel: MainViewModel
) {
    var precio by remember { mutableStateOf("") }
    var destino by remember { mutableStateOf("") }
    var metodoPago by remember { mutableStateOf("Efectivo") }

    val montosRapidos = listOf("2.0", "3.0", "5.0", "10.0")

    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF0F2027),
            Color(0xFF203A43),
            Color(0xFF2C5364)
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nueva Carrera 🛺", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
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
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Campo Principal de Monto
                Text(
                    text = "S/ ${if (precio.isEmpty()) "0.0" else precio}",
                    style = MaterialTheme.typography.displayLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 32.dp)
                )

                // Input invisible pero con teclado numérico activo
                OutlinedTextField(
                    value = precio,
                    onValueChange = { if (it.length <= 5) precio = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.size(0.dp), // Invisible
                    singleLine = true
                )

                Text(
                    "Montos Rápidos",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    montosRapidos.forEach { monto ->
                        MontoRapidoButton(
                            monto = monto,
                            onClick = { precio = monto },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Métodos de Pago
                Text(
                    "Tipo de Pago",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    PaymentMethodButton(
                        label = "Efectivo",
                        icon = Icons.Default.Money,
                        selected = metodoPago == "Efectivo",
                        color = Color(0xFFFFD54F),
                        onClick = { metodoPago = "Efectivo" },
                        modifier = Modifier.weight(1f)
                    )
                    PaymentMethodButton(
                        label = "Yape/Plin",
                        icon = Icons.Default.QrCode,
                        selected = metodoPago == "Yape/Plin",
                        color = Color(0xFF4FC3F7),
                        onClick = { metodoPago = "Yape/Plin" },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Destino (Opcional)
                OutlinedTextField(
                    value = destino,
                    onValueChange = { destino = it },
                    label = { Text("Destino (Opcional)", color = Color.White.copy(alpha = 0.6f)) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.3f)
                    ),
                    shape = RoundedCornerShape(16.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                // Botón Gigante de Acción
                Button(
                    onClick = {
                        if (precio.isNotBlank()) {
                            viewModel.agregarCarrera(
                                origen = "",
                                destino = destino,
                                precio = precio.toDouble(),
                                metodoPago = metodoPago
                            )
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "¡LISTO! ✅",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun MontoRapidoButton(
    monto: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        color = Color.White.copy(alpha = 0.15f),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
    ) {
        Text(
            text = "S/ $monto",
            modifier = Modifier.padding(vertical = 12.dp),
            textAlign = TextAlign.Center,
            color = Color.White,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun PaymentMethodButton(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        color = if (selected) color else Color.White.copy(alpha = 0.1f),
        shape = RoundedCornerShape(20.dp),
        modifier = modifier,
        border = if (selected) null else BorderStroke(1.dp, Color.White.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (selected) Color.Black else Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                color = if (selected) Color.Black else Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
