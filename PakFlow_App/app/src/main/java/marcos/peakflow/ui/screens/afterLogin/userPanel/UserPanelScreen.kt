package marcos.peakflow.ui.screens.afterLogin.userPanel

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import marcos.peakflow.R
import marcos.peakflow.ui.components.BottomNavBar
import marcos.peakflow.ui.components.TripleTopAppBar
import marcos.peakflow.ui.screens.PeakFlowViewModelFactory
import marcos.peakflow.ui.theme.Black
import marcos.peakflow.ui.theme.RedPeakFlow

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun UserPanelScreen(
    navigateToHome: () -> Unit,
    navigateToPlay: () -> Unit,
    navigateToRoute: () -> Unit,
    navigateToTraining: () -> Unit,
    navigateBack: () -> Unit,
    navigateToInitial: () -> Unit
) {
    val viewModel : UserPanelViewModel = viewModel(factory = PeakFlowViewModelFactory())
    val userName by viewModel.userName.collectAsState()


    Scaffold(
        topBar = {
            TripleTopAppBar(
                title = userName,
                onLeftClick = navigateBack,
                leftIcon = R.drawable.back,
                onMidleClick = {},
                middleIcon = R.drawable.search,
                onRightClick = {},
                rightIcon = R.drawable.settings
            )
        },
        bottomBar = {
            BottomNavBar(
                currentScreen = null,
                navigateToHome = navigateToHome,
                navigateToPlay = navigateToPlay,
                navigateToRoute = navigateToRoute,
                navigateToTraining = navigateToTraining
            )
        }
    ) { innerPadding ->  // Usa el padding del Scaffold
        Column(
            modifier = Modifier
                .fillMaxSize()  // Añade esto para ocupar todo el espacio
                .padding(innerPadding)  // Respeta el padding de las barras
                .background(Black)
        ) {
            // Botón de CERRAR SESIÓN (corregido)
            Button(
                onClick = { viewModel.closeSession(navigateToInitial) },
                modifier = Modifier
                    .fillMaxWidth()  // Ocupa todo el ancho
                    .padding(horizontal = 32.dp, vertical = 16.dp),  // Padding adecuado
                colors = ButtonDefaults.buttonColors(RedPeakFlow)
            ) {
                Text(
                    text = stringResource(R.string.closeSession),
                    color = Color.White,  // Cambiado a blanco para mejor contraste
                    fontSize = 16.sp  // Tamaño ligeramente mayor
                )
            }
        }
    }
}