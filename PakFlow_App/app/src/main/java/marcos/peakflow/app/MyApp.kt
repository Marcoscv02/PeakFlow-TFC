package marcos.peakflow.app

import android.app.Application
import android.util.Log
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        val supabase: SupabaseClient = createSupabaseClient(
            supabaseUrl = "https://zyjdewgpqigihayqjstw.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Inp5amRld2dwcWlnaWhheXFqc3R3Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDUyMjYwMDYsImV4cCI6MjA2MDgwMjAwNn0.1zpBC3NKuzwrSrtq04hYrc964mu_FsADRKUxIplcT8w"
        ) {
            install(Auth)
            install(Postgrest)
            Log.d("Conection", "Conexion establecida con supabase")
        }
    }
}