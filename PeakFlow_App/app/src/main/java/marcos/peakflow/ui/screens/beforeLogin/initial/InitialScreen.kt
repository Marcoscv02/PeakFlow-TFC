package marcos.peakflow.ui.screens.beforeLogin.initial

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import marcos.peakflow.R
import marcos.peakflow.ui.theme.BackgroundButton
import marcos.peakflow.ui.theme.Black
import marcos.peakflow.ui.theme.Gray
import marcos.peakflow.ui.theme.RedPeakFlow
import marcos.peakflow.ui.theme.backgroudNavSelectedItem

@Composable
fun InitialScreen(
    navigateToLogin: () -> Unit,
    navigateToSignup: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Gray, Black), startY = 0f, endY = 600f)),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(modifier = Modifier.weight(1f))

        //Logo de la aplicación
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo Peakflow",
            modifier = Modifier
                .clip(CircleShape)
                .size(200.dp),
            )
        Spacer(modifier = Modifier.weight(1f))

        //Slogan
        Text(
            text = stringResource(id = R.string.slogan),
            color = Color.White,
            fontSize = 35.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            lineHeight = 50.sp,
            modifier = Modifier.padding(25.dp)
        )

        Spacer(modifier = Modifier.weight(1f))

        //Boton de REGISTRO
        Button(
            onClick = {navigateToSignup()},
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            colors = ButtonDefaults.buttonColors(RedPeakFlow)
        ) {
            Text(
                text = stringResource(R.string.Registro),
                color = Black,
                fontSize = 15.sp
            )
        }
        Spacer(modifier = Modifier.height(12.dp))

        //boton de INICIO DE SESION
        Button(
            onClick = {navigateToLogin ()},
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .border(2.dp, color = backgroudNavSelectedItem, CircleShape),
            colors = ButtonDefaults.buttonColors(BackgroundButton)
        ) {
            Text(
                text = stringResource(R.string.InicioSesion),
                color = backgroudNavSelectedItem,
                fontSize = 15.sp
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = stringResource(R.string.copyRight),
            color = Color.White
        )
        Spacer(modifier = Modifier.height(20.dp))

    }
}
