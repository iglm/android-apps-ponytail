package com.igl.programadorlabores.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Entity mínima
data class Task(
    val id: Long = 0,
    val description: String,
    val date: Long,
    val completed: Boolean = false
)

// DAO mínima
@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks ORDER BY date DESC")
    suspend fun getAll(): List<Task>
    
    @Insert
    suspend fun insert(task: Task)
    
    @Query("UPDATE tasks SET completed = :done WHERE id = :id")
    suspend fun setCompleted(id: Long, done: Boolean)
}

// State sellado
sealed class UiState<out T> {
    object Idle : UiState<Nothing>()
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

// ViewModel
class TaskViewModel(private val dao: TaskDao) : ViewModel() {
    private val _state = MutableStateFlow<UiState<List<Task>>>(UiState.Idle)
    val state: StateFlow<UiState<List<Task>>> = _state
    
    fun load() {
        viewModelScope.launch {
            _state.update { UiState.Loading }
            try { _state.update { UiState.Success(dao.getAll()) } }
            catch (e: Exception) { _state.update { UiState.Error(e.message ?: "Error") } }
        }
    }
    
    fun add(description: String) {
        viewModelScope.launch { dao.insert(Task(description = description, date = System.currentTimeMillis())) }
    }
    
    fun toggle(id: Long, done: Boolean) {
        viewModelScope.launch { dao.setCompleted(id, done) }
    }
}