package com.igl.programadorlabores.ui.screens

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
import com.igl.programadorlabores.data.AppDatabase
import com.igl.programadorlabores.data.Task
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    viewModel: TaskViewModel = viewModel(
        factory = TaskViewModel.Factory(
            AppDatabase.get(LocalContext.current).taskDao()
        )
    )
) {
    val tasks by viewModel.allTasks.collectAsState()
    val pending by viewModel.pending.collectAsState()
    val inProgress by viewModel.inProgress.collectAsState()
    val completed by viewModel.completed.collectAsState()
    var showAdd by remember { mutableStateOf(false) }
    var filter by remember { mutableStateOf("todas") }

    val filtered = when (filter) {
        "pendiente" -> tasks.filter { it.status == "pendiente" }
        "en_progreso" -> tasks.filter { it.status == "en_progreso" }
        "completada" -> tasks.filter { it.status == "completada" }
        else -> tasks
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Programador Labores") }) },
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
                StatCard("Pendientes", pending)
                StatCard("En progreso", inProgress)
                StatCard("Completadas", completed)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Filtros
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("todas" to "Todas", "pendiente" to "Pendientes", "en_progreso" to "En progreso", "completada" to "Completadas").forEach { (key, label) ->
                    FilterChip(
                        selected = filter == key,
                        onClick = { filter = key },
                        label = { Text(label) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (filtered.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Sin labores. Toca + para agregar.")
                }
            } else {
                LazyColumn {
                    items(filtered, key = { it.id }) { task ->
                        TaskItem(
                            task = task,
                            onStatusChange = { status -> viewModel.updateStatus(task, status) },
                            onDelete = { viewModel.delete(task) }
                        )
                    }
                }
            }
        }
    }

    if (showAdd) {
        AddTaskDialog(onDismiss = { showAdd = false }, onAdd = { title, type, workers, notes ->
            viewModel.add(title, type, workers, notes)
            showAdd = false
        })
    }
}

@Composable
fun StatCard(label: String, count: Int) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("$count", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(label, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun TaskItem(task: Task, onStatusChange: (String) -> Unit, onDelete: () -> Unit) {
    val date = remember(task.date) {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(task.date))
    }
    val statusColor = when (task.status) {
        "pendiente" -> MaterialTheme.colorScheme.error
        "en_progreso" -> MaterialTheme.colorScheme.tertiary
        "completada" -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.onSurface
    }

    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(task.title, fontWeight = FontWeight.Medium)
                    Text("${task.type} · ${task.workers} trabajador(es) · $date", style = MaterialTheme.typography.bodySmall)
                    if (task.notes.isNotBlank()) Text(task.notes, style = MaterialTheme.typography.bodySmall)
                }
                TextButton(onClick = { onDelete() }) {
                    Text("✕", color = MaterialTheme.colorScheme.error)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = task.status == "pendiente",
                    onClick = { onStatusChange("pendiente") },
                    label = { Text("Pendiente") },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = MaterialTheme.colorScheme.errorContainer)
                )
                FilterChip(
                    selected = task.status == "en_progreso",
                    onClick = { onStatusChange("en_progreso") },
                    label = { Text("En progreso") }
                )
                FilterChip(
                    selected = task.status == "completada",
                    onClick = { onStatusChange("completada") },
                    label = { Text("Completada") },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = MaterialTheme.colorScheme.primaryContainer)
                )
            }
        }
    }
}

@Composable
fun AddTaskDialog(onDismiss: () -> Unit, onAdd: (String, String, Int, String) -> Unit) {
    var title by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("siembra") }
    var workers by remember { mutableStateOf("1") }
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nueva labor") },
        text = {
            Column {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Título") }, singleLine = true)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = type, onValueChange = { type = it }, label = { Text("Tipo (siembra/cosecha/fertilizacion/riego)") }, singleLine = true)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = workers, onValueChange = { workers = it }, label = { Text("Trabajadores") }, singleLine = true)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = notes, onValueChange = { notes = it }, label = { Text("Notas (opcional)") })
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (title.isBlank()) return@TextButton
                onAdd(title, type, workers.toIntOrNull() ?: 1, notes)
            }) { Text("Agregar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}
