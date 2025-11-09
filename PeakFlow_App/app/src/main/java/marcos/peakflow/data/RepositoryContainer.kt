package marcos.peakflow.data

import android.content.Context
import marcos.peakflow.app.PeakFlowApp
import marcos.peakflow.data.services.AndroidGpsService
import marcos.peakflow.data.supabasereposimpl.AuthSupabaseRepositoryImpl
import marcos.peakflow.data.supabasereposimpl.RouteSupabaseRepositoryImpl
import marcos.peakflow.domain.cache.RoutePointsCache
import marcos.peakflow.domain.repository.AuthRepository
import marcos.peakflow.domain.repository.RouteRepository
import marcos.peakflow.domain.service.GpsService

object RepositoryContainer {
    private val supabase by lazy { PeakFlowApp.getClient() }
    private lateinit var appContext: Context

    // Repositorios y servicios disponibles globalmente
    lateinit var authRepository: AuthRepository
    lateinit var routeRepository: RouteRepository
    lateinit var gpsService: GpsService
    val routePointsCache: RoutePointsCache by lazy { RoutePointsCache() }

    fun init(context: Context) {
        appContext = context.applicationContext

        // inicialización "singletons"
        authRepository = AuthSupabaseRepositoryImpl(supabase)
        routeRepository = RouteSupabaseRepositoryImpl(supabase)
        gpsService = AndroidGpsService(appContext)
    }
}
