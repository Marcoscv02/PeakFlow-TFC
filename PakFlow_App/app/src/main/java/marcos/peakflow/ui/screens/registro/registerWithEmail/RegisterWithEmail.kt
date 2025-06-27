package marcos.peakflow.ui.screens.registro.registerWithEmail

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentDataType.Companion.Date
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.datetime.*
import marcos.peakflow.R
import marcos.peakflow.ui.screens.PeakFlowViewModelFactory
import marcos.peakflow.ui.theme.*
import marcos.peakflow.ui.theme.Gray


@Composable
fun RegisterWithEmailScreen(
    navigateToSignup: () -> Unit,
    navigateToHome: () -> Unit = {}
){
    val context = LocalContext.current
    val viewModel: SignUpWithEmailViewModel = viewModel(factory = PeakFlowViewModelFactory())
    val userState by viewModel.userState.collectAsState()

    // Observar cambios en el estado para navegación y mensajes
    LaunchedEffect(userState) {
        userState!!.errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }

        if (userState!!.isRegistered) {
            navigateToHome()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // BOTÓN DE RETROCEDER
        IconButton(
            onClick = navigateToSignup,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.back),
                colorFilter = ColorFilter.tint(Color.White),
                contentDescription = "retroceder",
                modifier = Modifier.size(20.dp),
            )
        }

        // Campos de registro
        Column(
            modifier = Modifier
                .padding(horizontal = 40.dp)
                .fillMaxWidth()
        ) {
            // CAMPO DE USERNAME
            MakeText(R.string.userNameRegisterValue)
            CustomTextField(
                value = userState!!.username,
                onValueChange = { viewModel.updateFields(userName = it) },
                placeholder = stringResource(R.string.placeholderRegisterScreen),
                keyboardType = KeyboardType.Text
            )
            Spacer(modifier = Modifier.height(40.dp))

            // CAMPO DE CORREO
            MakeText(R.string.emailRegisterValue)
            CustomTextField(
                value = userState!!.email,
                onValueChange = { viewModel.updateFields(email = it) },
                placeholder = stringResource(R.string.placeholderRegisterScreen),
                keyboardType = KeyboardType.Email
            )
            Spacer(modifier = Modifier.height(40.dp))

            // CAMPO DE CONTRASEÑA
            MakeText(R.string.passwordRegisterValue)
            PasswordTextField(
                value = userState!!.password1,
                onValueChange = { viewModel.updateFields(password1 = it) },
                placeholder = stringResource(R.string.placeholderRegisterScreen),
            )
            Spacer(modifier = Modifier.height(40.dp))

            // CAMPO DE REPETIR CONTRASEÑA
            MakeText(R.string.passwordRegisterValue2)
            PasswordTextField(
                value = userState!!.password2,
                onValueChange = { viewModel.updateFields(password2 = it) },
                placeholder = stringResource(R.string.placeholderRegisterScreen),
            )
            Spacer(modifier = Modifier.height(40.dp))

            // CAMPO DE FECHA DE NACIMIENTO
            MakeText(R.string.birthdayRegisterValue2)
            DatePickerDocked(
                selectedDate = userState!!.birthdate,
                onDateSelected = { viewModel.updateFields(birthDate = it) }
            )
            Spacer(modifier = Modifier.height(40.dp))

            // CAMPO SEXO
            MakeText(R.string.sexoRegisterValue)
            GenderDropdownSelector(
                selectedOption = userState!!.gender,
                onOptionSelected = { viewModel.updateFields(gender = it) },
            )
            Spacer(modifier = Modifier.height(40.dp))

            // Botón de registro
            LoginButton(
                enabled = userState!!.isRegisterEnabled && !userState!!.isLoading,
                isLoading = userState!!.isLoading,
                onRegisterSelected = { viewModel.registerUser() }
            )
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}



/**
 * Este metodo generará todos los títulos de todos los campos de texto con el mismo formato
 * @param Int text
 */
@Composable
private fun MakeText(text: Int, modifier: Modifier = Modifier) {
    Text(
        text = stringResource(text),
        modifier = modifier,
        color = Color.White,
        fontSize = 20.sp,
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
private fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp),
        placeholder = {
            Text(placeholder, color = ShapeButton)
        },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Gray,
            focusedContainerColor = ShapeButton
        ),
        shape = RoundedCornerShape(8.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
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
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp),
        placeholder = {
            Text(placeholder, color = ShapeButton)
        },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Gray,
            focusedContainerColor = ShapeButton
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = if (passwordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        trailingIcon = {
            IconButton(
                onClick = { passwordVisible = !passwordVisible }
            ) {
                Icon(
                    painter = painterResource(
                        id = if (passwordVisible) R.drawable.visibility_on
                        else R.drawable.visibility_of
                    ),
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
 * Metodo para seleccionar la fecha de nacimiento del usuario, se guardará en un dato de tipo Date
 * @param Date: selected Date
 * @param Unit: onDateSelected
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDocked(
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate?) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate
            ?.atStartOfDayIn(TimeZone.currentSystemDefault())
            ?.toEpochMilliseconds()
    )

    val selectedDateText = selectedDate?.let {
        "${it.dayOfMonth.toString().padStart(2, '0')}/" +
                "${it.monthNumber.toString().padStart(2, '0')}/${it.year}"
    } ?: ""

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp),
    ) {
        TextField(
            value = selectedDateText,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDatePicker = true }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select date",
                        tint = Color.White
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Gray,
                focusedContainerColor = ShapeButton
            )
        )

        if (showDatePicker) {
            Popup(
                onDismissRequest = { showDatePicker = false },
                alignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .shadow(elevation = 4.dp)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp)
                ) {
                    DatePicker(
                        state = datePickerState,
                        showModeToggle = false
                    )

                    Button(
                        onClick = {
                            val newDate = datePickerState.selectedDateMillis?.let {
                                Instant.fromEpochMilliseconds(it)
                                    .toLocalDateTime(TimeZone.currentSystemDefault())
                                    .date
                            }
                            onDateSelected(newDate)
                            showDatePicker = false
                        },
                        modifier = Modifier.align(Alignment.BottomEnd)
                    ) {
                        Text("OK")
                    }
                }
            }
        }
    }
}


/**
 * Metodo para definir el genero de usuario en un menú desplegable con 3 opciones
 * @param String: selectedOption
 * @param Unit: onOptionSelected
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenderDropdownSelector(
    selectedOption: String?,
    onOptionSelected: (String) -> Unit
) {
    val genderOptions = listOf(
        "not_specified" to R.string.gender_notSpecified,
        "male" to R.string.gender_male,
        "female" to R.string.gender_female
    )

    var expanded by remember { mutableStateOf(false) }

    val displayText = selectedOption?.let { selectedValue ->
        genderOptions.find { it.first == selectedValue }?.let {
            stringResource(id = it.second)
        }
    } ?: ""

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth()
    ) {
        TextField(
            readOnly = true,
            value = displayText,
            onValueChange = {},
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            placeholder = { Text(text = stringResource(R.string.select_gender), color = ShapeButton) },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Gray,
                focusedContainerColor = ShapeButton
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            genderOptions.forEach { (value, resId) ->
                DropdownMenuItem(
                    text = { Text(stringResource(resId)) },
                    onClick = {
                        onOptionSelected(value)
                        expanded = false
                    }
                )
            }
        }
    }
}



/**
 * Botón de registro
 * @param Boolean: registerEnable- Indica si se cumplen todas las condiciones para poder logearse
 * @param Unit: onRegisterselected: Envia una funcion Lambda con lo que tiene que hacer el boton al ser clicado
 */
@Composable
fun LoginButton(
    enabled: Boolean,
    isLoading: Boolean,
    onRegisterSelected: () -> Unit
) {
    Button(
        onClick = onRegisterSelected,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = RedPeakFlow),
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier.size(24.dp)
            )
        } else {
            Text(
                text = stringResource(R.string.IniciaSesion),
                color = Black,
                fontSize = 15.sp
            )
        }
    }
}