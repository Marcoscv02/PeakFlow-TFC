package marcos.peakflow.ui.screens.afterLogin.settings


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import marcos.peakflow.R
import marcos.peakflow.ui.components.BottomNavBar
import marcos.peakflow.ui.components.StandardTopAppBar

@Composable
fun SettingsScreen(
    navigateToHome: () -> Unit,
    navigateToPlay: () -> Unit,
    navigateToRoute: () -> Unit,
    navigateToTraining: () -> Unit,
    navigateBack: () -> Unit,

) {
//    val context = LocalContext.current
//    // Obtén a instancia do ViewModel
//    val viewModel: SettingsViewModel = viewModel( )


    Scaffold(

        topBar = {
            StandardTopAppBar(
                title = stringResource(R.string.settingsTittle),
                onLeftClick = navigateBack,
                leftIcon = R.drawable.back,
                onRightClick = {},
                rightIcon = R.drawable.search
            )
        } ,

        bottomBar = {
            BottomNavBar(
                currentScreen = null,
                navigateToHome = navigateToHome,
                navigateToPlay = navigateToPlay,
                navigateToRoute = navigateToRoute,
                navigateToTraining = navigateToTraining
            )
        }



    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.appearance),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground // Usa a cor do tema!
                )

            }
        }
    }
}

