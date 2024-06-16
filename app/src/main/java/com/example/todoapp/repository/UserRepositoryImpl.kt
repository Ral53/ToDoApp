import android.R.attr.name
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.todoapp.model.User
import com.example.todoapp.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await


class UserRepositoryImpl() : UserRepository {

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()
//    private val credentialManager =  CredentialManager(context)

    companion object {
        private const val TAG = "UserRepositoryImpl"
    }

    override suspend fun signUpUser(email: String, password: String, userPass: User): Result<Unit> {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            val user: FirebaseUser? = auth.getCurrentUser()
            val uid = user?.uid // Get the user's unique ID
//            storeUserData(uid, name, profileUri)
            addUserInRealtimeDatabase(uid, userPass, )
            Log.d(TAG, "Successful Firebase Authentication and Realtime Database Storage")
            Log.d(TAG, "Signe up : ${user}")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Sign up failed", e)
            Result.failure(e)
        }
    }

    override suspend fun logInUser(email: String, password: String): Result<Unit> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
//            credentialManager.setLoggedIn(true)
            Log.d(TAG, "User Logged In Successfully")
            Result.success(Unit)
        } catch (e: Exception) {
            Log.d(TAG, "Login Failed ${e.message}")
            Result.failure(e)
        }
    }

    override fun addUserInRealtimeDatabase(userId: String?, user: User) {
        if (userId != null) {
            user.uid = userId
        }
        database.getReference("users").child(user.uid).setValue(user)
            .addOnSuccessListener {
                Log.d(TAG, "User added to Realtime Database")
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Failed to add user to Realtime Database", e)
                // Consider handling failure with Result.failure(e) if needed
            }
    }

    override fun getLoggedInUser(userId: String): LiveData<User?> {
        val userLiveData = MutableLiveData<User?>()
        database.getReference("users").child(userId).get()
            .addOnSuccessListener { snapshot ->
                val user = snapshot.getValue(User::class.java)
                userLiveData.value = user
                Log.d(TAG, "User data retrieved successfully")
            }
            .addOnFailureListener { e ->
                userLiveData.value = null
                Log.e(TAG, "Failed to retrieve user data", e)
                // Consider handling failure with userLiveData.value = null or Result.failure(e) if needed
            }
        return userLiveData
    }

    override suspend fun resetPassword(email: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Log.d(TAG, "Password reset email sent successfully")
            Result.success(Unit)
        } catch (e: Exception) {
            val message = if (e.message?.contains("user-not-found") == true) {
                "No user found with the provided email"
            } else {
                e.message ?: "Password Reset Failed"
            }
            Log.e(TAG, message, e)
            Result.failure(Exception(message, e))
        }
    }

    override fun logOut() {
        auth.signOut()
//        credentialManager.setLoggedIn(false)
        Log.d(TAG, "User logged out successfully")
    }
}
