package marcos.peakflow.presentation.view.signup

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentDataType.Companion.Date
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.*
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.lifecycle.ViewModel
import marcos.peakflow.R
import marcos.peakflow.presentation.viewModel.signup.SignUpWithEmailViewModel
import marcos.peakflow.ui.theme.*
import java.sql.Date


@Composable
fun RegisterWithEmailScreen(
    viewModel: SignUpWithEmailViewModel,
    navigateToSignup: () -> Unit
){

    val userName = remember { mutableStateOf("") }
    val email : String by viewModel.email.observeAsState(initial = "")
    val password = remember { mutableStateOf("") }
    val password2 = remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf<Date?>(null) }
    var selectedGender by remember { mutableStateOf<String?>(null) }




    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        //BOTÓN DE RETROCEDER
        IconButton(
            onClick = {navigateToSignup ()},
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

        //CAMPO DE USERNAME
        MakeText(R.string.userNameRegisterValue, modifier = Modifier.align(Alignment.Start))
        CustomTextField(
            value = userName.value,
            onValueChange = {userName.value = it},
            placeholder = stringResource(R.string.placeholderRegisterScreen),
            keyboardType = KeyboardType.Text
        )
        Spacer(modifier = Modifier.height(40.dp))

        //CAMPO DE CORREO
        MakeText(R.string.emailRegisterValue, modifier = Modifier.align(Alignment.Start))
        CustomTextField(
            value = email,
            onValueChange = {email},
            placeholder = stringResource(R.string.placeholderRegisterScreen),
            keyboardType = KeyboardType.Email
        )
        Spacer(modifier = Modifier.height(40.dp))

        //CAMPO DE CONTRASEÑA
        MakeText(R.string.passwordRegisterValue, modifier = Modifier.align(Alignment.Start))
        PasswordTextField(
            value = password.value,
            onValueChange = {password.value = it},
            placeholder = stringResource(R.string.placeholderRegisterScreen),
        )
        Spacer(modifier = Modifier.height(40.dp))

        //CAMPO DE REPETIR CONTRASEÑA
        MakeText(R.string.passwordRegisterValue2, modifier = Modifier.align(Alignment.Start))
        PasswordTextField(
            value = password2.value,
            onValueChange = {password2.value = it},
            placeholder = stringResource(R.string.placeholderRegisterScreen),
        )
        Spacer(modifier = Modifier.height(40.dp))

        //CAMPO DE FECHA DE NACIMIENTO
        MakeText(R.string.birthdayRegisterValue2, modifier = Modifier.align(Alignment.Start))
        DatePickerDocked(
            selectedDate = birthDate,
            onDateSelected = { newDate -> birthDate = newDate }
        )
        Spacer(modifier = Modifier.height(40.dp))

        //CAMPO SEXO
        MakeText(R.string.sexoRegisterValue, modifier = Modifier.align(Alignment.Start))
        GenderDropdownSelector(
            selectedOption = selectedGender,
            onOptionSelected = { gender -> selectedGender = gender },
        )
        Spacer(modifier = Modifier.height(40.dp))

        //Boton de REGISTRO
        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(50.dp),
            colors = ButtonDefaults.buttonColors(RedPeakFlow)
        ) {
            Text(
                text = stringResource(R.string.IniciaSesion),
                color = Black,
                fontSize = 15.sp
            )
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
    val interactionSource = remember { MutableInteractionSource() }

    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .height(55.dp)
            .height(55.dp)
            .size(300.dp),
        placeholder = {
            Text(placeholder, color = ShapeButton)
        },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Gray,
            focusedContainerColor = ShapeButton
        ),
        shape = RoundedCornerShape(8.dp),
        singleLine = true,
        interactionSource = interactionSource,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = if(keyboardType.equals(KeyboardType.Password)) PasswordVisualTransformation() else VisualTransformation.None,
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
            Text(placeholder, color = ShapeButton)
        },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Gray,
            focusedContainerColor = ShapeButton
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
 * Metodo para seleccionar la fecha de nacimiento del usuario, se guardará en un dato de tipo Date
 * @param Date: selected Date
 * @param Unit: onDateSelected
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDocked(
    selectedDate: Date?,            //parámetro: fecha seleccionada externamente
    onDateSelected: (Date) -> Unit   //parámetro: callback para selección
) {
    var showDatePicker by remember { mutableStateOf(false) }
    // Estado del DatePicker inicializado con la fecha externa
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate?.time
    )

    // Convertir la fecha seleccionada a String para mostrar
    val selectedDateText = selectedDate?.let { convertMillisToDate(it.time) } ?: ""

    Box(
        modifier = Modifier
            .height(55.dp)
            .size(300.dp),
    ) {
        OutlinedTextField(
            value = selectedDateText,
            onValueChange = { },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDatePicker = !showDatePicker }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select date",
                        tint = Color.White
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Gray,
                focusedContainerColor = ShapeButton
            )
        )

        if (showDatePicker) {
            Popup(
                onDismissRequest = { showDatePicker = false },
                alignment = Alignment.TopStart
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

                    // Botón de confirmación para aplicar la selección
                    Button(
                        onClick = {
                            datePickerState.selectedDateMillis?.let {
                                onDateSelected(Date(it))
                            }
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

// Función de conversión (sin cambios)
fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", java.util.Locale.getDefault())
    return formatter.format(Date(millis))
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
    // Definimos las opciones como pares (valor, recurso de texto)
    val genderOptions = listOf(
        "not_specified" to R.string.gender_notSpecified,
        "male" to R.string.gender_male,
        "female" to R.string.gender_female
    )

    // Estado para controlar la expansión del menú
    var expanded by remember { mutableStateOf(false) }

    // Texto a mostrar en el campo (basado en la opción seleccionada)
    val displayText = selectedOption?.let { selectedValue ->
        genderOptions.find { it.first == selectedValue }?.let {
            stringResource(id = it.second)
        }
    } ?: ""

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier =Modifier
            .padding(16.dp)
            .padding(horizontal = 30.dp)
    ) {
        // Campo de texto que muestra la selección actual
        OutlinedTextField(
            readOnly = true,
            value = displayText,
            onValueChange = {},
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            placeholder = { Text(text = stringResource(R.string.sexoRegisterValue), color = ShapeButton) },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Gray,
                focusedContainerColor = ShapeButton
            )
        )

        // Menú desplegable con las opciones
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