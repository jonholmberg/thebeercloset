package today.spunk.thebeercloset.network

import android.util.Log
import okhttp3.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import today.spunk.thebeercloset.extensions.getBodyAndClose
import today.spunk.thebeercloset.network.utils.HttpMethod
import today.spunk.thebeercloset.network.utils.HttpResponse
import today.spunk.thebeercloset.store.BeerStore
import today.spunk.thebeercloset.utils.FailureBlock
import today.spunk.thebeercloset.utils.LogKeys
import today.spunk.thebeercloset.utils.SuccessBlock

/**
 * Created by Jonna on 29/05/2017.
 */
object HttpClient {

    private val httpClient by lazy { OkHttpClient() }

    fun buildUrl(name : String, beers: Int) : String {
        return "http://spunk.today/thebeercloset/?name=$name&beers=$beers"
    }

    fun simpleAddBeer(name: String, beers: Int = 0, success: SuccessBlock?) {

        doAsync {
            val request = Request.Builder()
                    .url(buildUrl(name, beers))
                    .build()

            val response = httpClient.newCall(request).execute()

            uiThread {
                success?.invoke(response.body()?.string())
            }
        }
    }
}