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

    private val _tasks: MutableLiveData<List<Task>> = MutableLiveData()
    val tasks: LiveData<List<Task>>
        get() = _tasks

    private val _taskCreated: MutableLiveData<Boolean> = MutableLiveData()
    val taskCreated: LiveData<Boolean>
        get() = _taskCreated

    private val _taskDeleted: MutableLiveData<Boolean> = MutableLiveData()
    val taskDeleted: LiveData<Boolean>
        get() = _taskDeleted

    fun createTask(task: Task) {
        viewModelScope.launch {
            try {
                taskRepository.createTask(task)
                _taskCreated.value = true
            } catch (e: Exception) {
                _taskCreated.value = false
            }
        }
    }

    fun getAllTasks(userId: String) {
        viewModelScope.launch {
            try {
                val userTasks = taskRepository.getAllTasks(userId)
                _tasks.value = userTasks
            } catch (e: Exception) {
                _tasks.value = emptyList()
            }
        }
    }

    fun deleteTask(taskId: String) {
        viewModelScope.launch {
            try {
                val isDeleted = taskRepository.deleteTask(taskId)
                _taskDeleted.value = isDeleted
            } catch (e: Exception) {
                _taskDeleted.value = false
            }
        }
    }
}
