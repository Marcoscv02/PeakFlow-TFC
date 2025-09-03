package marcos.peakflow.domain.repository

import marcos.peakflow.domain.model.user.User

interface AuthRepository {
    suspend fun registerUser(user: User, password: String): Boolean
    suspend fun loginUser(email:String, password: String): Boolean
    suspend fun resetPassword (email: String): Boolean
    suspend fun closeSession(): Boolean
    suspend fun getAllUsers() : List<User>
    suspend fun deleteUser(user: User): Boolean
    suspend fun signInWithGoogle(): Boolean
    suspend fun getCurrentUser(): User?
}