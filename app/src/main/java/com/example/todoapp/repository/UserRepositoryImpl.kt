import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.todoapp.model.User
import com.example.todoapp.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl : UserRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()

    companion object {
        private const val TAG = "UserRepositoryImpl"
    }

    private val _loggedInUser = MutableLiveData<User?>()
    override fun getLoggedInUser(): LiveData<User?> = _loggedInUser

    init {
        // Observe the authentication state and update _loggedInUser accordingly
        auth.addAuthStateListener { auth ->
            val user = auth.currentUser
            if (user != null) {
                loadUserFromDatabase(user.uid)
            } else {
                _loggedInUser.postValue(null)
            }
        }
    }

    private fun loadUserFromDatabase(userId: String) {
        val userRef = database.getReference("users").child(userId)
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)
                _loggedInUser.postValue(user)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "Failed to read user data", error.toException())
                _loggedInUser.postValue(null)
            }
        })
    }

    override suspend fun signUpUser(email: String, password: String, userPass: User): Result<Unit> {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            val user: FirebaseUser? = auth.currentUser
            val uid = user?.uid // Get the user's unique ID
            if (uid != null) {
                addUserInRealtimeDatabase(uid, userPass)
                Log.d(TAG, "Successful Firebase Authentication and Realtime Database Storage")
                Result.success(Unit)
            } else {
                throw Exception("User ID is null after sign-up")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Sign up failed", e)
            Result.failure(e)
        }
    }

    override suspend fun logInUser(email: String, password: String): Boolean {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Log.d(TAG, "Firebase authentication successful")
            true // Return true indicating successful login
        } catch (e: Exception) {
            Log.e(TAG, "Firebase authentication failed", e)
            false // Return false indicating login failure
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
            }
    }

    override fun logOut() {
        auth.signOut()
        Log.d(TAG, "User logged out successfully")
    }
}
