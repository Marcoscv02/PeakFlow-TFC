package marcos.peakflow.ui.screens.beforeLogin.logueo.loginWithEmail

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import marcos.peakflow.R
import marcos.peakflow.ui.screens.PeakFlowViewModelFactory
import marcos.peakflow.ui.theme.Black
import marcos.peakflow.ui.theme.Gray
import marcos.peakflow.ui.theme.RedPeakFlow
import marcos.peakflow.ui.theme.backgroudNavSelectedItem

@Composable
fun LoginWithEmailScreen(

    navigateToLogin: () -> Unit,
    navigateToHome: () -> Unit
){
    val viewModel: LoginWithEmailViewModel = viewModel(factory = PeakFlowViewModelFactory())
    val state by viewModel.userState.collectAsState()

    var loginSuccess by remember { mutableStateOf(false) }
    val context = LocalContext.current


    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally,

    ) {

        //BOTÓN DE RETROCEDER
        IconButton(
            onClick = { navigateToLogin() },
            modifier = Modifier
                .align(Alignment.Start)
                .padding(16.dp)
        ) {
            //Logo de la aplicación
            Image(
                painter = painterResource(id = R.drawable.back),
                colorFilter = ColorFilter.tint(Color.White),
                contentDescription = "retroceder",
                modifier = Modifier
                    .size(20.dp),
            )
        }
        Spacer(modifier = Modifier.height(80.dp))

        //CAMPO DE EMAIL
        MakeText(R.string.userNameOrEmail, modifier = Modifier.align(Alignment.Start))
        CustomTextField(
            value = state!!.email,
            onValueChange = { viewModel.updateFields(email = it)},
            placeholder = stringResource(R.string.placeholderRegisterScreen),
            keyboardType = KeyboardType.Text
        )
        Spacer(modifier = Modifier.height(40.dp))


        //CAMPO DE CONTRASEÑA
        MakeText(
            R.string.passwordRegisterValue,
            modifier = Modifier.align(Alignment.Start)
        )
        PasswordTextField(
            value = state!!.password,
            onValueChange = { viewModel.updateFields(password = it) },
            placeholder = stringResource(R.string.placeholderRegisterScreen),
        )
        Text(
            text = stringResource(R.string.forgetPassword),
            modifier = Modifier
                .clickable { viewModel.resetPassword() }
                .padding(horizontal = 40.dp)
                .align(Alignment.End),
            color = Color.White,
            fontSize = 13.sp,
            lineHeight = 50.sp,
        )

        Spacer(modifier = Modifier.weight(1f))


        //Boton de REGISTRO
        LoginButton(true) {
            viewModel.loginUser { success ->
                loginSuccess = success
                if (success) {
                    navigateToHome() // o usa navController.navigate("home")
                } else {
                    Toast.makeText(context, "Error al iniciar sesión", Toast.LENGTH_SHORT).show()
                }
            }
        }
        Spacer(modifier = Modifier.height(40.dp))


    }
}


/**
 * Este método generará todos los títulos de todos los campos de texto con el mismo formato
 * @param Int text
 */
@Composable
private fun MakeText (
    text: Int,
    modifier: Modifier
){
    Text(
        text = stringResource(text),
        modifier = modifier.padding(horizontal = 40.dp),
        color = Color.White,
        fontSize = 35.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 50.sp,
    )
}


/**
 * Metodo encargado de crear todos los texfields (con el mismo formato) que recopilarán los datos para la creación de un nuevo usuario
 * @param String: value
 * @param String: onValueChange
 * @param String: Placeholder
 * @param KeyBoardType: keyboardType
 */
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    // Colores para el fondo
    val unfocusedColor = Color.LightGray.copy(alpha = 0.8f)
    val focusedColor = Color.LightGray.copy(alpha = 0.6f)
    val backgroundColor = if (isFocused) focusedColor else unfocusedColor

    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .background(backgroundColor, RoundedCornerShape(8.dp))
            .height(55.dp)
            .height(55.dp)
            .size(300.dp),
        placeholder = {
            Text(placeholder, color = backgroudNavSelectedItem)
        },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Gray,
            focusedContainerColor = backgroudNavSelectedItem
        ),
        shape = RoundedCornerShape(8.dp),
        )
}


/**
 * @param String: value
 * @param Unit: onValueChange
 * @param Modifier:
 */
@Composable
fun PasswordTextField(
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }


    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .height(55.dp)
            .size(300.dp),
        placeholder = {
            Text(placeholder, color = backgroudNavSelectedItem)
        },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Gray,
            focusedContainerColor = backgroudNavSelectedItem
        ),
        interactionSource = interactionSource,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = if (passwordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        trailingIcon = {
            val imageRes = if (passwordVisible) {
                R.drawable.visibility_on
            } else {
                R.drawable.visibility_of // Nombre corregido (típicamente 'visibility_off')
            }

            IconButton(
                onClick = { passwordVisible = !passwordVisible }
            ) {
                Icon(
                    painter = painterResource(id = imageRes),
                    contentDescription = if (passwordVisible) {
                        stringResource(R.string.hide_password)
                    } else {
                        stringResource(R.string.show_password)
                    },
                    tint = Color.White,
                    modifier = Modifier.size(25.dp)
                )
            }
        },
    )
}



/**
 * Botón de Inicio de sesion
 * @param Boolean: loginEnable- Indica si se cumplen todas las condiciones para poder logearse
 * @param Unit: onRegisterselected: Envia una función Lambda con lo que tiene que hacer el botón al ser clicado
 */
@Composable
fun LoginButton(loginEnable: Boolean, OnLoginSelected: () -> Unit) {
    //Boton de REGISTRO
    Button(
        onClick = {OnLoginSelected ()},
        modifier = Modifier
            .fillMaxWidth()
            .padding(50.dp),
        colors = ButtonDefaults.buttonColors(RedPeakFlow),
        enabled = loginEnable
    ) {
        Text(
            text = stringResource(R.string.IniciaSesion),
            color = Black,
            fontSize = 15.sp
        )
    }
}

