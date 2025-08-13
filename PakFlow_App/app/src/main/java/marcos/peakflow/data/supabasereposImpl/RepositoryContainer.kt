package marcos.peakflow.data.supabasereposImpl

import marcos.peakflow.app.PeakFlowApp

object RepositoryContainer {
    private val supabase by lazy { PeakFlowApp.getClient() }

    val authRepository by lazy { AuthSupabaseRepositoryImpl(supabase) }
    val routeRepository by lazy { RouteSupabaseRepositoryImpl(supabase) }
    // Aquí se agregarán más repositorios
}