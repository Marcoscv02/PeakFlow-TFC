package marcos.peakflow.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import marcos.peakflow.ui.theme.Gray

@Composable
fun TripleTopAppBar(
    title: String,
    onLeftClick: () -> Unit,
    leftIcon: Int,
    onMidleClick: () -> Unit,
    middleIcon: Int,
    onRightClick: () -> Unit,
    rightIcon: Int
) {
    Surface(
        color = Gray,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Icono izquierdo (alineado al inicio)
            IconButton(
                onClick = onLeftClick,
                modifier = Modifier
                    .weight(0.9f, fill = false)
                    .padding(horizontal = 4.dp)
            ) {
                Icon(
                    painter = painterResource(id = leftIcon),
                    contentDescription = "leftIcon",
                    tint = Color.LightGray,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Texto central (con peso máximo para centrar)
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.LightGray,
                modifier = Modifier
                    .weight(3.4f) // Peso mayor para centrar en el espacio disponible
                    .wrapContentWidth(Alignment.CenterHorizontally),
                textAlign = TextAlign.End
            )

            // Contenedor para iconos derechos (alineados al final)
            Row(
                modifier = Modifier
                    .weight(1f, fill = true)
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onMidleClick) {
                    Icon(
                        painter = painterResource(id = middleIcon),
                        contentDescription = "MiddleIcon",
                        tint = Color.LightGray,
                        modifier = Modifier.size(21.dp)
                    )
                }
                IconButton(onClick = onRightClick) {
                    Icon(
                        painter = painterResource(id = rightIcon),
                        contentDescription = "RightIcon",
                        tint = Color.LightGray,
                        modifier = Modifier.size(21.dp)
                    )
                }
            }
        }
    }
}