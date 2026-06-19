package com.igl.monitoreobrc.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.igl.monitoreobrc.data.AppDatabase
import com.igl.monitoreobrc.data.TreeRecord
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TreeMonitorScreen(
    viewModel: TreeViewModel = viewModel(
        factory = TreeViewModel.Factory(
            AppDatabase.get(LocalContext.current).treeDao()
        )
    )
) {
    val records by viewModel.allRecords.collectAsState()
    val broca by viewModel.broca.collectAsState()
    val roya by viewModel.roya.collectAsState()
    var showAdd by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Monitoreo Broca/Roya") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAdd = true }) {
                Text("+", style = MaterialTheme.typography.headlineMedium)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatCard("Con broca", broca, MaterialTheme.colorScheme.error)
                StatCard("Con roya", roya, MaterialTheme.colorScheme.tertiary)
                StatCard("Total registros", records.size, MaterialTheme.colorScheme.primary)
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (records.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Sin registros. Toca + para monitorear.")
                }
            } else {
                LazyColumn {
                    items(records, key = { it.id }) { record ->
                        TreeItem(record = record, onDelete = { viewModel.delete(record) })
                    }
                }
            }
        }
    }

    if (showAdd) {
        AddTreeDialog(onDismiss = { showAdd = false }, onAdd = { plot, num, disease, severity, notes ->
            viewModel.add(plot, num, disease, severity, notes)
            showAdd = false
        })
    }
}

@Composable
fun StatCard(label: String, count: Int, color: androidx.compose.ui.graphics.Color) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("$count", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = color)
            Text(label, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun TreeItem(record: TreeRecord, onDelete: () -> Unit) {
    val date = remember(record.date) {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(record.date))
    }
    val severityColor = when (record.severity) {
        "leve" -> MaterialTheme.colorScheme.primary
        "moderada" -> MaterialTheme.colorScheme.tertiary
        "severa" -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.onSurface
    }

    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Parcela ${record.plot} · Árbol #${record.treeNumber}", fontWeight = FontWeight.Medium)
                Text("${record.disease} · ${record.severity} · $date", style = MaterialTheme.typography.bodySmall)
                if (record.notes.isNotBlank()) Text(record.notes, style = MaterialTheme.typography.bodySmall)
            }
            TextButton(onClick = onDelete) {
                Text("✕", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
fun AddTreeDialog(onDismiss: () -> Unit, onAdd: (String, Int, String, String, String) -> Unit) {
    var plot by remember { mutableStateOf("") }
    var treeNumber by remember { mutableStateOf("") }
    var disease by remember { mutableStateOf("broca") }
    var severity by remember { mutableStateOf("leve") }
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nuevo registro") },
        text = {
            Column {
                OutlinedTextField(value = plot, onValueChange = { plot = it }, label = { Text("Parcela") }, singleLine = true)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = treeNumber, onValueChange = { treeNumber = it }, label = { Text("Número de árbol") }, singleLine = true)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = disease, onValueChange = { disease = it }, label = { Text("Enfermedad (broca/roya/ninguna)") }, singleLine = true)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = severity, onValueChange = { severity = it }, label = { Text("Severidad (leve/moderada/severa)") }, singleLine = true)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = notes, onValueChange = { notes = it }, label = { Text("Notas") })
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (plot.isBlank() || treeNumber.isBlank()) return@TextButton
                onAdd(plot, treeNumber.toIntOrNull() ?: 0, disease, severity, notes)
            }) { Text("Agregar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}
