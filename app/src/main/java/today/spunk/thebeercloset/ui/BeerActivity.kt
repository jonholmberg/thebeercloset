package today.spunk.thebeercloset.ui

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.webkit.*
import android.widget.RemoteViews
import org.jetbrains.anko.find
import org.jetbrains.anko.toast
import today.spunk.thebeercloset.R
import today.spunk.thebeercloset.network.HttpClient
import today.spunk.thebeercloset.store.BeerStore
import today.spunk.thebeercloset.utils.SharedPrefKeys
import today.spunk.thebeercloset.widget.BeerClosetWidgetProvider

class BeerActivity : AppCompatActivity() {

    val webview by lazy { find<WebView>(R.id.webview) }
    val swipeLayout by lazy { find<SwipeRefreshLayout>(R.id.swipeLayout)}

    val sharedPreferences : SharedPreferences by lazy { getPreferences(Context.MODE_PRIVATE) }
    val beerClosetUrl = "http://thebeercloset.spunk.today"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beer)
        setSwipeListener()
    }

    override fun onStart() {
        super.onStart()
        webview.settings.setJavaScriptEnabled(true);
        webview.setWebViewClient( object : WebViewClient() {
            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                toast("Something went wrong #BestErrorMessage2013. Try again.")
            }
        })
        webview.loadUrl(beerClosetUrl)

        setupData()

        if (BeerStore.name == null) {
            NameDialogFragment({ name ->
                addName(name)
                BeerStore.name = name
                supportActionBar?.title = "The Beer Closet: ${BeerStore.name}"
                getTotalBeers()}).show(supportFragmentManager, "NameDialogFragment")
        } else {
            getTotalBeers()
            supportActionBar?.title = "The Beer Closet: ${BeerStore.name}"
        }
    }

    private fun setSwipeListener() {
        swipeLayout.setOnRefreshListener {
            webview.loadUrl(beerClosetUrl)
            swipeLayout.isRefreshing = false
        }
    }

    private fun addName(name : String) {
        sharedPreferences.edit().putString(SharedPrefKeys.NAME.key, name).apply()
    }

    private fun setupData() {
        BeerStore.name = sharedPreferences.getString(SharedPrefKeys.NAME.key, null)
    }

    private fun getTotalBeers() {
        HttpClient.simpleAddBeer(BeerStore.name ?: "") { totalBeers ->
            BeerStore.beers = totalBeers
            val appWidgetManager = AppWidgetManager.getInstance(this)
            val remoteViews = RemoteViews(this.packageName, R.layout.widget)
            val widget = ComponentName(this, BeerClosetWidgetProvider::class.java)
            remoteViews.setTextViewText(R.id.name_beers, "${BeerStore.name ?: "Mr. Spunk"}: ${BeerStore.beers ?: "Not enough"}")
            appWidgetManager.updateAppWidget(widget, remoteViews)
        }
    }
}
