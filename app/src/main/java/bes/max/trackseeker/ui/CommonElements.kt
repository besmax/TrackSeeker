package bes.max.trackseeker.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bes.max.trackseeker.ui.theme.ysDisplayFamily

@Composable
fun Title(
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 12.dp)
    ) {
        Text(
            text = text,
            fontFamily = ysDisplayFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 22.sp
        )
    }
}


@Composable
@Preview
fun TitlePreview() {
    Title(text = "Поиск")
}