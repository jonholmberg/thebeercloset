package today.spunk.thebeercloset.extensions

import android.util.Log
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.IOException

/**
 * Created by Jonna on 29/05/2017.
 */

fun Response.getBodyAndClose() : String? =
        try {
            this.body()?.string()
        } catch (exception: IOException) {
            Log.d("HTTP CLIENT ERROR", "Error getting body from response $this")
            this.close()
            null
        }