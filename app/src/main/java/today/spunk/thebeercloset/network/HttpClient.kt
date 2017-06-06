package today.spunk.thebeercloset.network

import android.util.Log
import okhttp3.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import today.spunk.thebeercloset.extensions.getBodyAndClose
import today.spunk.thebeercloset.network.utils.HttpMethod
import today.spunk.thebeercloset.network.utils.HttpResponse
import today.spunk.thebeercloset.utils.FailureBlock
import today.spunk.thebeercloset.utils.LogKeys
import today.spunk.thebeercloset.utils.SuccessBlock

/**
 * Created by Jonna on 29/05/2017.
 */
object HttpClient {

    private val httpClient by lazy { OkHttpClient() }
    val baseUrl = "http://spunk.today/thebeercloset/"
    val JSONMediaType = MediaType.parse("application/json; charset=utf-8")

    fun getRemoteObject(path: String? = null,
                        success: SuccessBlock? = null,
                        failure: FailureBlock? = null,
                        queryParameters: Map<String, String>? = null) {
        doAsync {

            val completePath = buildPath(path, queryParameters)
            val response = executeRequest(method = HttpMethod.GET, path = completePath)

            uiThread {
                onResponse(method = HttpMethod.GET, response = response, success = success, failure = failure, path = completePath)
            }
        }
    }

    private fun onResponse(method: HttpMethod, response: HttpResponse, success: SuccessBlock? = null,
                           failure: FailureBlock? = null, path: HttpUrl?) {
        if (response.success) {
            Log.d(LogKeys.HttpClient.tag, "Successful $method request to $path")
            success?.invoke(response.body)
        } else {
            Log.d(LogKeys.HttpClient.tag, "Error in $method request to $path")
            failure?.invoke()
        }
    }

    private fun executeRequest(method: HttpMethod, path: HttpUrl?, mediaType: MediaType? = JSONMediaType, body: String? = null) : HttpResponse {
        val request = Request.Builder().url(path)
        when (method) {
            HttpMethod.POST -> request.post(RequestBody.create(mediaType, body))
            HttpMethod.PUT -> request.put(RequestBody.create(mediaType, body))
            HttpMethod.DELETE -> request.delete()
            else -> Unit
        }
        val response = httpClient.newCall(request.build()).execute()
        return HttpResponse(response.isSuccessful, response?.getBodyAndClose())
    }

    private fun buildPath(path: String?, queryParameters: Map<String, String>? = null) : HttpUrl? {

        val completePath = HttpUrl.parse(baseUrl)?.newBuilder()
        path?.let { path ->  completePath?.addPathSegment(path) }
        queryParameters?.forEach {
            key, value -> completePath?.addQueryParameter(key, value)
        }
        return completePath?.build()
    }
}