import android.util.Log
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

    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean>
        get() = _loginResult

    companion object {
        private const val TAG = "User ViewModel"
    }

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

    fun logInUser(email: String, password: String): Boolean {
        var loginSuccess = false
        viewModelScope.launch {
            try {
                Log.d(TAG, "Attempting login with email: $email")
                loginSuccess = userRepository.logInUser(email, password)
                _loginResult.postValue(loginSuccess)
                if (loginSuccess) {
                    Log.d(TAG, "Login successful")
                } else {
                    Log.d(TAG, "Login failed: Invalid credentials or other error")
                }
            } catch (e: Exception) {
                _loginResult.postValue(false)
                Log.e(TAG, "Login failed: ${e.message}", e)
            }
        }
        return loginSuccess
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
