package com.igl.gastosmoto.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.igl.gastosmoto.data.AppDatabase
import com.igl.gastosmoto.data.Expense
import com.igl.gastosmoto.data.ExpenseDao
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class UiState<out T> {
    data object Idle : UiState<Nothing>()
    data object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

class ExpenseViewModel(private val dao: ExpenseDao) : ViewModel() {
    val uiState: StateFlow<UiState<List<Expense>>> = dao.getAll()
        .map<UiState<List<Expense>>> { UiState.Success(it) }
        .catch { emit(UiState.Error(it.message ?: "Error")) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UiState.Idle)

    val total: StateFlow<Double> = dao.total()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    fun add(amount: Double, concept: String, km: Int = 0) {
        viewModelScope.launch {
            try {
                dao.insert(Expense(amount = amount, concept = concept, km = km))
            } catch (e: Exception) {
                // YAGNI: log mínimo, UI ya muestra error genérico
            }
        }
    }

    fun delete(expense: Expense) {
        viewModelScope.launch { dao.delete(expense) }
    }

    class Factory(private val dao: ExpenseDao) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            ExpenseViewModel(dao) as T
    }
}
