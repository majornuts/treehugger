package dk.hug.treehugger.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ScreenAbout() {

    Column(modifier = Modifier.padding(16.dp)) {
        Text("About:")

        Text(text = "The app is made by a group of volunteers, and is not affiliated with Københavns komune.")
        Spacer(modifier = Modifier.size(10.dp))
        Text(text = "Library used:")
        Text(text = "androidx.core:core-ktx")
        Text(text = "androidx.lifecycle:lifecycle-runtime-ktx")
        Text(text = "androidx.activity:activity-compose")
        Text(text = "androidx.compose.ui:ui")
        Text(text = "androidx.compose.ui:ui-graphics")
        Text(text = "androidx.compose.material3:material3")
        Text(text = "androidx.navigation:navigation-compose")
        Text(text = "org.jetbrains.kotlinx:kotlinx-coroutines-core")
        Text(text = "org.jetbrains.kotlinx:kotlinx-coroutines-android")
        Text(text = "com.google.maps.android:android-maps-utils")
        Text(text = "com.google.maps.android:maps-compose")
        Text(text = "com.google.maps.android:maps-compose-utils")
        Text(text = "com.fasterxml.jackson.core:jackson-core")
        Text(text = "com.fasterxml.jackson.core:jackson-databind")
        Text(text = "com.google.firebase:firebase-core")
        Text(text = "com.google.firebase:firebase-crashlytics")
        Text(text = "com.google.firebase:firebase-analytics")
        Spacer(modifier = Modifier.size(10.dp))
        Text(text = "Remember to hug a Tree ones in a while")
    }
}

