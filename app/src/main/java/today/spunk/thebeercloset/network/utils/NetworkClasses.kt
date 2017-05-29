package today.spunk.thebeercloset.network.utils

/**
 * Created by Jonna on 29/05/2017.
 */

enum class HttpMethod {
    GET,
    POST,
    PUT,
    DELETE
}

data class HttpResponse (val success: Boolean, val body: String?)