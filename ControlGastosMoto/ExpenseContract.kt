package com.igl.gastosmoto

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Entity mínima
data class Expense(
    val id: Long = 0,
    val amount: Double,
    val concept: String,
    val date: Long = System.currentTimeMillis()
)

// DAO mínima (sin Repository - YAGNI)
@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expenses ORDER BY date DESC")
    suspend fun getAll(): List<Expense>
    
    @Insert
    suspend fun insert(expense: Expense)
    
    @Delete
    suspend fun delete(expense: Expense)
}

// State sellado (regla oro #0)
sealed class UiState {
    object Idle : UiState()
    object Loading : UiState()
    data class Success(val expenses: List<Expense>) : UiState()
    data class Error(val message: String) : UiState()
}

// Eventos (unidireccional)
sealed class ExpenseEvent {
    data class Add(val amount: Double, val concept: String) : ExpenseEvent()
    data class Delete(val expense: Expense) : ExpenseEvent()
}

// ViewModel minimalista (sin casos de uso - YAGNI)
class ExpenseViewModel(private val dao: ExpenseDao) : ViewModel() {
    private val _state = MutableStateFlow<UiState>(UiState.Idle)
    val state: StateFlow<UiState> = _state
    
    fun onEvent(event: ExpenseEvent) {
        when (event) {
            is ExpenseEvent.Add -> addExpense(event.amount, event.concept)
            is ExpenseEvent.Delete -> deleteExpense(event.expense)
        }
    }
    
    private fun addExpense(amount: Double, concept: String) {
        viewModelScope.launch {
            _state.update { UiState.Loading }
            try {
                dao.insert(Expense(amount = amount, concept = concept))
                refresh()
            } catch (e: Exception) {
                _state.update { UiState.Error(e.message ?: "Error") }
            }
        }
    }
    
    private fun deleteExpense(expense: Expense) {
        viewModelScope.launch { dao.delete(expense) }
    }
    
    private suspend fun refresh() {
        _state.update { UiState.Success(dao.getAll()) }
    }
}