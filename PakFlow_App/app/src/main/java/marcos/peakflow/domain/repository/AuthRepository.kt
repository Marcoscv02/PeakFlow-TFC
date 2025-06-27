package marcos.peakflow.domain.repository

import marcos.peakflow.domain.model.User

interface AuthRepository {
    suspend fun registerUser(user: User, password: String)
    suspend fun loginUser(email:String, password: String)
    suspend fun resetPassword (email: String)
    suspend fun closeSession(user: User)
    suspend fun getAllUsers() : List<User>
    suspend fun deleteUser(user: User)
}