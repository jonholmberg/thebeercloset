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
import today.spunk.thebeercloset.managers.BeerManager
import today.spunk.thebeercloset.widget.BeerClosetWidgetProvider

class BeerActivity : AppCompatActivity() {

    val webview by lazy { find<WebView>(R.id.webview) }
    val swipeLayout by lazy { find<SwipeRefreshLayout>(R.id.swipeLayout)}

    val sharedPreferences : SharedPreferences by lazy { getPreferences(Context.MODE_PRIVATE) }
    val beerClosetUrl = "http://thebeercloset.spunk.today"

    companion object {
        var name: String? = null
        var beers: String? = null
        val NAME_KEY = "nameKey"
    }

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

        if (name == null) {
            NameDialogFragment({ name -> addName(name) }).show(supportFragmentManager, "NameDialogFragment")
        }

        supportActionBar?.title = "The Beer Closet: $name"

        BeerManager().addBeers(
                name = name,
                beers = 0,
                success = { totalBeers ->
                    beers = totalBeers
                    val appWidgetManager = AppWidgetManager.getInstance(this)
                    val remoteViews = RemoteViews(this.packageName, R.layout.widget)
                    val widget = ComponentName(this, BeerClosetWidgetProvider::class.java)
                    remoteViews.setTextViewText(R.id.name_beers, "${name ?: "None"}: ${beers ?: "-"}")
                    appWidgetManager.updateAppWidget(widget, remoteViews)
                },
                failure = {})
    }

    private fun setSwipeListener() {
        swipeLayout.setOnRefreshListener {
            webview.loadUrl(beerClosetUrl)
            name?.also { name ->
                    BeerManager().addBeers(name, 1,
                            success = { totalBeers -> toast("Success!")},
                            failure = { toast("Failure!")})
            }
            swipeLayout.isRefreshing = false
        }
    }

    private fun addName(name : String) {
        sharedPreferences.edit().putString(NAME_KEY, name).apply()
    }

    private fun setupData() {
        name = sharedPreferences.getString(NAME_KEY, null)
    }
}
