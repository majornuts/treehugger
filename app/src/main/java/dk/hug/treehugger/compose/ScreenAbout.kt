package dk.hug.treehugger.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dk.hug.treehugger.R

@Composable
fun ScreenAbout() {

    Column {
        Spacer(modifier = Modifier.fillMaxHeight(0.2f))
        Text(
            text = "Created by volunteers",
            fontSize = 20.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Text(
            text = "No affiliation with Copenhagen Municipality",
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(0.dp,12.dp, 0.dp,2.dp)
        )
        Text(
            text = "Data open to public use:",
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(0.dp,12.dp, 0.dp,2.dp)
        )
        Text(
            text = buildAnnotatedString {
                withLink(
                    LinkAnnotation.Url(
                        "https://www.opendata.dk/city-of-copenhagen/gadetraeer ",
                        TextLinkStyles(style = SpanStyle(color = Color.Green))
                    )
                ) {
                    append("https://www.opendata.dk/city-of-copenhagen/gadetraeer ")
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(0.dp,4.dp, 0.dp,2.dp)
        )
        Text(
            text = "Remember to hug a tree once in a while",
            fontStyle = FontStyle.Italic,
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(0.dp,24.dp, 0.dp,4.dp)
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
