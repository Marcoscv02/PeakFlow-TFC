@file:Suppress("UNUSED_EXPRESSION")

package marcos.peakflow.ui.screens.afterLogin.play

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

    var isRunning by remember { mutableStateOf(false) }


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
                MapLibreLocationView()
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

            TopRecordingAppBar(isRunning, onPlayPauseClick = {isRunning = !isRunning})
            //panel de datos en tiempo real
            DataPanel()

        }
    }
}


/**
 * Metodo que crea la la vista de mapa embebida en la UI
 */
@SuppressLint("MissingPermission")
@Composable
fun MapLibreLocationView() {
    val context = LocalContext.current
    val mapTilerKey = "ayEMMbyitB7UOUpklBr6"
    val styleUrl = "https://api.maptiler.com/maps/hybrid/style.json?key=$mapTilerKey"

    AndroidView(
        factory = { ctx -> //ctx es el más preciso dentro del AndroidView porque garantiza que es el contexto correcto para esa vista.
            // Inicializa MapLibre solo si no se hizo antes
            MapLibre.getInstance(
                ctx,
                mapTilerKey,
                WellKnownTileServer.MapTiler
            )

            //Crea la vista del mapView usando en contexto de android
            val mapView = MapView(ctx)
            //Callback que se llama cuando el mapa está libre para ser usado
            mapView.getMapAsync { mapLibreMap ->

                //Establece el estilo visual del mapa usando un archivo .json remoto
                mapLibreMap.setStyle(
                    Style.Builder().fromUri(styleUrl)
                ) { style ->

                    //Obtiene el componente de ubicación, que es responsable de mostrar y actualizar la posición del usuario.
                    val locationComponent = mapLibreMap.locationComponent

                    //Esto define cómo se verá el indicador de ubicación
                    val options = LocationComponentOptions.builder(ctx)
                        .pulseEnabled(true)
                        .pulseColor(android.graphics.Color.CYAN)
                        .foregroundTintColor(android.graphics.Color.BLUE)
                        .build()

                    //Aquí se define cómo y cuándo se activa la ubicación:

                    val activationOptions = LocationComponentActivationOptions.builder(ctx, style)
                        .locationComponentOptions(options)
                        .useDefaultLocationEngine(true) //usa el motor de ubicación por defecto.
                        .locationEngineRequest( //frecuencia y precisión de actualizaciones:
                            LocationEngineRequest.Builder(750L)
                                .setFastestInterval(750L)
                                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                                .build()
                        )

                        .build()

                    locationComponent.activateLocationComponent(activationOptions) //Activa el componente de ubicación con las opciones anteriores.
                    locationComponent.isLocationComponentEnabled = true //Lo habilita visualmente.
                    locationComponent.cameraMode = CameraMode.TRACKING //Configura el modo de cámara para seguir al usuario mientras se mueve.


                    //Obtiene la ultima localizacion conocida del usuario y mueve la camara ahí para que esta apunte al usuario
                    val lastLocation = locationComponent.lastKnownLocation
                    if (lastLocation != null) {
                        val userLatLng = LatLng(lastLocation.latitude, lastLocation.longitude)
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
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp)
    )
}

@Composable
fun TopRecordingAppBar(
    isRunning:Boolean,
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
fun DataPanel() {

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
            StatBlock(label = "Tiempo", value = "00:38:50")
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

