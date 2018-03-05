package today.spunk.thebeercloset.utils

import android.app.job.JobScheduler
import android.app.job.JobInfo
import android.content.ComponentName
import android.content.Context
import today.spunk.thebeercloset.network.BeerService


/**
 * Created by Jonna on 08/12/2017.
 */
object Util {

    fun scheduleJob(context: Context) {
        val serviceComponent = ComponentName(context, BeerService::class.java)
        val builder = JobInfo.Builder(0, serviceComponent)
        builder.setOverrideDeadline((100).toLong())
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY) // require unmetered network
        //builder.setRequiresDeviceIdle(true) // device should be idle
        builder.setRequiresCharging(false)
        val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as? JobScheduler
        jobScheduler?.schedule(builder.build())
    }

}