package marcos.peakflow.presentation.view.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import marcos.peakflow.R
import marcos.peakflow.ui.theme.BackgroundButton
import marcos.peakflow.ui.theme.Black
import marcos.peakflow.ui.theme.RedPeakFlow
import marcos.peakflow.ui.theme.ShapeButton


@Composable
fun LoginScreen (
    navigateToSignup :() -> Unit,
    navigateToLoginWithEmail :() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
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
            text = stringResource(id = R.string.textoInicioSesion),
            modifier = Modifier.padding(30.dp),
            color = Color.White,
            fontSize = 35.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            lineHeight = 50.sp
        )

        Spacer(modifier = Modifier.weight(1f))

        //Boton de INICIO CON EMAIL
        Button(
            onClick = {navigateToLoginWithEmail ()},
            modifier = Modifier
                .fillMaxWidth()
                .height(45.dp)
                .padding(horizontal = 32.dp),
            colors = ButtonDefaults.buttonColors(RedPeakFlow),

        ) {
            Text(
                text = stringResource(R.string.InicioSesionEmail),
                color = Black,
                fontSize = 15.sp
            )
        }
        Spacer(modifier = Modifier.height(12.dp))

        //boton de INICIO CON GOOGLE
        CustomButton(painterResource(R.drawable.google), stringResource(R.string.InicioSesionGoogle))
        Spacer(modifier = Modifier.height(12.dp))

        //boton de INICIO CON FACEBOOK
        CustomButton(painterResource(R.drawable.facebook), stringResource(R.string.InicioSesionFacebook))
        Spacer(modifier = Modifier.weight(1f))

        //Texto que lleva al registro
        Text(
            text = stringResource(id = R.string.noTienesCuenta),
            color = Color.White,
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(id = R.string.registrate),
            color = Color.White,
            modifier = Modifier.clickable { navigateToSignup() },
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.weight(1f))

    }

}

/**
 * Funcion encargada de crear los botones customizados de iniciar sesion con Google y con Facebook
 * @param Painter
 * @param String
 */
@Composable
private fun CustomButton(painter: Painter, text: String){
    Box (
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 32.dp)
            .background(BackgroundButton)
            .border(2.dp, ShapeButton, CircleShape)
            .clickable { },
        contentAlignment = Alignment.CenterStart
    ){
        Image(
            painter = painter,
            contentDescription = "",
            modifier = Modifier
                .padding(start = 12.dp)
                .size(25.dp)
        )
        Text(
            text = text,
            color = Color.White,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}