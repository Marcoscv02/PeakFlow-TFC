@file:Suppress("UNUSED_EXPRESSION")

package marcos.peakflow.ui.screens.afterLogin.play

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
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
    val viewModel : PlayViewModel = viewModel(factory = PeakFlowViewModelFactory())//Falta agregar en viewModelFactory

    val context = LocalContext.current
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
        // Aquí va el contenido principal
        Column (
            modifier = Modifier
                .fillMaxSize()
                .background(Black)
        ){
            MapLibreLocationView()

        }
    }
}


@SuppressLint("MissingPermission")
@Composable
fun MapLibreLocationView() {
    val context = LocalContext.current

    AndroidView(
        factory = { ctx ->
            // Inicializa MapLibre solo si no se hizo antes
            MapLibre.getInstance(
                ctx,
                "", // Sin API key
                WellKnownTileServer.MapLibre
            )

            val mapView = MapView(ctx)
            mapView.getMapAsync { mapLibreMap ->

                mapLibreMap.setStyle(
                    Style.Builder().fromUri("https://demotiles.maplibre.org/style.json")
                ) { style ->

                    val locationComponent = mapLibreMap.locationComponent

                    val options = LocationComponentOptions.builder(ctx)
                        .pulseEnabled(true)
                        .pulseColor(Color.CYAN)
                        .foregroundTintColor(Color.BLUE)
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

                    locationComponent.activateLocationComponent(activationOptions)
                    locationComponent.isLocationComponentEnabled = true
                    locationComponent.cameraMode = CameraMode.TRACKING


                    val lastLocation = locationComponent.lastKnownLocation
                    if (lastLocation != null) {
                        val userLatLng = LatLng(lastLocation.latitude, lastLocation.longitude)
                        val cameraPosition = CameraPosition.Builder()
                            .target(userLatLng)
                            .zoom(10.0)
                            .build()

                        mapLibreMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                    }
                }
            }
            mapView
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    )
}
