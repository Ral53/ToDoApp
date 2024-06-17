import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.model.User
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    private val userRepository = UserRepositoryImpl()

    private val _userMessage = MutableLiveData<Boolean>()
    val userMessage: LiveData<Boolean>
        get() = _userMessage

    private val _loginResult = MutableLiveData<Boolean>()
    val loginResult: LiveData<Boolean>
        get() = _loginResult

    companion object {
        private const val TAG = "UserViewModel"
    }

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> get() = userRepository.getLoggedInUser()

    private val _profilePictureUrl = MutableLiveData<String?>()
    val profilePictureUrl: LiveData<String?> get() = _profilePictureUrl

    fun signUpUser(email: String, password: String, user: User) {
        viewModelScope.launch {
            try {
                val result = userRepository.signUpUser(email, password, user)
                if (result.isSuccess) {
                    _userMessage.postValue(true)
                } else {
                    _userMessage.postValue(false)
                }
            } catch (e: Exception) {
                _userMessage.postValue(false)
                Log.e(TAG, "Sign up failed: ${e.message}", e)
            }
        }
    }

    fun logInUser(email: String, password: String) {
        viewModelScope.launch {
            try {
                val loginSuccess = userRepository.logInUser(email, password)
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
    }

    private val _fullName = MutableLiveData<String?>()
    val fullName: LiveData<String?> get() = _fullName

    private val _email = MutableLiveData<String?>()
    val email: LiveData<String?> get() = _email

    init {
        user.observeForever { user ->
            if (user != null) {
                _fullName.value = user.name
                _email.value = user.email
                _profilePictureUrl.value = user.profilePictureUrl
            } else {
                _fullName.value = null
                _email.value = null
                _profilePictureUrl.value = null
            }
        }
    }

    fun logOut() {
        userRepository.logOut()
        Log.d(TAG, "User logged out successfully")
    }
}
