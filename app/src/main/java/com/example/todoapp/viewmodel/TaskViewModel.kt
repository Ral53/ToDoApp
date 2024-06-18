import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.model.Task
import com.example.todoapp.repository.TaskRepository
import com.example.todoapp.repository.TaskRepositoryImpl
import kotlinx.coroutines.launch

class TaskViewModel : ViewModel() {

    private val taskRepository: TaskRepository = TaskRepositoryImpl()

    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> = _tasks

    private val _taskCreated = MutableLiveData<Boolean>()
    val taskCreated: LiveData<Boolean> = _taskCreated

    private val _taskDeleted = MutableLiveData<Boolean>()
    val taskDeleted: LiveData<Boolean> = _taskDeleted

    fun createTask(task: Task) {
        viewModelScope.launch {
            try {
                taskRepository.createTask(task)
                _taskCreated.postValue(true)
                fetchTasks(task.userId)
            } catch (e: Exception) {
                _taskCreated.postValue(false)
            }
        }
    }

    fun fetchTasks(userId: String) {
        viewModelScope.launch {
            try {
                val userTasks = taskRepository.getAllTasks(userId)
                _tasks.value = userTasks
                Log.d("TaskViewModel", "fetchTasks: Fetched ${userTasks.size} tasks for userId: $userId")
            } catch (e: Exception) {
                _tasks.value = emptyList()
                Log.e("TaskViewModel", "fetchTasks: Failed to fetch tasks for userId: $userId", e)
            }
        }
    }

    fun deleteTask(taskId: String) {
        viewModelScope.launch {
            try {
                val isDeleted = taskRepository.deleteTask(taskId)
                _taskDeleted.value = isDeleted
                if (isDeleted) {
                    Log.d("TaskViewModel", "deleteTask: Task deleted successfully with id: $taskId")
                } else {
                    Log.d("TaskViewModel", "deleteTask: Failed to delete task with id: $taskId")
                }
            } catch (e: Exception) {
                _taskDeleted.value = false
                Log.e("TaskViewModel", "deleteTask: Failed to delete task with id: $taskId", e)
            }
        }
    }
}
