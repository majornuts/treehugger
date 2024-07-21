package dk.hug.treehugger.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import dk.hug.treehugger.R

@Composable
fun ScreenAbout() {

    Column {

        Spacer(modifier = Modifier.fillMaxHeight(0.4f))
        Text(
            text = "Made by volunteers, with no affiliation with Copenhagen Municipality.",
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Text(
            text = "Remember to hug a tree once in a while",
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )



        Row(
            modifier = Modifier.fillMaxHeight(1f),
            horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.Bottom
        ) {
            Image(
                modifier = Modifier.fillMaxWidth(),
                painter = painterResource(id = R.drawable.trees2),
                colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color(0xFF0B6411)),
                contentDescription = "Localized description",
            )
        }
    }
}
