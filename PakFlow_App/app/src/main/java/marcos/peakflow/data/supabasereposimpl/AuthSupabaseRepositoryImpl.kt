package marcos.peakflow.data.supabasereposimpl

import android.util.Log
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
            Log.d("AuthRepository", "El usuario ha sido registrado")

            // 2. Verificar si el registro fue exitoso
            val userId = authResponse?.id?: throw Exception("User registration failed in repository")

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
    override suspend fun getCurrentUser(): User? {
        return try {
            val sessionUser = supabase.auth.currentUserOrNull()
            sessionUser?.let {
                User(
                    id = it.id,
                    email = it.email,
                    username = it.userMetadata?.get("name") as? String,
                    birthdate = it.userMetadata?.get("birthdate") as? LocalDate?,
                    gender = it.userMetadata?.get("gender") as? String
                )
            }
        } catch (e: Exception) {
            null
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