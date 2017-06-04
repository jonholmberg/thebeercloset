package today.spunk.thebeercloset.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import today.spunk.thebeercloset.R
import today.spunk.thebeercloset.managers.BeerManager
import today.spunk.thebeercloset.ui.BeerActivity
import android.content.ComponentName
import today.spunk.thebeercloset.utils.WidgetRequest


/**
 * Created by Jonna on 26/05/2017.
 */
class BeerClosetWidgetProvider : AppWidgetProvider() {

    val beerManager by lazy {BeerManager()}

    override fun onUpdate(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetIds: IntArray?) {
        val length = appWidgetIds?.size ?: 0

        for (i in 0..length-1) {
            val appWidgetId = appWidgetIds?.get(i)
            appWidgetId?.let {
                val views = RemoteViews(context?.packageName, R.layout.widget)
                views.setTextViewText(R.id.name_beers, "${BeerActivity.name ?: "None"}: ${BeerActivity.beers ?: "-"}")

                val buttonPlusPendingIntent = PendingIntent.getBroadcast(context, 0, Intent(context, javaClass).setAction(WidgetRequest.ButtonPlus.request), PendingIntent.FLAG_UPDATE_CURRENT)
                val buttonMinusPendingIntent = PendingIntent.getBroadcast(context, 1, Intent(context, javaClass).setAction(WidgetRequest.ButtonMinus.request), PendingIntent.FLAG_UPDATE_CURRENT)
                views.setOnClickPendingIntent(R.id.button_plus, buttonPlusPendingIntent)
                views.setOnClickPendingIntent(R.id.button_minus, buttonMinusPendingIntent)

                appWidgetManager?.updateAppWidget(appWidgetId, views)
            }
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        when (intent?.action) {
            WidgetRequest.ButtonPlus.request ->
                    beerManager.addBeers(
                            BeerActivity.name,
                            1,
                            success = { totalBeers ->
                                BeerActivity.beers = totalBeers
                                onUpdate(context)
                            },
                            failure = {}
                    )
            WidgetRequest.ButtonMinus.request ->
                    beerManager.addBeers(
                            BeerActivity.name,
                            -1,
                            success = { totalBeers ->
                                BeerActivity.beers = totalBeers
                                onUpdate(context)
                            },
                            failure = {}
                    )
        }
    }

    // Call onUpdate only with context.
    private fun onUpdate(context: Context?) {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val thisAppWidgetComponentName = ComponentName(context?.packageName, javaClass.name)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidgetComponentName)
        onUpdate(context, appWidgetManager, appWidgetIds)
    }
}