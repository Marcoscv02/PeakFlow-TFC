@file:Suppress("UNUSED_EXPRESSION")

package marcos.peakflow.ui.screens.afterLogin.play

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import marcos.peakflow.R
import marcos.peakflow.ui.components.BottomNavBar
import marcos.peakflow.ui.components.Screen
import marcos.peakflow.ui.components.StandardTopAppBar
import marcos.peakflow.ui.screens.PeakFlowViewModelFactory
import marcos.peakflow.ui.theme.Black
import marcos.peakflow.ui.theme.Gray
import org.maplibre.android.MapLibre
import org.maplibre.android.WellKnownTileServer
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.location.LocationComponentActivationOptions
import org.maplibre.android.location.LocationComponentOptions
import org.maplibre.android.location.engine.LocationEngineRequest
import org.maplibre.android.location.modes.CameraMode
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.Style

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PlayScreen(
    navigateToHome: () -> Unit,
    navigateToRoute: () -> Unit,
    navigateToTraining: () -> Unit,
    navigateBack: () -> Unit
) {
    val viewModel : PlayViewModel = viewModel(factory = PeakFlowViewModelFactory())

    val context = LocalContext.current
    val activity = context as? Activity

    val isRunning = viewModel.isRunning.collectAsState()
    val routeStarted =viewModel.isRecording.collectAsState()

    val elapsed by viewModel.elapsedTime.collectAsState()


    //Lanza el chequeo de permisos cuando la vista se infle por primera vez
    LaunchedEffect(Unit) {
        activity?.let {
            viewModel.checkOrRequestPermissions(it)
        }
    }

    val hasPermission by viewModel.hasLocationPerm.collectAsState()

    Scaffold(
        topBar = { StandardTopAppBar(
            title = stringResource(R.string.playTitleScreen),
            onLeftClick = { navigateBack ()},
            leftIcon = R.drawable.back,
            onRightClick = {  },
            rightIcon = R.drawable.save
        ) },
        bottomBar = {
            BottomNavBar(
                currentScreen = Screen.Play.route,
                navigateToHome = navigateToHome,
                navigateToPlay = {},
                navigateToRoute = navigateToRoute,
                navigateToTraining = navigateToTraining
            )
        }
    ) {
        //Mapa
        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(Black)
        ){
            if (hasPermission) {
                MapLibreLocationView(viewModel)
            } else {
                Text(
                    text = stringResource(id = R.string.EnableLocationRequest),
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(20.dp)
                        .padding(vertical = 60.dp),
                    fontSize = 18.sp
                )
            }

            TopRecordingAppBar(
                routeStarted = routeStarted.value,
                isRunning = isRunning.value,
                onPlayPauseClick = { viewModel.toggleRunning()
                })
            //panel de datos en tiempo real
            DataPanel(viewModel)

        }
    }
}


/**
 * Metodo que crea la la vista de mapa embebida en la UI
 */
@SuppressLint("MissingPermission")
@Composable
fun MapLibreLocationView(
    viewModel: PlayViewModel
) {
    val context = LocalContext.current
    val mapTilerKey = viewModel.mapTilerKey
    val styleUrl = viewModel.styleUrl
    val userLocation by viewModel.userLocation.collectAsState()

    // guardamos la referencia del MapLibreMap para poder mover la cámara desde el update block
    val mapRef = remember { mutableStateOf<org.maplibre.android.maps.MapLibreMap?>(null) }

    // Cuando el Composable desaparezca, detenemos las actualizaciones del ViewModel
    DisposableEffect(Unit) {
        onDispose {
            viewModel.stopLocationUpdates()
        }
    }

    AndroidView(
        factory = { ctx ->
            // Inicializa MapLibre (solo si no se hizo)
            MapLibre.getInstance(ctx, mapTilerKey, WellKnownTileServer.MapTiler)

            val mapView = MapView(ctx)
            mapView.getMapAsync { mapLibreMap ->
                // Guardamos la referencia
                mapRef.value = mapLibreMap

                // Establece el estilo
                mapLibreMap.setStyle(Style.Builder().fromUri(styleUrl)) { style ->
                    // Configuración visual del LocationComponent (la parte UI)
                    val options = LocationComponentOptions.builder(ctx)
                        .pulseEnabled(true)
                        .pulseColor(android.graphics.Color.CYAN)
                        .foregroundTintColor(android.graphics.Color.BLUE)
                        .build()

                    val activationOptions = LocationComponentActivationOptions.builder(ctx, style)
                        .locationComponentOptions(options)
                        .useDefaultLocationEngine(true)
                        .locationEngineRequest(
                            LocationEngineRequest.Builder(750L)
                                .setFastestInterval(750L)
                                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                                .build()
                        )
                        .build()

                    // Activa el LocationComponent (UI)
                    mapLibreMap.locationComponent.activateLocationComponent(activationOptions)
                    mapLibreMap.locationComponent.isLocationComponentEnabled = true
                    mapLibreMap.locationComponent.cameraMode = CameraMode.TRACKING

                    // Crea el engine de la UI y se lo pasa al ViewModel para suscribirse.
                    val engine = mapLibreMap.locationComponent.locationEngine
                        ?: org.maplibre.android.location.engine.LocationEngineDefault.getDefaultLocationEngine(ctx)
                    viewModel.startLocationUpdates(engine)

                    // Si ya hay una ubicación conocida en el componente, centrar cámara (arranque rápido)
                    val lastLoc = mapLibreMap.locationComponent.lastKnownLocation
                    if (lastLoc != null) {
                        val userLatLng = LatLng(lastLoc.latitude, lastLoc.longitude)
                        val cameraPosition = CameraPosition.Builder()
                            .target(userLatLng)
                            .zoom(17.0)
                            .build()
                        mapLibreMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                    }
                }
            }
            mapView
        },
        update = { mapView ->
            // Cada recomposición: si hay mapa y nueva userLocation -> mover cámara suavemente
            val map = mapRef.value
            val loc = userLocation
            if (map != null && loc != null) {
                val camera = CameraPosition.Builder()
                    .target(loc)
                    .zoom(17.0)
                    .build()
                map.animateCamera(CameraUpdateFactory.newCameraPosition(camera), 500)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp)
    )
}

@Composable
fun TopRecordingAppBar(
    routeStarted: Boolean,
    isRunning: Boolean,
    onPlayPauseClick: () -> Unit
){
    Surface(
        color = Gray,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 13.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = {null}) {
                Icon(
                    painter = painterResource(id = R.drawable.running),
                    contentDescription = "leftIcon",
                    tint = Color.LightGray,
                    modifier = Modifier.size(80.dp)
                )
            }

            Text(
                text = "Carrera",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.LightGray,
            )

            IconButton(onClick = onPlayPauseClick) {
                Icon(
                    painter = if (isRunning) painterResource(R.drawable.play) else painterResource(R.drawable.pause),
                    contentDescription = "RightIcon",
                    tint = if (isRunning) Color.Green else Color.Red,
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    }
}

@Composable
fun DataPanel(
    viewModel: PlayViewModel
) {
    val elapsed by viewModel.elapsedTime.collectAsState()
    val tiempo = formatTime(elapsed)


    Column(
        modifier = Modifier
            .height(300.dp)
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .padding(horizontal = 20.dp)
        ,
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        // Distancia
        StatBlock(label = "Distancia", value = "7.2 km", fontSize = 34.sp)

        // Separador horizontal
        Divider(
            modifier = Modifier
                .fillMaxWidth(),
            color = Color.Gray.copy(alpha = 0.3f),
            thickness = 1.dp
        )

        // Tiempo y Ritmo
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatBlock(label = "Tiempo", value = tiempo)
            StatBlock(label = "Ritmo", value = "5:23 /km")
        }

        // Elevación y ppm
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatBlock(label = "Desnivel +", value = "120 m")
            StatBlock(label = "Frecuencia", value = "145 ppm")
        }



    }
}

@Composable
fun StatBlock(label: String, value: String, fontSize: TextUnit = 24.sp) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, fontSize = 12.sp, color = Color.Gray)
        Text(text = value, fontSize = fontSize, fontWeight = FontWeight.Bold, color = Color.White)
    }
}

fun formatTime(ms: Long): String {
    val totalSec = ms / 1000
    val h = totalSec / 3600
    val m = (totalSec % 3600) / 60
    val s = totalSec % 60
    return String.format("%02d:%02d:%02d", h, m, s)
}


