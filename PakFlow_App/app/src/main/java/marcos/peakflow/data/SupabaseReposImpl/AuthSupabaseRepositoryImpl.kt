package marcos.peakflow.data.SupabaseReposImpl

import android.util.Log
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.from
import marcos.peakflow.domain.model.user.User
import marcos.peakflow.domain.repository.AuthRepository

class AuthSupabaseRepositoryImpl(
    private val supabase: SupabaseClient
): AuthRepository {



    /**
     * Metodo para registrar un nuevo usuario
     * @param User: usuario
     * @param String: password
     */
    override suspend fun registerUser(user: User, password:String): Boolean {
        return try{
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
            true //Retorna true si el usuario ha sido logeado correctamente
        }catch(e: Exception){
            Log.e("AuthRepository", "Error en el registro: ${e.message}")
            false //Retorna false si ha habido un arror durante el proceso
        }
    }

    /**
     * Metodo para loguearse como usuario ya existente
     * @param email: String
     * @param String: password
     */
    override suspend fun loginUser(email: String, password: String): Boolean {
        return try {
            supabase.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            Log.d("logedUser", "El usuario ha sido logueado")
            true
        }catch (e: Exception){
            Log.e("AuthRepository", "Error en login: ${e.message}")
            false
        }
    }

    /**
     * Metodo para Cambiar la contraseña de una cuenta
     * @param email: String
     */
    override suspend fun resetPassword(email: String): Boolean {
        return try {
            supabase.auth.resetPasswordForEmail(email = email)
            Log.d("AuthRepository", "Contraseña cambiada correctamente")
            true
        }catch (e: Exception){
            Log.e("AuthRepository", "Error en reset password: ${e.message}")
            false
        }
    }

    /**
     * Metodo para cerrar sesion
     */
    override suspend fun closeSession():Boolean {
        return try {
            supabase.auth.signOut()
            Log.d("AuthRepository", "Sesión cerrada correctamente")
            true
        }catch (e: Exception){
            Log.e("AuthRepository", "Error en sign out: ${e.message}")
            false
        }
    }

    override suspend fun getAllUsers(): List<User> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUser(user: User): Boolean {
        TODO("Not yet implemented")
    }

    /**
     * Metodo para logearse a través de google
     */
    override suspend fun signInWithGoogle(): Boolean {
        return try {
            supabase.auth.signInWith(Google)
            true
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error en registro con google: ${e.message}")
            false
        }
    }
}