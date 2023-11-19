package dk.hug.treehugger.compose.data

import androidx.appcompat.app.AppCompatActivity
import com.fasterxml.jackson.databind.ObjectMapper
import dk.hug.treehugger.core.DBhandler
import dk.hug.treehugger.model.Root
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import java.net.URL

class TreeDownloaderKotlin : AppCompatActivity(), CoroutineScope by MainScope() {
    var url =
        "https://wfs-kbhkort.kk.dk/ows?service=wfs&version=1.0.0&request=GetFeature&typeName=k101:gadetraer&outputFormat=json&SRSNAME=EPSG:4326"

    fun doStuff() {
        async {
            withContext(Dispatchers.IO) {
                URL(url).openStream().use { input ->
                    DBhandler.storeTreeList(
                        ObjectMapper().readValue(input, Root::class.java)
                    )
                }
            }
        }
    }
}