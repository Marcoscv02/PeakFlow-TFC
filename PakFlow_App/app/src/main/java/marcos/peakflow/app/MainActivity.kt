package marcos.peakflow.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import marcos.peakflow.ui.navigation.NavigationWrapper
import marcos.peakflow.ui.screens.afterLogin.play.PlayViewModel
import marcos.peakflow.ui.theme.ThemeChangerTheme

class MainActivity : ComponentActivity() {

    private lateinit var navHostController: NavHostController
    private val playViewModel: PlayViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val application = application as PeakFlowApp
            navHostController = rememberNavController()

            ThemeChangerTheme(darkTheme = true) { 
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



