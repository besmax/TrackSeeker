package bes.max.trackseeker.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bes.max.trackseeker.R
import bes.max.trackseeker.presentation.search.SearchViewModel
import bes.max.trackseeker.ui.theme.YpLightGray
import bes.max.trackseeker.ui.theme.ysDisplayFamily
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen(
    searchViewModel: SearchViewModel = koinViewModel()
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Title(text = stringResource(id = R.string.search))
        UserInput()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInput() {
    var text by remember { mutableStateOf("") }
    TextField(
        value = text,
        onValueChange = { text = it },
        label = { Text(text = stringResource(id = R.string.search)) },
        maxLines = 1,
        textStyle = TextStyle(
            fontFamily = ysDisplayFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        ),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = YpLightGray,
            disabledIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
            ),
        leadingIcon = { Icon(painter = painterResource(id = R.drawable.ic_find), contentDescription = "search icon") },
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
@Preview
fun UserInputPreview() {
    UserInput()
}