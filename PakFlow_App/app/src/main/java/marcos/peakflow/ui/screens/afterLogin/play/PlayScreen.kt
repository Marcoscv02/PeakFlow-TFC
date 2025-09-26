@file:Suppress("UNUSED_EXPRESSION", "DEPRECATION")

package marcos.peakflow.ui.screens.afterLogin.play

import android.annotation.SuppressLint
import android.app.Activity
import android.widget.Toast
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
import marcos.peakflow.domain.model.route.RoutePoint
import marcos.peakflow.domain.util.calculateCurrentSpeed
import marcos.peakflow.domain.util.calculateDistance
import marcos.peakflow.domain.util.calculateElevationGain
import marcos.peakflow.domain.util.formatTime
import marcos.peakflow.domain.util.msToMinPerKm
import marcos.peakflow.ui.components.BottomNavBar
import marcos.peakflow.ui.components.Screen
import marcos.peakflow.ui.components.StandardTopAppBar
import marcos.peakflow.ui.screens.PeakFlowViewModelFactory
import marcos.peakflow.ui.theme.Black
import marcos.peakflow.ui.theme.Gray
import org.maplibre.android.MapLibre
import org.maplibre.android.WellKnownTileServer
import org.maplibre.android.annotations.PolylineOptions
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

    val isRunning by viewModel.isRunning.collectAsState()
    val isRecording by viewModel.isRecording.collectAsState()
    val hasPermission by viewModel.hasLocationPerm.collectAsState()
    val mapReady by viewModel.mapReady.collectAsState()

    var showSaveDialog by remember { mutableStateOf(false) }

    //puntos de la ruta
    val routePoints by viewModel.routePoints.collectAsState()

    //Datos en tiempo real


    //Lanza el chequeo de permisos cuando la vista se infle por primera vez
    LaunchedEffect(Unit) {
        activity?.let {
            viewModel.checkOrRequestPermissions(it)
        }
    }


    Scaffold(

        topBar = { StandardTopAppBar(
            title = stringResource(R.string.playTitleScreen),
            onLeftClick = { navigateBack ()},
            leftIcon = R.drawable.back,
            onRightClick = {
                if (isRecording) {
                    showSaveDialog = true
                } else {
                    Toast.makeText(
                        context,
                        context.getString(R.string.mustStartRoute),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            },
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

            //Barra superior de panel de datos
            TopRecordingAppBar(
                isRunning = isRunning,
                enabled = hasPermission && mapReady,
                onPlayPauseClick = {
                    if (isRecording){
                        viewModel.toggleRunning()
                    }else{
                      viewModel.onStartRoute()
                    }

                })
            //panel de datos en tiempo real
            DataPanel(viewModel, routePoints)

            SaveRouteDialog(
                showDialog = showSaveDialog,
                onDismiss = { showSaveDialog = false },
                onConfirm = { routeName ->
                    viewModel.onFinishRoute(routeName)
                    showSaveDialog = false
                },
                onDiscard = {viewModel.onDiscardRoute()}
            )

        }
    }
}


/**
 * Metodo que crea la la vista de mapa embebida en la UI
 * @param ViewModel
 *
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

                    // Crea el engFFFFine de la UI y se lo pasa al ViewModel para suscribirse.
                    val engine = mapLibreMap.locationComponent.locationEngine
                        ?: org.maplibre.android.location.engine.LocationEngineDefault.getDefaultLocationEngine(ctx)

                    //notificamos al ViewModel que tiene engine disponible
                    viewModel.startLocationUpdates(engine)
                    viewModel.setMapReady() // marca map como listo

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
            // Dibuja polyline con los puntos acumulados
            val points = viewModel.routePoints.value.map {
                LatLng(it.latitude, it.longitude)
            }
            if (points.isNotEmpty()) {
                // elimina líneas anteriores si no quieres que se acumulen
                map?.clear()

                map?.addPolyline(
                    PolylineOptions()
                        .addAll(points)
                        .color(Color.Blue.hashCode()) // necesitas int (android.graphics.Color)
                        .width(5f)
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp)
    )
}


/**
 * Metodo que crea la barra superior del DataPanel en donde se situan el botón de play/pause y el de seleccionar deporte
 *
 * @param Boolean: routeStarted
 * @param Boolean: isRunning
 * @param Boolean: enable
 * @param Unit: onPlayPauseClick
 *
 */
@Composable
fun TopRecordingAppBar(
    isRunning: Boolean,
    enabled: Boolean = true,
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

            IconButton(onClick = onPlayPauseClick, enabled = enabled) {
                Icon(
                    painter = if (!isRunning) painterResource(R.drawable.play) else painterResource(R.drawable.pause),
                    contentDescription = "RightIcon",
                    tint = if (!isRunning) Color.Green else Color.Red,
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    }
}


/**
 * Panel de datos en tiempo real
 * @param ViewModel
 */
@Composable
fun DataPanel(
    viewModel: PlayViewModel,
    routePoints:List<RoutePoint>
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
        StatBlock(
            label = "Distancia",
            value = String.format("%.2f", calculateDistance(routePoints) / 1000),
            fontSize = 34.sp
        )
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
            StatBlock(label = "km/min", value = msToMinPerKm(calculateCurrentSpeed(routePoints)).toString())
        }

        // Elevación y ppm
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatBlock(label = "Desnivel +", value = calculateElevationGain(routePoints).toString())
            StatBlock(label = "Frecuencia", value = "---")
        }



    }
}


/**
 * Bloque individual que componen el DataPanel
 */
@Composable
fun StatBlock(label: String, value: String, fontSize: TextUnit = 24.sp) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, fontSize = 12.sp, color = Color.Gray)
        Text(text = value, fontSize = fontSize, fontWeight = FontWeight.Bold, color = Color.White)
    }
}


/**
 * Este bloque muestra un mensaje de confirmacion una vez que se le da al botón de guardar la ruta
 */
@Composable
fun SaveRouteDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
    onDiscard: () -> Unit
) {
    var routeName by remember { mutableStateOf("") }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(
                    text = stringResource(R.string.saveRoute),
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = stringResource(R.string.routeName),
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = routeName,
                        onValueChange = { routeName = it },
                        label = { Text(stringResource(R.string.routeNamePlaceholder)) },
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedLabelColor = Color.White,
                            unfocusedLabelColor = Color.White.copy(alpha = 0.6f),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = Color.White,
                            focusedIndicatorColor = Color.White,
                            unfocusedIndicatorColor = Color.White.copy(alpha = 0.6f)
                        )
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { if (routeName.isNotBlank()) onConfirm(routeName) },
                    enabled = routeName.isNotBlank()
                ) { Text(stringResource(R.string.save), color = Color.White) }
            },
            dismissButton = {
                Row {
                    TextButton(onClick = onDiscard) {
                        Text("Descartar", color = Color.Yellow)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(onClick = onDismiss) {
                        Text(stringResource(R.string.cancel), color = Color.Red)
                    }
                }
            },
            containerColor = Gray,
            shape = MaterialTheme.shapes.medium
        )
    }
}
