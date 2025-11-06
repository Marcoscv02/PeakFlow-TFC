package marcos.peakflow.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import marcos.peakflow.ui.navigation.NavigationWrapper
import marcos.peakflow.ui.screens.afterLogin.play.PlayViewModel
import marcos.peakflow.ui.theme.Theme
import marcos.peakflow.ui.theme.ThemeChangerTheme
import marcos.peakflow.ui.theme.ThemeViewModel

class MainActivity : ComponentActivity() {

    private lateinit var navHostController: NavHostController
    private val playViewModel: PlayViewModel by viewModels()
    private val themeViewModel: ThemeViewModel by viewModels()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val application = application as PeakFlowApp
            navHostController = rememberNavController()

            //Recolectar el tema actual del viewModel
            val currentTheme = themeViewModel.theme.collectAsState()

            //Logica para decidir si el tema oscuro está activado
            val isDarkTheme = when (currentTheme.value){
                Theme.LIGHT -> false
                Theme.DARK -> true
                Theme.SYSTEM -> isSystemInDarkTheme()
            }

            ThemeChangerTheme(darkTheme = isDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavigationWrapper()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        playViewModel.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}



