package marcos.peakflow.app

import android.app.Application
import android.util.Log
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import marcos.peakflow.BuildConfig
import marcos.peakflow.data.RepositoryContainer

class   PeakFlowApp : Application() {

    companion object {
        private lateinit var instance: PeakFlowApp

        fun getClient(): SupabaseClient {
            return instance.supabaseClient
        }
    }

    private lateinit var supabaseClient: SupabaseClient


    override fun onCreate() {
        super.onCreate()
        instance = this


        // Inicialización segura
        try {
            supabaseClient = createSupabaseClient(
                supabaseUrl = "https://zyjdewgpqigihayqjstw.supabase.co",
                supabaseKey = BuildConfig.Supabase_key
            ) {
                install(Auth)
                install(Postgrest)
                Log.d("Connection", "Conexión establecida con Supabase")
            }
            //Importante inicializar contenedor de repositorios después del supabase client
            RepositoryContainer.init(this)

        } catch (e: Exception) {
            Log.e("SupabaseInit", "Error al inicializar Supabase: ${e.message}")
        }
    }
}