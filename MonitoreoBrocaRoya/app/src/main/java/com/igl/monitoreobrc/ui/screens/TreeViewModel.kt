package com.igl.monitoreobrc.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.igl.monitoreobrc.AppDatabase
import com.igl.monitoreobrc.data.TreeRecord
import com.igl.monitoreobrc.data.TreeDao
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TreeViewModel(private val dao: TreeDao) : ViewModel() {
    val allRecords: StateFlow<List<TreeRecord>> = dao.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val broca: StateFlow<Int> = dao.countByDisease("broca")
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val roya: StateFlow<Int> = dao.countByDisease("roya")
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val diseased: StateFlow<List<TreeRecord>> = dao.getDiseased()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun add(plot: String, treeNumber: Int, disease: String, severity: String, notes: String) {
        viewModelScope.launch {
            dao.insert(TreeRecord(plot = plot, treeNumber = treeNumber, disease = disease, severity = severity, notes = notes))
        }
    }

    fun delete(record: TreeRecord) {
        viewModelScope.launch { dao.delete(record) }
    }

    class Factory(private val dao: TreeDao) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            TreeViewModel(dao) as T
    }
}
