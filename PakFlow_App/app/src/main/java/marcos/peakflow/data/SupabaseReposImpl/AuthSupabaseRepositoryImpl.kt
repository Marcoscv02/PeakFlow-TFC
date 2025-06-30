package marcos.peakflow.data.SupabaseReposImpl

import android.util.Log
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.from
import marcos.peakflow.domain.model.User
import marcos.peakflow.domain.repository.AuthRepository

class AuthSupabaseRepositoryImpl(
    private val supabase: SupabaseClient
): AuthRepository {

    /**
     * Metodo para registrar un nuevo usuario
     * @param User: usuario
     * @param String: password
     */
    override suspend fun registerUser(user: User, password:String) {
        // 1. Registro en autenticación
        val authResponse = supabase.auth.signUpWith(Email) {
            this.email = user.email!!
            this.password = password
        }
        Log.d("UserRegister", "El usuario ha sido registrado")

        // 2. Verificar si el registro fue exitoso
        val userId = authResponse?.id ?: throw Exception("User registration failed in repository")

        //3. Insertar el resto de datos en la tabla para DB
        supabase.from("user").insert(
            mapOf(
                "id" to userId,
                "name" to user.username,
                "birthdate" to user.birthdate.toString(),
                "gender" to user.gender
            )
        )
        Log.d("UserDataSave", "Los datos del usuario han sido guardados en la base de datos")
    }

    override suspend fun loginUser(email: String, password: String) {
        supabase.auth.signInWith(Email) {
            this.email = email
            this.password = password
        }
        Log.d("logedUser", "El usuario ha sido logueado")
    }

    override suspend fun resetPassword(email: String) {
        supabase.auth.resetPasswordForEmail(email = email)
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