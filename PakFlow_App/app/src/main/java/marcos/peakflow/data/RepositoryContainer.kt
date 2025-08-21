package marcos.peakflow.data

import marcos.peakflow.app.PeakFlowApp
import marcos.peakflow.data.supabasereposimpl.AuthSupabaseRepositoryImpl
import marcos.peakflow.data.supabasereposimpl.RouteSupabaseRepositoryImpl

object RepositoryContainer {
    private val supabase by lazy { PeakFlowApp.getClient() }

    val authRepository by lazy { AuthSupabaseRepositoryImpl(supabase) }
    val routeRepository by lazy { RouteSupabaseRepositoryImpl(supabase) }
    // Aquí se agregarán más repositorios
}