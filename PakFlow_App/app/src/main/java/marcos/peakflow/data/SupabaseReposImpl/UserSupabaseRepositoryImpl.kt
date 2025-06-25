package marcos.peakflow.data.SupabaseReposImpl

import marcos.peakflow.domain.model.UserProfile
import marcos.peakflow.domain.repository.UserRepository

class UserSupabaseRepositoryImpl: UserRepository {
    override suspend fun registerUser(userProfile: UserProfile) {
        TODO("Not yet implemented")
    }

    override suspend fun loginUser(userProfile: UserProfile) {
        TODO("Not yet implemented")
    }

    override suspend fun closeSession(userProfile: UserProfile) {
        TODO("Not yet implemented")
    }

    override suspend fun getAllUsers(): List<UserProfile> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUser(userProfile: UserProfile) {
        TODO("Not yet implemented")
    }
}