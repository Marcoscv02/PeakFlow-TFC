@file:OptIn(ExperimentalMaterial3Api::class)

package marcos.peakflow.ui.screens.home

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(

){
    Toolbar()
    Scaffold(
        content = {Content()}
    )
}

@Preview
@Composable
fun Toolbar(){
    TopAppBar(
        title = { Text(text = "Initial") }
    )
}

@Composable
fun Content(){

}