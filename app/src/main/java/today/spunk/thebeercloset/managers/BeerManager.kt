package today.spunk.thebeercloset.managers

import today.spunk.thebeercloset.network.HttpClient
import today.spunk.thebeercloset.utils.FailureBlock
import today.spunk.thebeercloset.utils.SuccessBlock

/**
 * Created by Jonna on 29/05/2017.
 */
class BeerManager {

    fun addBeers(name: String?, beers: Int, success: SuccessBlock, failure: FailureBlock) {
        name?.let {
            HttpClient.getRemoteObject(
                    success = success,
                    failure = failure,
                    queryParameters = mapOf(
                            "name" to name,
                            "beers" to beers.toString()
                    )
            )
        }
    }
}