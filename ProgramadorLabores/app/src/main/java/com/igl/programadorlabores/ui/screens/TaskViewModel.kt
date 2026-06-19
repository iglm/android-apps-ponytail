package com.igl.programadorlabores.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.igl.programadorlabores.data.AppDatabase
import com.igl.programadorlabores.data.Task
import com.igl.programadorlabores.data.TaskDao
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TaskViewModel(private val dao: TaskDao) : ViewModel() {
    val allTasks: StateFlow<List<Task>> = dao.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val pending: StateFlow<Int> = dao.countByStatus("pendiente")
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val inProgress: StateFlow<Int> = dao.countByStatus("en_progreso")
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val completed: StateFlow<Int> = dao.countByStatus("completada")
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun add(title: String, type: String, workers: Int = 1, notes: String = "") {
        viewModelScope.launch {
            dao.insert(Task(title = title, type = type, workers = workers, notes = notes))
        }
    }

    fun updateStatus(task: Task, status: String) {
        viewModelScope.launch { dao.update(task.copy(status = status)) }
    }

    fun delete(task: Task) {
        viewModelScope.launch { dao.delete(task) }
    }

    class Factory(private val dao: TaskDao) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            TaskViewModel(dao) as T
    }
}
