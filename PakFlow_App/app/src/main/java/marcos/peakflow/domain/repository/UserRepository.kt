package marcos.peakflow.domain.repository

import marcos.peakflow.domain.model.UserProfile

interface UserRepository {
    suspend fun registerUser(userProfile: UserProfile)
    suspend fun loginUser(userProfile: UserProfile)
    suspend fun closeSession(userProfile: UserProfile)
    suspend fun getAllUsers() : List<UserProfile>
    suspend fun deleteUser(userProfile: UserProfile)
}