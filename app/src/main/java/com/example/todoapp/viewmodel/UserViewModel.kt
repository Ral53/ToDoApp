import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.model.User
import kotlinx.coroutines.launch

class UserViewModel() : ViewModel() {

    private val userRepository = UserRepositoryImpl()

    private val _userMessage = MutableLiveData<Boolean>()
    val userMessage: LiveData<Boolean>
        get() = _userMessage


    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user

    fun signUpUser(email: String, password: String, user: User) {
        viewModelScope.launch {
            try {
                userRepository.signUpUser(email, password, user)
                _userMessage.postValue(true)
            } catch (e: Exception) {
                _userMessage.postValue(false)
            }
        }
    }

    fun logInUser(email: String, password: String) {
        viewModelScope.launch {
            try {
                userRepository.logInUser(email, password)
                _userMessage.postValue(true)
            } catch (e: Exception) {
                _userMessage.postValue(false)
            }
        }
    }

    fun getUserData(userId: String) {
        userRepository.getLoggedInUser(userId)
    }

    fun resetPassword(email: String) {
        viewModelScope.launch {
            userRepository.resetPassword(email)
        }
    }

    fun logOut() {
        userRepository.logOut()
    }
}
