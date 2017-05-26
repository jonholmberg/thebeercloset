package today.spunk.thebeercloset.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.webkit.*
import org.jetbrains.anko.find
import org.jetbrains.anko.toast
import today.spunk.thebeercloset.R

class BeerActivity : AppCompatActivity() {

    val webview by lazy { find<WebView>(R.id.webview) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beer)
    }

    override fun onStart() {
        super.onStart()
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient( object : WebViewClient() {

            override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
                toast("Ehh you messed some shit up. Try again.")
            }
        })
        webview.loadUrl("http://thebeercloset.spunk.today")
    }
}
