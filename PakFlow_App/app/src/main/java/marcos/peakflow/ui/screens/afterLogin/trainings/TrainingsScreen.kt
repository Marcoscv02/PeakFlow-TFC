package marcos.peakflow.ui.screens.afterLogin.trainings

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import marcos.peakflow.R
import marcos.peakflow.domain.model.route.Route
import marcos.peakflow.domain.util.formatInstant
import marcos.peakflow.domain.util.formatTime
import marcos.peakflow.domain.util.msToMinPerKm
import marcos.peakflow.ui.components.BottomNavBar
import marcos.peakflow.ui.components.Screen
import marcos.peakflow.ui.components.TripleTopAppBar
import marcos.peakflow.ui.screens.PeakFlowViewModelFactory
import marcos.peakflow.ui.theme.Gray
import org.maplibre.android.MapLibre
import org.maplibre.android.WellKnownTileServer
import org.maplibre.android.annotations.PolylineOptions
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.geometry.LatLngBounds
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.Style
import kotlin.time.*


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TrainingsScreen(
    navigateToHome: () -> Unit,
    navigateToPlay: () -> Unit,
    navigateToRoute: () -> Unit,
    navigateBack: () -> Unit,
    navigateTrainingDetail: (String) -> Unit
){
    val viewModel: TrainingsViewModel = viewModel(factory = PeakFlowViewModelFactory())
    val uiState : TrainingsUIState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TripleTopAppBar(
                title = stringResource(R.string.trainingsTitleScreen),
                onLeftClick = navigateBack,
                leftIcon = R.drawable.back,
                onMidleClick = {},
                middleIcon = R.drawable.search,
                onRightClick = {},
                rightIcon = R.drawable.filter
            )
        },
        bottomBar = {
            BottomNavBar(
                currentScreen = Screen.Training.route,
                navigateToHome = navigateToHome,
                navigateToPlay = navigateToPlay,
                navigateToRoute = navigateToRoute,
                navigateToTraining = {}
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(0.dp, 40.dp)
        ) {
            when {
                uiState.isLoading -> {

                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(50.dp)
                    )
                }
                uiState.error != null -> {
                    Text(
                        text = "Error: ${uiState.error}",
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(uiState.routes) { route ->
                            TrainingItem(
                                route = route,
                                onItemClick = {navigateTrainingDetail (route.id.toString())},
                                onDeleteClick ={
                                    if (route.id != null) {
                                        viewModel.deleteRoute(route.id)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }

    }
}

/**
 * Item de entrenamiento en la pantalla de entrenamiento
 * @param Route
 * @param Unit
 * @param Unit
 */
@OptIn(ExperimentalTime::class)
@Composable
fun TrainingItem(
    route: Route,
    onItemClick: () -> Unit,
    onDeleteClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable{onItemClick()},
        colors = CardDefaults.cardColors(containerColor = Gray)
    ) {
        Column {
            // Sección de datos del entrenamiento
            Column(modifier = Modifier.padding(16.dp)) {
                // Fecha
                Text(
                    text = formatInstant(route.startTime),
                    color = Color.LightGray,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Nombre del entrenamiento
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.running),
                        contentDescription = "Running icon",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = route.name ?: "Entrenamiento",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.weight(1f))

                    // 2. ENGADE A ICONA DA PAPELEIRA
                    IconButton(onClick = onDeleteClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.delete),
                            contentDescription = "Eliminar entrenamiento",
                            tint = Color.White.copy(alpha = 0.7f),
                            modifier = Modifier.size(22.dp)
                        )
                    }

                }

                Spacer(modifier = Modifier.height(16.dp))

                // Métricas (Distancia, Tiempo, Ritmo)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MetricItem(label = "Distancia", value = "%.2f km".format(route.distance / 1000))
                    MetricItem(label = "Tiempo", value = formatTime(route.durationSec * 1000))
                    MetricItem(label = "Ritmo", value = msToMinPerKm(route.avgSpeed).toString())
                }
            }

            // Mapa estático con la ruta
            StaticRouteMap(route = route)
        }
    }
}


/**
 * Bloque individual que componen el DataPanel
 * @param String: label
 * @param String: value
 */
@Composable
fun MetricItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.Start) {
        Text(text = label, color = Color.LightGray, fontSize = 14.sp)
        Text(text = value, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
    }
}

/**
 * Mapa estático con la ruta
 * @param Route
 */
@Composable
fun StaticRouteMap(route: Route) {
    val context = LocalContext.current
    // Clave dummy, ya que tu ViewModel de PlayScreen la gestiona. Aquí podrías obtenerla de otro sitio si fuera necesario.
    val mapTilerKey = "ayEMMbyitB7UOUpklBr6"
    val styleUrl = "https://api.maptiler.com/maps/hybrid/style.json?key=$mapTilerKey"

    // Solo muestra el mapa si hay puntos en la ruta
    if (route.points.isNullOrEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color.DarkGray),
            contentAlignment = Alignment.Center
        ) {
            Text("No hay datos de mapa para esta ruta", color = Color.White)
        }
        return
    }

    AndroidView(
        factory = { ctx ->
            MapLibre.getInstance(ctx, mapTilerKey, WellKnownTileServer.MapTiler)
            MapView(ctx).apply {
                getMapAsync { mapLibreMap ->
                    mapLibreMap.setStyle(Style.Builder().fromUri(styleUrl)) {
                        // Desactivar gestos para que sea estático
                        mapLibreMap.uiSettings.setAllGesturesEnabled(false)

                        val latLngPoints = route.points.map { LatLng(it.latitude, it.longitude) }

                        // Dibuja la polilínea
                        mapLibreMap.addPolyline(
                            PolylineOptions()
                                .addAll(latLngPoints)
                                .color(android.graphics.Color.BLUE)
                                .width(5f)
                        )

                        // Centra la cámara para que se vea toda la ruta
                        if (latLngPoints.size > 1) {
                            val bounds = LatLngBounds.Builder()
                                .includes(latLngPoints)
                                .build()
                            mapLibreMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50))
                        } else if (latLngPoints.isNotEmpty()) {
                            mapLibreMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLngPoints.first(), 15.0))
                        }
                    }
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    )
}



