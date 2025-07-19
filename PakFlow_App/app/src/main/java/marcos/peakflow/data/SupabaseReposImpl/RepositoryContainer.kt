package marcos.peakflow.data.SupabaseReposImpl

import marcos.peakflow.app.PeakFlowApp

object RepositoryContainer {
    private val supabase by lazy { PeakFlowApp.getClient() }

    val authRepository by lazy { AuthSupabaseRepositoryImpl(supabase) }
    // Aquí se agregarán más repositorios
}