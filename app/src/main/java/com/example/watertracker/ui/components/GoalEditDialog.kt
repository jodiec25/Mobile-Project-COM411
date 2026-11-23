package com.example.watertracker.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.watertracker.model.WaterUnit

/**
 * Reusable dialog for editing the daily water goal with input validation.
 * Rejects empty, zero, negative, or unrealistic limits.
 *
 * @param currentTargetMl Current target intake in milliliters.
 * @param unit Active measurement unit system.
 * @param onDismiss Dismiss handler callback.
 * @param onConfirm Handler invoked with the validated new target in milliliters.
 */
@Composable
fun GoalEditDialog(
    currentTargetMl: Int,
    unit: WaterUnit,
    onDismiss: () -> Unit,
    onConfirm: (newTargetMl: Int) -> Unit
) {
    val initialInUnit = unit.fromMl(currentTargetMl)
    var inputStr by remember { mutableStateOf(initialInUnit.toString()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Set Daily Water Goal",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Enter your target intake in ${unit.shortLabel}:",
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
                    placeholder = { Text("e.g. ${if (unit == WaterUnit.METRIC) "2850" else "96"}") },
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
                        fontSize = 12.sp
                    )
                } else {
                    val (minV, maxV) = unit.getGoalRange()
                    Text(
                        text = "Allowed range: $minV - $maxV ${unit.shortLabel}. Zero is not allowed.",
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val validationError = unit.validateGoalInput(inputStr)
                    if (validationError != null) {
                        errorMessage = validationError
                    } else {
                        val valueInUnit = inputStr.trim().toInt()
                        val newGoalMl = unit.toMl(valueInUnit)
                        onConfirm(newGoalMl)
                    }
                }
            ) {
                Text("Save Goal", fontWeight = FontWeight.Bold, color = Color(0xFF0284C7))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color(0xFF64748B))
            }
        }
    )
}
