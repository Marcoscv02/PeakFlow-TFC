package marcos.peakflow.data.supabasereposimpl

import android.util.Log
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import marcos.peakflow.domain.model.route.Route
import marcos.peakflow.domain.model.route.RoutePoint
import marcos.peakflow.domain.repository.RouteRepository

class RouteSupabaseRepositoryImpl(
    private val supabase: SupabaseClient
): RouteRepository {

    /**
     * Guarda una nueva ruta en la base de datos.
     * El `user_id` se asocia automáticamente al usuario autenticado actual
     * mediante el valor por defecto `auth.uid()` en la base de datos.
     *
     * @param route El objeto [Route] que contiene la información de la ruta a guardar.
     *              El campo `userId` de este objeto se ignora, ya que la base de datos
     *              lo establece automáticamente.
     * @return `true` si la ruta se guardó exitosamente, `false` en caso contrario.
     */
    override suspend fun saveRoute(route: Route): Result<Boolean> {
        //Obtener el id del usuario actual
        val currentUserId = supabase.auth.currentUserOrNull()?.id
            ?: return Result.failure(Exception("Usuario no autenticado."))

       val routeCopy = route.copy(userId = currentUserId)

        return try {
            Log.d("RouteSupabaseRepo", "Guardando ruta: $routeCopy")

            //Insertar ruta en la DB
            val savedRoute = supabase.from("run_route").insert(routeCopy) //.decodeSingleOrNull<Route>()

            Log.d("RouteSupabaseRepo", "Ruta insertada: ${savedRoute.data}")


            val savedRouteId =  "30b56d9f-36be-4c36-81a2-56f9dfc5cb52"// savedRoute?.id //Obtener el id de la ruta insertada

            Log.d("RouteSupabaseRepo", "Ruta insertada con ID: $savedRouteId")

            if (savedRouteId != null) {
                Log.i("RouteSupabaseRepo", "Ruta guardada exitosamente con ID: $savedRouteId.")

                if (!route.points.isNullOrEmpty()) {
                    addPoints(savedRouteId , route.points)
                } else {
                    Log.w("RouteSupabaseRepo", "No hay puntos en la caché para añadir a la ruta $savedRouteId")
                }

                Result.success(true) //Devolver resultado exitoso

            } else { // Este bloque se ejecuta si la inserción fue bien pero la decodificación falló.
                Result.failure(Exception("La ruta se guardó, pero no se pudo obtener el ID de vuelta."))
            }

        } catch (e: RestException) {
            Log.e("RouteSupabaseRepo", "Error RestException al guardar ruta: ${e.message} | Código: ${e.statusCode}", e)
            Result.failure(e)
        } catch (e: Exception) {
            Log.e("RouteSupabaseRepo", "Error genérico al guardar ruta: ${e.message}", e)
            Result.failure(e)
        }
    }


    /**
     * Elimina una ruta de la base de datos.
     * @param String routeId
     * @return Boolean
     */
    override suspend fun deleteRoute(routeId: String): Result<Boolean> {
        return try {
            //Borra los RoutePoints asociados a esa ruta
            supabase.from("run_route_point").delete {
                filter { eq("route_id", routeId) }
            }
            Log.d("RouteSupabaseRepo", "Puntos de ruta borrados para ruta: $routeId")

            // Borra la ruta
            supabase.from("run_route").delete {
                filter { eq("id", routeId) }
            }
            Log.i("RouteSupabaseRepo", "Route borrada correctamente: $routeId")
            Result.success(true)

        } catch (e: RestException) {
            Log.e(
                "RouteSupabaseRepo",
                "Error RestException en el borrado de la ruta $routeId: ${e.message} | Code: ${e.statusCode}"
            )
            Result.failure(e)

        } catch (e: Exception) {
            Log.e(
                "RouteSupabaseRepo",
                "Error genérico en el borrado de la ruta $routeId: ${e.message}"
            )
            Result.failure(e)
        }
    }


    /**
     * Obtiene todas las rutas de un usuario de la base de datos.
     * @param String userId
     * @return List<Route>
     */
    override suspend fun getUserRoutes(): Result<List<Route>> { // Nombre de la interfaz original
        val currentUserId = supabase.auth.currentUserOrNull()?.id
            ?: return Result.failure(Exception("Usuario no autenticado."))

        return try {
            // Obtener rutas base (sin puntos)
            val baseRoutes = supabase.from("run_route")
                .select {
                    filter { eq("user_id", currentUserId) }
                    order("start_time", Order.DESCENDING)
                }
                .decodeList<Route>()

            // Para cada ruta base, obtener sus puntos y crear el objeto Route final
            val routesWithPoints = mutableListOf<Route>()
            for (baseRoute in baseRoutes) {
                if (baseRoute.id == null) { //Aunque sea tecnicamente imposible porque es clave primaria, debe ponerse ya que su tipo de dato es string?
                    Log.w("RouteSupabaseRepo", "Ruta recuperada sin ID, saltando: $baseRoute")
                    continue
                }
                val pointsResult = getPoints(baseRoute.id)

                val routeWithPoints = if (pointsResult.isSuccess) {
                    baseRoute.copy(points = pointsResult.getOrNull() ?: emptyList())
                } else {
                    Log.w("RouteSupabaseRepo", "No se pudieron obtener los puntos para la ruta ${baseRoute.id}: ${pointsResult.exceptionOrNull()?.message}")
                    baseRoute.copy(points = emptyList()) // Devolver ruta con puntos vacíos
                }
                routesWithPoints.add(routeWithPoints)
            }

            Log.i("RouteSupabaseRepo", "Se obtuvieron ${routesWithPoints.size} rutas para el usuario $currentUserId.")
            Result.success(routesWithPoints)

        } catch (e: RestException) {
            Log.e("RouteSupabaseRepo", "Error RestException al obtener rutas para el usuario $currentUserId: ${e.message}", e)
            Result.failure(e)
        } catch (e: kotlinx.serialization.SerializationException) {
            Log.e("RouteSupabaseRepo", "Error de serialización al obtener rutas para el usuario $currentUserId: ${e.message}", e)
            Result.failure(e)
        } catch (e: Exception) {
            Log.e("RouteSupabaseRepo", "Error genérico al obtener rutas para el usuario $currentUserId: ${e.message}", e)
            Result.failure(e)
        }
    }


    /**
     * Obtiene una ruta por su ID de la base de datos.()
     * @param String routeId
     * @return Route
     */
    override suspend fun getRouteById(routeId: String): Result<Route> {
        return try {
            // 1. Obtener los metadatos de la ruta
            // Asumimos que Route.kt está preparado para la deserialización directa
            val route = supabase.from("run_route")
                .select {
                    filter { eq("id", routeId) }
                    limit(1) // Nos aseguramos de que solo esperamos un resultado
                }
                .decodeSingleOrNull<Route>()

            if (route == null) {
                Log.w("RouteSupabaseRepo", "Ruta no encontrada con id: $routeId")
                return Result.failure(NoSuchElementException("Ruta no encontrada con id: $routeId"))
            }

            // 2. Obtener los puntos para esa ruta
            // getPoints ya debería devolver Result<List<RoutePoint>>
            val pointsResult = getPoints(routeId) // Llama al metodo getPoints

            // 3. Combinar la ruta con sus puntos
            val routeWithPoints = if (pointsResult.isSuccess) {
                route.copy(points = pointsResult.getOrNull() ?: emptyList())
            } else {
                Log.w("RouteSupabaseRepo", "No se pudieron obtener los puntos para la ruta $routeId: ${pointsResult.exceptionOrNull()?.message}")
                // Decidir si devolver la ruta sin puntos o fallar si los puntos son cruciales
                // Por ahora, devolvemos la ruta con puntos vacíos o nulos si falló la obtención de puntos
                route.copy(points = emptyList()) // O route, si el campo points en Route es nullable y quieres que sea null
            }

            Log.i("RouteSupabaseRepo", "Ruta obtenida por id $routeId con ${routeWithPoints.points?.size ?: 0} puntos.")
            Result.success(routeWithPoints)

        } catch (e: RestException) {
            Log.e("RouteSupabaseRepo", "Error RestException al obtener ruta por id $routeId: ${e.message}", e)
            Result.failure(e)
        } catch (e: kotlinx.serialization.SerializationException) {
            Log.e("RouteSupabaseRepo", "Error de serialización al obtener ruta por id $routeId: ${e.message}", e)
            Result.failure(e)
        } catch (e: Exception) {
            Log.e("RouteSupabaseRepo", "Error genérico al obtener ruta por id $routeId: ${e.message}", e)
            Result.failure(e)
        }
    }


    /**
     * Añade los puntos que componen una ruta a la base de datos.
     * @param String routeId
     * @param List<RoutePoint> points
     * @return Result<Unit>
     */
    override suspend fun addPoints(routeId: String, points: List<RoutePoint>): Result<Unit> {
        if (points.isEmpty()) {
            Log.i("RouteSupabaseRepo", "No points to add for routeId: $routeId")
            return Result.success(Unit)
        }
        return try {
            val pointsData = points.map { point ->
                    point.copy(routeId = routeId)
            }


            supabase.from("run_route_point").insert(values = pointsData)
            Log.i("RouteSupabaseRepo", "${points.size} points added for routeId: $routeId")
            Result.success(Unit)
        } catch (e: RestException) {
            Log.e(
                "RouteSupabaseRepo",
                "Error RestException adding points for route $routeId: ${e.message} | Code: ${e.statusCode}"
            )
            Result.failure(e)
        } catch (e: Exception) {
            Log.e(
                "RouteSupabaseRepo",
                "Generic error adding points for route $routeId: ${e.message}"
            )
            Result.failure(e)
        }
    }


    /**
     * Obtiene los puntos de una ruta de la base de datos.
     * @param String routeId
     * @return Result<List<RoutePoint>>
     */
    override suspend fun getPoints(routeId: String): Result<List<RoutePoint>> {
        return try {
            val points = supabase.from("run_route_point") // Nombre correcto de la tabla de puntos
                .select {
                    filter { eq("route_id", routeId) }
                    order("created_at", Order.ASCENDING) // Asumiendo que 'created_at' es el timestamp del punto
                }
                .decodeList<RoutePoint>() // Deserialización directa
            Log.d("RouteSupabaseRepo", "Obtenidos ${points.size} puntos para la ruta $routeId")
            Result.success(points)
        } catch (e: RestException) {
            Log.e("RouteSupabaseRepo", "RestException en getPoints para $routeId: ${e.message}", e)
            Result.failure(e)
        } catch (e: kotlinx.serialization.SerializationException) {
            Log.e("RouteSupabaseRepo", "SerializationException en getPoints para $routeId: ${e.message}", e)
            Result.failure(e)
        } catch (e: Exception) {
            Log.e("RouteSupabaseRepo", "Exception en getPoints para $routeId: ${e.message}", e)
            Result.failure(e)
        }
    }


    /**
     * Obtiene la última ruta guardada por el usuario actual.
     * @return Result<Route>
     */
    override suspend fun getLastRouteSaved(): Result<Route> {
        val currentUserId = supabase.auth.currentUserOrNull()?.id
            ?: return Result.failure(Exception("Usuario no autenticado."))

        return try {
            val route = supabase.from("run_route")
                .select {
                    filter { eq("user_id", currentUserId) }
                    order("created_at", Order.DESCENDING)
                    limit(1)
                }
                .decodeSingleOrNull<Route>() // Directo a Route

            if (route != null) {
                // Si necesitas los puntos también para la "última ruta guardada":
                val pointsResult = getPoints(route.id!!) // Asumiendo que route.id no será null aquí
                val routeWithPoints = if (pointsResult.isSuccess) {
                    route.copy(points = pointsResult.getOrNull() ?: emptyList())
                } else {
                    Log.w("RouteSupabaseRepo", "No se pudieron obtener los puntos para la última ruta: ${route.id}")
                    route // Devuelve la ruta sin puntos o con puntos nulos/vacíos
                }
                Result.success(routeWithPoints)
            } else {
                Result.failure(NoSuchElementException("No se encontró la última ruta guardada."))
            }
        } catch (e: RestException) {
            Log.e("RouteSupabaseRepo", "RestException en getLastRouteSaved: ${e.message}", e)
            Result.failure(e)
        } catch (e: kotlinx.serialization.SerializationException) {
            Log.e("RouteSupabaseRepo", "SerializationException en getLastRouteSaved: ${e.message}", e)
            Result.failure(e)
        } catch (e: Exception) {
            Log.e("RouteSupabaseRepo", "Exception en getLastRouteSaved: ${e.message}", e)
            Result.failure(e)
        }
    }
}