package today.spunk.thebeercloset.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.webkit.*
import org.jetbrains.anko.find
import org.jetbrains.anko.toast
import today.spunk.thebeercloset.R
import today.spunk.thebeercloset.managers.BeerManager

class BeerActivity : AppCompatActivity() {

    val webview by lazy { find<WebView>(R.id.webview) }
    val swipeLayout by lazy { find<SwipeRefreshLayout>(R.id.swipeLayout)}

    val beerClosetUrl = "http://thebeercloset.spunk.today"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beer)
        setSwipeListener()
    }

    override fun onStart() {
        super.onStart()
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient( object : WebViewClient() {
            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                toast("Something went wrong #BestErrorMessage2013. Try again.")
            }
        })
        webview.loadUrl(beerClosetUrl)
        webview.setOnClickListener {
            BeerManager().addBeers("Jon", 1,
                    success = { totalBeers -> toast("Success! $totalBeers")},
                    failure = { toast("Failure!")})
        }
    }

    private fun setSwipeListener() {
        swipeLayout.setOnRefreshListener {
            webview.loadUrl(beerClosetUrl)
            toast("Noice!")
            swipeLayout.isRefreshing = false
        }
    }
}
