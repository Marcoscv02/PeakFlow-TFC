package marcos.peakflow.data.SupabaseReposImpl

import marcos.peakflow.app.PeakFlowApp

object RepositoryContainer {
    private val supabase by lazy { PeakFlowApp.supabaseClient }

    val authRepository by lazy { AuthSupabaseRepositoryImpl(supabase) }
    // Aqí se agregarán más repositorios
}