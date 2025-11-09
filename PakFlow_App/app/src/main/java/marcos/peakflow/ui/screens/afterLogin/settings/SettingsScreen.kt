package marcos.peakflow.ui.screens.afterLogin.settings


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import marcos.peakflow.R
import marcos.peakflow.ui.components.BottomNavBar
import marcos.peakflow.ui.components.StandardTopAppBar
import marcos.peakflow.ui.theme.RedPeakFlow
import marcos.peakflow.ui.theme.Theme
import marcos.peakflow.ui.theme.ThemeViewModel

@Composable
fun SettingsScreen(
    navigateToHome: () -> Unit,
    navigateToPlay: () -> Unit,
    navigateToRoute: () -> Unit,
    navigateToTraining: () -> Unit,
    navigateBack: () -> Unit,
    themeViewModel: ThemeViewModel
) {
    val context = LocalContext.current
        //Tema actual
    val currentTheme by themeViewModel.theme.collectAsState()


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
                Spacer(modifier = Modifier.height(16.dp))

                // 3. Añadimos el Switch para el modo oscuro
                ThemeSwitcherRow(
                    currentTheme = currentTheme,
                    onThemeChange = { newTheme ->
                        themeViewModel.changeTheme(newTheme)
                    }
                )

            }
        }
    }
}


@Composable
private fun ThemeSwitcherRow(
    currentTheme: Theme,
    onThemeChange: (Theme) -> Unit
) {
    // Lógica para determinar si el switch debe estar "encendido"
    // Estará encendido solo si el tema es explícitamente DARK.
    val isChecked = currentTheme == Theme.DARK

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.appearance),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Switch(
            checked = isChecked,
            onCheckedChange = { switchIsOn ->
                val newTheme = if (switchIsOn) Theme.DARK else Theme.LIGHT
                onThemeChange(newTheme)
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = RedPeakFlow,
                checkedTrackColor = RedPeakFlow.copy(alpha = 0.5f),
                uncheckedThumbColor = MaterialTheme.colorScheme.onSurface,
                uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        )
    }
}

