package com.igl.gastosmoto.ui.screens

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
import com.igl.gastosmoto.data.AppDatabase
import com.igl.gastosmoto.data.Expense
import com.igl.gastosmoto.data.ExpenseDao
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseScreen(
    viewModel: ExpenseViewModel = viewModel(
        factory = ExpenseViewModel.Factory(
            AppDatabase.get(LocalContext.current).expenseDao()
        )
    )
) {
    val state by viewModel.uiState.collectAsState()
    val total by viewModel.total.collectAsState()
    var showAdd by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Gastos Moto") })
        },
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
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Text(
                    text = "Total: $${"%.2f".format(total)}",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (val s = state) {
                is UiState.Idle -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Cargando...")
                    }
                }
                is UiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is UiState.Success -> {
                    if (s.data.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Sin gastos. Toca + para agregar.")
                        }
                    } else {
                        LazyColumn {
                            items(s.data, key = { it.id }) { expense ->
                                ExpenseItem(expense = expense, onDelete = { viewModel.delete(expense) })
                            }
                        }
                    }
                }
                is UiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Error: ${s.message}")
                    }
                }
            }
        }
    }

    if (showAdd) {
        AddExpenseDialog(onDismiss = { showAdd = false }, onAdd = { amount, concept, km ->
            viewModel.add(amount, concept, km)
            showAdd = false
        })
    }
}

@Composable
fun ExpenseItem(expense: Expense, onDelete: () -> Unit) {
    val date = remember(expense.date) {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(expense.date))
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(expense.concept, fontWeight = FontWeight.Medium)
                Text(date, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                if (expense.km > 0) Text("${expense.km} km", style = MaterialTheme.typography.bodySmall)
            }
            Text("$${"%.2f".format(expense.amount)}", fontWeight = FontWeight.Bold)
            TextButton(onClick = onDelete) {
                Text("✕", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
fun AddExpenseDialog(onDismiss: () -> Unit, onAdd: (Double, String, Int) -> Unit) {
    var amount by remember { mutableStateOf("") }
    var concept by remember { mutableStateOf("") }
    var km by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nuevo gasto") },
        text = {
            Column {
                OutlinedTextField(value = amount, onValueChange = { amount = it }, label = { Text("Monto") }, singleLine = true)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = concept, onValueChange = { concept = it }, label = { Text("Concepto") }, singleLine = true)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = km, onValueChange = { km = it }, label = { Text("Km (opcional)") }, singleLine = true)
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val a = amount.toDoubleOrNull() ?: return@TextButton
                if (concept.isBlank()) return@TextButton
                onAdd(a, concept, km.toIntOrNull() ?: 0)
            }) { Text("Agregar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}
