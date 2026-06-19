package com.igl.gastosmoto.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.igl.gastosmoto.*

@Composable
fun ExpenseScreen(viewModel: ExpenseViewModel) {
    val state by viewModel.state.collectAsState()
    
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        var amount by remember { mutableStateOf("") }
        var concept by remember { mutableStateOf("") }
        
        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Monto") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = concept,
            onValueChange = { concept = it },
            label = { Text("Concepto") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = {
                amount.toDoubleOrNull()?.let { amt ->
                    viewModel.onEvent(ExpenseEvent.Add(amt, concept))
                    amount = ""; concept = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Agregar") }
        
        when (state) {
            is UiState.Loading -> CircularProgressIndicator()
            is UiState.Success -> {
                LazyColumn {
                    items((state as UiState.Success).expenses) { expense ->
                        ExpenseItem(expense) { viewModel.onEvent(ExpenseEvent.Delete(expense)) }
                    }
                }
            }
            is UiState.Error -> Text((state as UiState.Error).message)
            else -> Unit
        }
    }
}

@Composable
private fun ExpenseItem(expense: Expense, onDelete: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("${expense.amount} - ${expense.concept}")
        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Delete, "Borrar")
        }
    }
}