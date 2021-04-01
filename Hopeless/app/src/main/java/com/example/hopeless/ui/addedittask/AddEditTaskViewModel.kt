package com.example.hopeless.ui.addedittask

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hopeless.data.Task
import com.example.hopeless.data.TaskDao
import com.example.hopeless.ui.ADD_TASK_RESULT_OK
import com.example.hopeless.ui.DELETE_TASK_RESULT_OK
import com.example.hopeless.ui.EDIT_TASK_RESULT_OK
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AddEditTaskViewModel @ViewModelInject constructor(
    private val taskDao: TaskDao,
    @Assisted private val state: SavedStateHandle
) : ViewModel()  {

    val task = state.get<Task>("task")

    var taskDetails = state.get<String>("taskDetails") ?: task?.details ?: ""
        set(value) {
            field = value
            state.set("taskDetails", value)
        }

    private val addEditTaskEventChannel = Channel<AddEditTaskEvent>()
    val addEditTaskEvent = addEditTaskEventChannel.receiveAsFlow()


    fun onSaveClick(){
        if (taskDetails.isBlank()){
            return
        }
        if (task != null) {
            val updatedTask = task.copy(details = taskDetails)
            updateTask(updatedTask)
        } else {
            val newTask = Task(details = taskDetails)
            createTask(newTask)
        }
    }

    private fun createTask(task: Task) = viewModelScope.launch {
        taskDao.insert(task)
        addEditTaskEventChannel.send(AddEditTaskEvent.NavigateBackWithResult(ADD_TASK_RESULT_OK))
    }

    private fun updateTask(task : Task) = viewModelScope.launch {
        taskDao.update(task)
        addEditTaskEventChannel.send(AddEditTaskEvent.NavigateBackWithResult(EDIT_TASK_RESULT_OK))
    }

    fun deleteTask(task : Task) = viewModelScope.launch {
        taskDao.delete(task)
        addEditTaskEventChannel.send(AddEditTaskEvent.NavigateBackWithResult(DELETE_TASK_RESULT_OK))
    }

    sealed class AddEditTaskEvent {
        data class NavigateBackWithResult(val result : Int) : AddEditTaskEvent()
    }
}

