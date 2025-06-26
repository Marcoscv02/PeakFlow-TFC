package marcos.peakflow.data.SupabaseReposImpl

import io.github.jan.supabase.SupabaseClient
import marcos.peakflow.domain.model.User
import marcos.peakflow.domain.repository.AuthRepository

class AuthSupabaseRepositoryImpl(
    private val supabaseClient: SupabaseClient
): AuthRepository {
    override suspend fun registerUser(user: User) {
        TODO("Not yet implemented")
    }

    override suspend fun loginUser(user: User) {
        TODO("Not yet implemented")
    }

    override suspend fun closeSession(user: User) {
        TODO("Not yet implemented")
    }

    override suspend fun getAllUsers(): List<User> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUser(user: User) {
        TODO("Not yet implemented")
    }
}