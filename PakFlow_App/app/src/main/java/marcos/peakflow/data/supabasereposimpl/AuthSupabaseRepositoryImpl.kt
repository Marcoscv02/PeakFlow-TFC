package marcos.peakflow.data.supabasereposimpl

import android.util.Log
import androidx.compose.animation.core.copy
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.from
import kotlinx.datetime.LocalDate
import marcos.peakflow.domain.model.user.User
import marcos.peakflow.domain.repository.AuthRepository

class AuthSupabaseRepositoryImpl(
    private val supabase: SupabaseClient
): AuthRepository {


    /**
     * Metodo para registrar un nuevo usuario
     * @param User: usuario
     * @param String: password
     *
     * @return Boolean
     */
    override suspend fun registerUser(user: User, password:String): Boolean {
        return try{
            // 1. Registro en autenticación
            val authResponse = supabase.auth.signUpWith(Email) {
                this.email = user.email!!
                this.password = password
            }
            Log.d("AuthRepository", "signUpWith completado para ${user.email}")

            // 2. Obtener el usuario recién registrado de la sesión actual.
            // Después de un signUp exitoso (con confirmación desactivada), el usuario debería estar en la sesión.
            val sessionUser = supabase.auth.currentUserOrNull()
                ?: throw Exception("No se pudo obtener la sesión del usuario inmediatamente después del registro.")

            val userId = sessionUser.id
            Log.d("AuthRepository", "Usuario obtenido de la sesión con ID: $userId")

            //3. Insertar el resto de datos en la tabla para DB
            supabase.from("users").insert(
                mapOf(
                    "id" to userId,
                    "name" to user.username,
                    "birthdate" to user.birthdate.toString(),
                    "gender" to user.gender
                )
            )
            Log.d("AuthRepository", "Los datos del usuario han sido guardados en la base de datos")
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
     *
     * @return Boolean
     */
    override suspend fun loginUser(email: String, password: String): Boolean {
        return try {
            supabase.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }
            Log.d("AuthRepository", "El usuario ha sido logueado")
            true
        }catch (e: Exception){
            Log.e("AuthRepository", "Error en login: ${e.message}")
            false
        }
    }


    /**
     * Metodo con el que se obtiene el usuario logueado actualmente a través del token de sesión
     * @return User
     */
    override suspend fun getCurrentUser(): Result<User> {
        val sessionUser = supabase.auth.currentUserOrNull()
            ?: return Result.failure(Exception("No hay un usuario en la sesión actual."))

        val userId = sessionUser.id
        val userEmail = sessionUser.email

        return try {
            // 1. Decodifica el usuario desde la DB. Gracias a @Transient, esto ya no fallará.
            val userFromDB = supabase.from("users")
                .select {
                    filter { eq("id", userId) }
                    limit(1)
                }.decodeSingleOrNull<User>()

            if (userFromDB == null) {
                val errorMsg = "Usuario con ID: $userId no encontrado en la base de datos."
                Log.w("AuthRepository", errorMsg)
                return Result.failure(NoSuchElementException(errorMsg))
            }

            // 2. Crea una nueva instancia con el email añadido y RETÓRNALA.
            val finalUser = userFromDB.copy(email = userEmail)

            Log.d("AuthRepository", "Usuario obtenido de DB y combinado con email: ${finalUser.id}")
            Result.success(finalUser)

        } catch (e: Exception) {
            Log.e("AuthRepository", "Error al obtener el usuario de la base de datos: ${e.message}", e)
            Result.failure(e)
        }
    }



    /**
     * Metodo para Cambiar la contraseña de una cuenta
     * @param email: String
     * @return Boolean
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
     * @return Boolean
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


    /**
     * Metodo que devuelve una lista de todos los usuarios registrados en la base de datos
     * @return List
     */
    override suspend fun getAllUsers(): List<User> {
        TODO("Not yet implemented")
    }


    /**
     * Metodo para borrar un usuario de la base de datos
     * @param User user
     * @return Boolean
     */
    override suspend fun deleteUser(user: User): Boolean {
        TODO("Not yet implemented")
    }


    /**
     * Metodo para logearse a través de google
     * @return Boolean
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