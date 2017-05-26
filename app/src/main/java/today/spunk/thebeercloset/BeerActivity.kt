package today.spunk.thebeercloset

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView

class BeerActivity : AppCompatActivity() {

    val webview by lazy { findViewById(R.id.webview) as? WebView }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beer)
    }

    override fun onStart() {
        super.onStart()
        webview?.loadUrl("http://www.thebeercloset.spunk.today/");
    }
}
