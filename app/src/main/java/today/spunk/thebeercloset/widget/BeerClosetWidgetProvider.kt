package today.spunk.thebeercloset.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import today.spunk.thebeercloset.R
import today.spunk.thebeercloset.managers.BeerManager
import android.content.ComponentName
import android.util.Log
import today.spunk.thebeercloset.store.BeerStore
import today.spunk.thebeercloset.utils.LogKeys
import today.spunk.thebeercloset.utils.WidgetRequest


/**
 * Created by Jonna on 26/05/2017.
 */
class BeerClosetWidgetProvider : AppWidgetProvider() {

    val beerManager by lazy { BeerManager() }

    override fun onUpdate(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetIds: IntArray?) {
        val length = appWidgetIds?.size ?: 0

        (0 until length).forEach {
            val appWidgetId = appWidgetIds?.get(it)
            appWidgetId?.let {
                val views = RemoteViews(context?.packageName, R.layout.widget)
                views.setTextViewText(R.id.name_beers, "${BeerStore.name ?: "None"}: ${BeerStore.beers ?: "-"}")

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
                            name = BeerStore.name,
                            beers = 1,
                            success = { totalBeers ->
                                BeerStore.beers = totalBeers
                                onUpdate(context)
                            },
                            failure = {
                                Log.d(LogKeys.BeerWidget.tag, "Couldn't increment beers for ${BeerStore.name}")
                            }
                    )
            WidgetRequest.ButtonMinus.request ->
                    beerManager.addBeers(
                            name = BeerStore.name,
                            beers = -1,
                            success = { totalBeers ->
                                BeerStore.beers = totalBeers
                                onUpdate(context)
                            },
                            failure = {
                                Log.d(LogKeys.BeerWidget.tag, "Couldn't decrement beers for ${BeerStore.name}")
                            }
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