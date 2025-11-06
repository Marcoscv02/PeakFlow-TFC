package marcos.peakflow.ui.screens.afterLogin.trainingDetail

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import marcos.peakflow.R
import marcos.peakflow.domain.model.route.Route
import marcos.peakflow.domain.util.formatDistance
import marcos.peakflow.domain.util.formatInstant
import marcos.peakflow.domain.util.formatSecondsToTime
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
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.geometry.LatLngBounds
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.Style

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TrainingDetailScreen(
    navigateToHome: () -> Unit,
    navigateToPlay: () -> Unit,
    navigateToRoute: () -> Unit,
    navigateToTraining: () -> Unit,
    navigateBack: () -> Unit,
    routeId: String
) {
    val viewModel: TrainingDetailViewModel = viewModel(factory = PeakFlowViewModelFactory())
    // Recolectamos el estado (puede ser Loading, Error o Success)
    val uiState: TrainingDetailUiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope ()

    var showEditDialog by remember {mutableStateOf(false)}

    LaunchedEffect(routeId) {
        viewModel.loadRouteDetails(routeId)
    }

    Scaffold(
        topBar = {
            StandardTopAppBar(
                title = stringResource(R.string.trainingDetailTitleScreen),
                onLeftClick = navigateBack,
                leftIcon = R.drawable.back,
                onRightClick = { showEditDialog = true },
                rightIcon = R.drawable.edit
            )
        },

        bottomBar = {
            BottomNavBar(
                currentScreen = Screen.Training.route,
                navigateToHome = navigateToHome,
                navigateToPlay = navigateToPlay,
                navigateToRoute = navigateToRoute,
                navigateToTraining = { navigateToTraining }
            )
        }
    ) { paddingValues ->

        // Almacenamos el estado actual en una variable local para usarlo en el when
        val currentState = uiState
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Black)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                when (currentState) {
                    is TrainingDetailUiState.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(50.dp)
                        )
                    }

                    is TrainingDetailUiState.Error -> {
                        Text(
                            text = "Error: ${currentState.message}",
                            color = Color.Red,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    is TrainingDetailUiState.Success -> {
                        // En el caso de éxito, el mapa recibe la ruta que NO ES NULA.
                        StaticRouteMap(route = currentState.route)
                    }
                }
            }

            // Mostramos los detalles solo en caso de éxito
            if (currentState is TrainingDetailUiState.Success) {
                val context = LocalContext.current
                val editRouteSuccess = stringResource(R.string.editRouteSuccess)
                val editRouteFailure = stringResource(R.string.editRouteFailure)

                TrainingDetailsContent(route = currentState.route)
                EditRouteDialog(
                    showDialog = showEditDialog,
                    onDiscard = { showEditDialog=false },
                    onConfirm = {newName->
                        scope.launch {

                            var success = viewModel.editRoute(route = currentState.route, name = newName)
                            showEditDialog=false

                            val message = if (success) {
                                editRouteSuccess
                            } else {
                                editRouteFailure
                            }
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun TrainingDetailsContent(route: Route) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)) {
        // Título del entrenamiento: Si es nulo, muestra "Entrenamiento"
        Text(
            text = route.name ?: stringResource(R.string.trainingName),
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )

        // Fecha del entrenamiento
        Text(
            text = formatInstant(route.startTime),
            color = Color.Gray,
            fontSize = 16.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        MetricRow(
            // Distancia
            leftMetric = MetricItem(
                title = stringResource(R.string.distance),
                value = formatDistance(route.distance),
                color = Color(0xFF4FC3F7) // Azul claro
            ),
            // Tiempo en movimiento
            rightMetric = MetricItem(
                title = stringResource(R.string.movTime),
                value = formatSecondsToTime(route.movingSec),
                color = Color(0xFF81C784) // Verde claro
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        MetricRow(
            // Velocidad media
            leftMetric = MetricItem(
                title = stringResource(R.string.avgSpeed),
                // Formateamos para que muestre " min/km"
                value = "${msToMinPerKm(route.avgSpeed)} min/km",
                color = Color(0xFFFFB74D) // Naranja
            ),
            // Tiempo total del entrenamiento
            rightMetric = MetricItem(
                title = stringResource(R.string.totalTime),
                // ¡CORRECCIÓN CLAVE! Usamos durationSec que ya tienes.
                value = formatSecondsToTime(route.durationSec),
                color = Color(0xFF9575CD) // Púrpura claro
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        MetricRow(
            // Desnivel positivo
            leftMetric = MetricItem(
                title = stringResource(R.string.desPos),
                // ¡CORRECCIÓN CLAVE! Manejamos el valor nulo y añadimos las unidades "m".
                value = route.elevationGain?.let { "%.0fm".format(it) } ?: "---",
                color = Color(0xFF4DB6AC) // Verde azulado
            ),
            // Ritmo cardiaco medio
            rightMetric = MetricItem(
                title = stringResource(R.string.heartRate),
                // ¡CORRECCIÓN CLAVE! Manejamos el valor nulo.
                value = route.avgHeartRate?.let { "%.0f".format(it) } ?: "---",
                color = Color(0xFFF06292) // Rosa
            )
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}


@Composable
fun MetricRow(leftMetric: MetricItem, rightMetric: MetricItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        MetricCard(metric = leftMetric, modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.width(16.dp))
        MetricCard(metric = rightMetric, modifier = Modifier.weight(1f))
    }
}

@Composable
fun MetricCard(metric: MetricItem, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(
                color = Color(0xFF1E1E1E),
                shape = MaterialTheme.shapes.medium
            )
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = metric.title,
            color = Color.Gray,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = metric.value,
            color = metric.color,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}


/**
 * Mapa estático con la ruta
 * @param Route
 */
@Composable
fun StaticRouteMap(route: Route?) {
    val context = LocalContext.current
    // Clave dummy, ya que tu ViewModel de PlayScreen la gestiona. Aquí podrías obtenerla de otro sitio si fuera necesario.
    val mapTilerKey = "ayEMMbyitB7UOUpklBr6"
    val styleUrl = "https://api.maptiler.com/maps/hybrid/style.json?key=$mapTilerKey"

    // Solo muestra el mapa si hay puntos en la ruta
    if (route?.points.isNullOrEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
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
            .height(300.dp)
    )
}


data class MetricItem(
    val title: String,
    val value: String,
    val color: Color
)

/**
 * Este bloque muestra un panel que permite modificar el nombre de la ruta
 * @param Boolean: showDialog
 * @param Unit: onDismiss
 * @param Unit: onConfirm
 * @param Unit: onDiscard
 */
@Composable
fun EditRouteDialog(
    showDialog: Boolean,
    onDiscard: () -> Unit,
    onConfirm: (String) -> Unit,

) {
    var routeName by remember { mutableStateOf("") }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDiscard,
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
                        text = stringResource(R.string.editRoute),
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = routeName,
                        onValueChange = { routeName = it },
                        label = { Text(stringResource(R.string.editRoutePlaceHolder)) },
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
            containerColor = Gray,
            shape = MaterialTheme.shapes.medium
        )
    }
}
