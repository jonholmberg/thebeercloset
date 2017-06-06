package today.spunk.thebeercloset.utils

/**
 * Created by Jonna on 04/06/2017.
 */

enum class WidgetRequest(val request: String) {
    ButtonPlus("ButtonPlus"),
    ButtonMinus("ButtonMinus")
}

enum class LogKeys(val tag: String) {
    BeerActivity("BeerActivity"),
    HttpClient("BeerClosetHttpClient"),
    BeerWidget("BeerWidget")
}

enum class SharedPrefKeys(val key: String) {
    NAME("Name")
}