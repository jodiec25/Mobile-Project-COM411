package com.example.watertracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.watertracker.model.WaterUnit

/**
 * Quick logging section displaying interactive shortcut cards (+Glass, +Bottle, +Flask, Custom).
 *
 * Formats standard intake values dynamically according to the active [WaterUnit].
 *
 * @param unit Active measurement unit system.
 * @param onAddWater Callback triggered when a quick log button is pressed with (amountInMl, label).
 */
@Composable
fun QuickLogSection(
    unit: WaterUnit = WaterUnit.METRIC,
    onAddWater: (Int, String) -> Unit
) {
    var showCustomDialog by remember { mutableStateOf(false) }

    val preset1Val = if (unit == WaterUnit.METRIC) 250 else 8
    val preset1Ml = unit.toMl(preset1Val)

    val preset2Val = if (unit == WaterUnit.METRIC) 500 else 17
    val preset2Ml = unit.toMl(preset2Val)

    val preset3Val = if (unit == WaterUnit.METRIC) 750 else 25
    val preset3Ml = unit.toMl(preset3Val)

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Quick Log",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )
            Text(
                text = "Tap to record",
                fontSize = 12.sp,
                color = Color(0xFF0284C7),
                fontWeight = FontWeight.SemiBold
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            QuickLogCard(
                modifier = Modifier.weight(1f),
                amountStr = "+$preset1Val",
                label = "Glass",
                icon = Icons.Default.LocalDrink,
                bgColor = Color(0xFFE0F2FE),
                iconColor = Color(0xFF0284C7),
                onClick = { onAddWater(preset1Ml, "Glass") }
            )
            QuickLogCard(
                modifier = Modifier.weight(1f),
                amountStr = "+$preset2Val",
                label = "Bottle",
                icon = Icons.Default.WaterDrop,
                bgColor = Color(0xFFE0F2FE),
                iconColor = Color(0xFF0284C7),
                onClick = { onAddWater(preset2Ml, "Bottle") }
            )
            QuickLogCard(
                modifier = Modifier.weight(1f),
                amountStr = "+$preset3Val",
                label = "Flask",
                icon = Icons.Default.Thermostat,
                bgColor = Color(0xFFE0F2FE),
                iconColor = Color(0xFF0284C7),
                onClick = { onAddWater(preset3Ml, "Flask") }
            )
            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable { showCustomDialog = true },
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0369A1))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Custom",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                    Text(
                        text = "Custom",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Drinks",
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }

    if (showCustomDialog) {
        CustomWaterInputDialog(
            unit = unit,
            onDismiss = { showCustomDialog = false },
            onConfirm = { amountInMl, label ->
                onAddWater(amountInMl, label)
                showCustomDialog = false
            }
        )
    }
}

/**
 * Reusable card button component for quick log volume options.
 */
@Composable
fun QuickLogCard(
    modifier: Modifier = Modifier,
    amountStr: String,
    label: String,
    icon: ImageVector,
    bgColor: Color,
    iconColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(bgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = iconColor,
                    modifier = Modifier.size(18.dp)
                )
            }
            Text(
                text = amountStr,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )
            Text(
                text = label,
                fontSize = 11.sp,
                color = Color(0xFF64748B)
            )
        }
    }
}

/**
 * Dialog for entering a custom water intake volume.
 * Strictly enforces range of 1 to 5000 (ml) or equivalent in fl oz. Zero is prohibited.
 */
@Composable
fun CustomWaterInputDialog(
    unit: WaterUnit,
    onDismiss: () -> Unit,
    onConfirm: (amountInMl: Int, label: String) -> Unit
) {
    var inputStr by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val maxLimit = if (unit == WaterUnit.METRIC) 5000 else 169

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Log Custom Intake",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Enter water amount in ${unit.shortLabel}:",
                    fontSize = 14.sp,
                    color = Color(0xFF475569)
                )
                OutlinedTextField(
                    value = inputStr,
                    onValueChange = {
                        inputStr = it
                        errorMessage = null
                    },
                    isError = errorMessage != null,
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    placeholder = { Text(if (unit == WaterUnit.METRIC) "e.g. 350" else "e.g. 12") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF0284C7),
                        unfocusedBorderColor = Color(0xFFCBD5E1)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = Color(0xFFDC2626),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                } else {
                    Text(
                        text = "Allowed range: 1 – $maxLimit ${unit.shortLabel}. Zero is not allowed.",
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val value = inputStr.trim().toIntOrNull()
                    if (inputStr.trim().isEmpty()) {
                        errorMessage = "Custom intake amount cannot be empty."
                    } else if (value == null) {
                        errorMessage = "Please enter a valid whole number."
                    } else if (value <= 0) {
                        errorMessage = "Amount must be at least 1 ${unit.shortLabel}. Zero is not allowed."
                    } else if (value > maxLimit) {
                        errorMessage = "Amount cannot exceed $maxLimit ${unit.shortLabel}."
                    } else {
                        val amountMl = unit.toMl(value)
                        onConfirm(amountMl, "Custom Drink")
                    }
                }
            ) {
                Text("Log Intake", fontWeight = FontWeight.Bold, color = Color(0xFF0284C7))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color(0xFF64748B))
            }
        }
    )
}
