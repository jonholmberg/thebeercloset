package today.spunk.thebeercloset.network

import android.app.job.JobParameters
import android.content.Intent
import android.app.job.JobService
import today.spunk.thebeercloset.utils.Util


/**
 * Created by Jonna on 08/12/2017.
 */
class BeerService : JobService() {

        override fun onStartJob(params: JobParameters): Boolean {
            val service = Intent(applicationContext, BeerService::class.java)
            applicationContext.startService(service)
            Util.scheduleJob(applicationContext) // reschedule the job
            return true
        }

        override fun onStopJob(params: JobParameters): Boolean {
            return true
        }

}