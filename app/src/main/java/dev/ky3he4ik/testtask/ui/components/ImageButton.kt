package dev.ky3he4ik.testtask.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun ImageButton(onClick: () -> Unit, resource: Int, description: String) {
    Button(
        onClick = onClick,
        border = BorderStroke(1.dp, MaterialTheme.colors.secondary),
        shape = RoundedCornerShape(50),
        modifier = Modifier.padding(10.dp)
    ) {
        Image(
            painter = painterResource(id = resource),
            contentDescription = description,
            modifier = Modifier.defaultMinSize(25.dp, 25.dp)
        )
    }
}
