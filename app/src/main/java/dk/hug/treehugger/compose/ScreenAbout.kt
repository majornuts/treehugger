package dk.hug.treehugger.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dk.hug.treehugger.R

@Composable
fun ScreenAbout() {


    Column(modifier = Modifier.padding(10.dp)) {
        Spacer(modifier = Modifier.size(10.dp))
        Text(text = "The app is made by a group of volunteers." , fontSize = 20.sp)
        Spacer(modifier = Modifier.size(10.dp))
        Text(text = "Remember to hug a Tree ones in a while")
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier.align(Alignment.BottomCenter)) {
            Image(
                modifier = Modifier.fillMaxWidth(),
                painter = painterResource(id = R.drawable.trees2),
                colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color(0xFF0B6411)),
                contentDescription = "Localized description",
            )
        }
    }

}
