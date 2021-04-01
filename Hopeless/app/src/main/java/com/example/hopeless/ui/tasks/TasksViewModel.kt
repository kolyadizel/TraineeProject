package com.example.hopeless.ui.tasks

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.hopeless.data.Task
import com.example.hopeless.data.TaskDao
import com.example.hopeless.ui.ADD_TASK_RESULT_OK
import com.example.hopeless.ui.DELETE_TASK_RESULT_OK
import com.example.hopeless.ui.EDIT_TASK_RESULT_OK
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class TasksViewModel @ViewModelInject constructor(
    private val taskDao: TaskDao
) : ViewModel() {

    private val tasksEventChannel = Channel<TasksEvent>()
    val tasksEvent = tasksEventChannel.receiveAsFlow()

    private val tasksFlow = taskDao.getTasks()
    val tasks = tasksFlow.asLiveData()


    fun onAddNewTaskClick() = viewModelScope.launch {
        tasksEventChannel.send(TasksEvent.NavigateToAddTaskScreen)
    }

    fun onTaskSelected(task: Task) = viewModelScope.launch {
        tasksEventChannel.send(TasksEvent.NavigateToEditTaskScreen(task))
    }

    fun onAddEditResult(result: Int){
        when (result) {
            ADD_TASK_RESULT_OK -> showTaskConfirmationMessage("Task added")
            EDIT_TASK_RESULT_OK -> showTaskConfirmationMessage("Task edited")
            DELETE_TASK_RESULT_OK -> showTaskConfirmationMessage("Task deleted")
        }
    }

    private fun showTaskConfirmationMessage(text : String) = viewModelScope.launch {
        tasksEventChannel.send(TasksEvent.ShowTaskSavedConfirmationMessage(text))
    }

    sealed class TasksEvent {
        object NavigateToAddTaskScreen : TasksEvent()
        data class NavigateToEditTaskScreen(val task: Task) : TasksEvent()
        data class ShowTaskSavedConfirmationMessage(val msg: String ) : TasksEvent()
    }
}