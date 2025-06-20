package marcos.peakflow.presentation.view.initial

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import marcos.peakflow.R
import marcos.peakflow.SupabaseClient
import marcos.peakflow.SupabaseClient.supabase
import marcos.peakflow.data.User
import marcos.peakflow.presentation.theme.BackgroundButton
import marcos.peakflow.presentation.theme.Black
import marcos.peakflow.presentation.theme.Gray
import marcos.peakflow.presentation.theme.RedPeakFlow
import marcos.peakflow.presentation.theme.ShapeButton

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
                .border(2.dp, color = ShapeButton, CircleShape),
            colors = ButtonDefaults.buttonColors(BackgroundButton)
        ) {
            Text(
                text = stringResource(R.string.InicioSesion),
                color = ShapeButton,
                fontSize = 15.sp
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = stringResource(R.string.copyRight),
            color = Color.White
        )
        Spacer(modifier = Modifier.height(20.dp))
        UserList()
    }
}


@Composable
fun UserList() {
    var users by remember { mutableStateOf(emptyList<User>()) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            users = SupabaseClient.supabase  // ✅ Accede a la instancia real
                .from("user")
                .select()
                .decodeList<User>()
        }
    }

    LazyColumn {
        items(
            items = users,
            key = { user -> user.id },
        ) { user ->
            Text(
                text = user.name,
                modifier = Modifier.padding(8.dp),
            )
        }
    }
}
