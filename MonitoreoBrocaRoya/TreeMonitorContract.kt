package com.igl.monitoreobrocaroya.core

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
data class Tree(
    val id: Long = 0,
    val loteId: Long,
    val status: String, // saludable/infectado/tratado
    val fechaMonitoreo: Long = System.currentTimeMillis()
)

// DAO mínima
@Dao
interface TreeDao {
    @Query("SELECT * FROM trees WHERE loteId = :loteId")
    suspend fun getByLote(loteId: Long): List<Tree>
    
    @Insert
    suspend fun insert(tree: Tree)
}

// State sellado (regla oro #0)
sealed class UiState<out T> {
    object Idle : UiState<Nothing>()
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

// ViewModel minimalista
class TreeMonitorViewModel(private val dao: TreeDao) : ViewModel() {
    private val _state = MutableStateFlow<UiState<List<Tree>>>(UiState.Idle)
    val state: StateFlow<UiState<List<Tree>>> = _state
    
    fun loadTrees(loteId: Long) {
        viewModelScope.launch {
            _state.update { UiState.Loading }
            try {
                _state.update { UiState.Success(dao.getByLote(loteId)) }
            } catch (e: Exception) {
                _state.update { UiState.Error(e.message ?: "Error") }
            }
        }
    }
    
    fun addTree(loteId: Long, status: String) {
        viewModelScope.launch {
            dao.insert(Tree(loteId = loteId, status = status))
            loadTrees(loteId)
        }
    }
}