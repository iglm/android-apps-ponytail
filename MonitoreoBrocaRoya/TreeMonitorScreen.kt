package com.igl.monitoreobrocaroya.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.igl.monitoreobrocaroya.core.*

@Composable
fun TreeMonitorScreen(viewModel: TreeMonitorViewModel) {
    val state by viewModel.state.collectAsState()
    
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        var status by remember { mutableStateOf("saludable") }
        var loteId by remember { mutableStateOf(1L) }
        
        OutlinedTextField(
            value = status,
            onValueChange = { status = it },
            label = { Text("Estado árbol") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(onClick = { viewModel.addTree(loteId, status) }) {
            Text("Agregar árbol")
        }
        
        when (state) {
            is UiState.Loading -> CircularProgressIndicator()
            is UiState.Success -> {
                LazyColumn {
                    items((state as UiState.Success<List<Tree>>).data) { tree ->
                        Text("${tree.status} - ${tree.fechaMonitoreo}")
                    }
                }
            }
            is UiState.Error -> Text((state as UiState.Error).message)
            else -> Unit
        }
    }
}