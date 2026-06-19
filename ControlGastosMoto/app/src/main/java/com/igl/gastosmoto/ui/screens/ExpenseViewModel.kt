package com.igl.gastosmoto.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
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
    private val _uiState = MutableStateFlow<UiState<List<Expense>>>(UiState.Idle)
    val uiState: StateFlow<UiState<List<Expense>>> = _uiState.asStateFlow()

    private val _total = MutableStateFlow(0.0)
    val total: StateFlow<Double> = _total.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                dao.getAll().collect { expenses ->
                    _uiState.value = UiState.Success(expenses)
                }
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Error")
            }
        }
        viewModelScope.launch {
            try {
                dao.total().collect { t ->
                    _total.value = t
                }
            } catch (_: Exception) {}
        }
    }

    fun add(amount: Double, concept: String, km: Int = 0) {
        viewModelScope.launch {
            try {
                dao.insert(Expense(amount = amount, concept = concept, km = km))
            } catch (_: Exception) {}
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
