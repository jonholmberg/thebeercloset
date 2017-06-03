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



/**
 * Created by Jonna on 26/05/2017.
 */
class BeerClosetWidgetProvider : AppWidgetProvider() {

    private val BUTTON_PLUS = "ButtonPlus"

    override fun onUpdate(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetIds: IntArray?) {
        val length = appWidgetIds?.size ?: 0

        for (i in 0..length-1) {
            val appWidgetId = appWidgetIds?.get(i)
            appWidgetId?.let {
                val views = RemoteViews(context?.packageName, R.layout.widget)
                views.setTextViewText(R.id.name_beers, "${BeerActivity.name ?: "None"}: ${BeerActivity.beers ?: "-"}")
                appWidgetManager?.updateAppWidget(appWidgetId, views)

                val pendingIntent = PendingIntent.getBroadcast(context, 0, Intent(context, javaClass).setAction(BUTTON_PLUS), PendingIntent.FLAG_UPDATE_CURRENT)
                views.setOnClickPendingIntent(R.id.button_plus, pendingIntent)
            }
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        if (intent?.action == BUTTON_PLUS) {
            BeerManager().addBeers(
                    BeerActivity.name,
                    1,
                    success = { totalBeers ->
                        BeerActivity.beers = totalBeers
                        onUpdate(context)
                    },
                    failure = {}
            )
        }
    }

    private fun onUpdate(context: Context?) {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val thisAppWidgetComponentName = ComponentName(context?.packageName, javaClass.name)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidgetComponentName)
        onUpdate(context, appWidgetManager, appWidgetIds)
    }
}