package marcos.peakflow.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import marcos.peakflow.ui.theme.Gray

@Composable
fun StandardTopAppBar(
    title: String,
    onLeftClick: () -> Unit,
    leftIcon: Int,
    onRightClick: () -> Unit,
    rightIcon:Int
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        border = BorderStroke(1.dp, Gray)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 7.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onLeftClick) {
                Icon(
                    painter = painterResource(id = leftIcon),
                    contentDescription = "leftIcon",
                    tint = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier.size(20.dp)
                )
            }

            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onSecondary,
            )

            IconButton(onClick = onRightClick) {
                Icon(
                    painter = painterResource(id = rightIcon),
                    contentDescription = "RightIcon",
                    tint = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier.size(23.dp)
                )
            }
        }
    }
}