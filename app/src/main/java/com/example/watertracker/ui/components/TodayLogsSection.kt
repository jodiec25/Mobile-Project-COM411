package com.example.watertracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.watertracker.model.LogEntry
import com.example.watertracker.model.WaterUnit

/**
 * Section rendering today's log entries with click-to-edit capabilities.
 *
 * Formats values dynamically using active [WaterUnit].
 *
 * @param logs List of LogEntry records.
 * @param unit Measurement unit system.
 * @param onEditLog Callback triggered when a log entry's amount is edited.
 * @param onDeleteLog Callback triggered when a log entry is deleted.
 */
@Composable
fun TodayLogsSection(
    logs: List<LogEntry>,
    unit: WaterUnit = WaterUnit.METRIC,
    onEditLog: (LogEntry, Int) -> Unit = { _, _ -> },
    onDeleteLog: (LogEntry) -> Unit = {}
) {
    var selectedLogToEdit by remember { mutableStateOf<LogEntry?>(null) }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Today's Logs",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFE0F2FE))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "${logs.size} entries",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF0284C7)
                    )
                }
            }
            Text(
                text = "Tap entry to edit",
                fontSize = 12.sp,
                color = Color(0xFF64748B)
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            logs.forEach { entry ->
                LogItemRow(
                    entry = entry,
                    unit = unit,
                    onClick = { selectedLogToEdit = entry }
                )
            }
        }
    }

    if (selectedLogToEdit != null) {
        EditLogEntryDialog(
            entry = selectedLogToEdit!!,
            unit = unit,
            onDismiss = { selectedLogToEdit = null },
            onSave = { updatedAmountMl ->
                onEditLog(selectedLogToEdit!!, updatedAmountMl)
                selectedLogToEdit = null
            },
            onDelete = {
                onDeleteLog(selectedLogToEdit!!)
                selectedLogToEdit = null
            }
        )
    }
}

/**
 * Card row representing an individual LogEntry record.
 */
@Composable
fun LogItemRow(
    entry: LogEntry,
    unit: WaterUnit = WaterUnit.METRIC,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(entry.iconBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = entry.icon,
                        contentDescription = entry.title,
                        tint = entry.iconTint,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = entry.title,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(entry.tagColor)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = entry.tag,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = entry.tagTextColor
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "${entry.time}  •  ${entry.note}",
                        fontSize = 12.sp,
                        color = Color(0xFF64748B)
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "+${unit.fromMl(entry.amountMl)}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0284C7)
                    )
                    Text(
                        text = unit.shortLabel,
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                    )
                }
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Entry",
                    tint = Color(0xFFCBD5E1),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

/**
 * Dialog for editing or deleting an existing log entry amount.
 * Enforces range of 1 to 5000 (ml) or equivalent in fl oz. Zero is prohibited.
 */
@Composable
fun EditLogEntryDialog(
    entry: LogEntry,
    unit: WaterUnit,
    onDismiss: () -> Unit,
    onSave: (newAmountMl: Int) -> Unit,
    onDelete: () -> Unit
) {
    val initialValue = unit.fromMl(entry.amountMl)
    var inputStr by remember { mutableStateOf(initialValue.toString()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val maxLimit = if (unit == WaterUnit.METRIC) 5000 else 169

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Edit ${entry.title}",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Update intake amount in ${unit.shortLabel}:",
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
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TextButton(onClick = onDelete) {
                    Text("Delete", color = Color(0xFFDC2626), fontWeight = FontWeight.Bold)
                }
                TextButton(
                    onClick = {
                        val value = inputStr.trim().toIntOrNull()
                        if (inputStr.trim().isEmpty()) {
                            errorMessage = "Amount cannot be empty."
                        } else if (value == null) {
                            errorMessage = "Please enter a valid whole number."
                        } else if (value <= 0) {
                            errorMessage = "Amount must be at least 1 ${unit.shortLabel}. Zero is not allowed."
                        } else if (value > maxLimit) {
                            errorMessage = "Amount cannot exceed $maxLimit ${unit.shortLabel}."
                        } else {
                            val newAmountMl = unit.toMl(value)
                            onSave(newAmountMl)
                        }
                    }
                ) {
                    Text("Save", color = Color(0xFF0284C7), fontWeight = FontWeight.Bold)
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color(0xFF64748B))
            }
        }
    )
}
