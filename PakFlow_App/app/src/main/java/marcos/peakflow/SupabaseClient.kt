package marcos.peakflow


import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest


object SupabaseClient {
    val supabase = createSupabaseClient(
        supabaseUrl = "https://zyjdewgpqigihayqjstw.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Inp5amRld2dwcWlnaWhheXFqc3R3Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDUyMjYwMDYsImV4cCI6MjA2MDgwMjAwNn0.1zpBC3NKuzwrSrtq04hYrc964mu_FsADRKUxIplcT8w"
    ) {
        install(Auth)
        install(Postgrest)
        //install other modules
    }
}