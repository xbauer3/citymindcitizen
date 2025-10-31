package com.example.projectobcane.ui.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.projectobcane.ui.theme.basicMargin

data class PlaceholderScreenContent(
    val text: String? = null,
    val image: Int? = null
)

@Composable
fun PlaceholderScreen(
    paddingValues: PaddingValues,
    placeholderScreenContent: PlaceholderScreenContent
){
    Box(modifier = Modifier.fillMaxSize().padding(
        top = paddingValues.calculateTopPadding(),
        start = basicMargin,
        end = basicMargin,
        bottom = basicMargin
    )){
        Column(modifier = Modifier.align(Alignment.Center)) {
            if (placeholderScreenContent.image != null){
                Image(
                    painter = painterResource(placeholderScreenContent.image),
                    contentDescription = null,
                    modifier = Modifier.size(300.dp),
                    contentScale = ContentScale.FillWidth
                )
            }
            if (placeholderScreenContent.text != null){
                Text(text = placeholderScreenContent.text)
            }
        }
    }

}



