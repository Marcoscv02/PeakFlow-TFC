package marcos.peakflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import marcos.peakflow.core.navigation.NavigationWrapper
import marcos.peakflow.ui.theme.PeakFlowTheme

class MainActivity : ComponentActivity() {

    private lateinit var navHostController: NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            navHostController = rememberNavController()

            PeakFlowTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavigationWrapper()
                }
            }
        }
    }

    private fun getClient(){
        val  Client = createSupabaseClient(
            supabaseUrl = "https://zyjdewgpqigihayqjstw.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Inp5amRld2dwcWlnaWhheXFqc3R3Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDUyMjYwMDYsImV4cCI6MjA2MDgwMjAwNn0.1zpBC3NKuzwrSrtq04hYrc964mu_FsADRKUxIplcT8w"
        ){
            install(Postgrest)
        }
    }
}
