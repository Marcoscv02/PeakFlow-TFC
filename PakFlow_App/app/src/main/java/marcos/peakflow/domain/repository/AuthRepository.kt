package marcos.peakflow.domain.repository

import marcos.peakflow.domain.model.User

interface AuthRepository {
    suspend fun registerUser(user: User)
    suspend fun loginUser(user: User)
    suspend fun closeSession(user: User)
    suspend fun getAllUsers() : List<User>
    suspend fun deleteUser(user: User)
}