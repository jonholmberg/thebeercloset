package today.spunk.thebeercloset.network

import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import today.spunk.thebeercloset.utils.Util


/**
 * Created by Jonna on 08/12/2017.
 */
class BeerServiceReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Util.scheduleJob(context)
    }
}