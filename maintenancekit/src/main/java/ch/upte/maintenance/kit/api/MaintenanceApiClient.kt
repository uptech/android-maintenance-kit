@file:Suppress("BlockingMethodInNonBlockingContext")

package ch.upte.maintenance.kit.api

import ch.upte.maintenance.kit.BuildConfig
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

object MaintenanceApiClient {

    private lateinit var okHttpClient: OkHttpClient

    private val gson by lazy {
        Gson()
    }
    private val okHttpLogger by lazy {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        interceptor
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("http://upte.ch")
            .client(okHttpClient)
            .build()
    }

    private val api by lazy {
        retrofit.create(MaintenanceApi::class.java)
    }


    fun init() {
        if (!::okHttpClient.isInitialized) {
            okHttpClient = if (BuildConfig.DEBUG) {
                Timber.e("!!!WARNING!!! - Running in Debug mode. All HTTP data will be logged to LogCat!")
                OkHttpClient.Builder().addInterceptor(okHttpLogger).build()
            } else {
                OkHttpClient.Builder().build()
            }
        }
    }

    fun getMaintenanceReportAsync(
        maintenanceUrl: String,
        coroutineContext: CoroutineContext,
        useTestData: Boolean = false
    ): Deferred<MaintenanceResponse> {

        return CoroutineScope(coroutineContext).async {
            if (useTestData) {
                gson.fromJson<MaintenanceResponse>(testData, MaintenanceResponse::class.java)
            } else {
                try {
                    val executed = api.maintenanceReport(maintenanceUrl).execute()
                    if (executed.isSuccessful) {
                        executed.body()?.let { response ->
                            MaintenanceResponse(response.maintenance, response.upgrade)
                        } ?: kotlin.run {
                            MaintenanceResponse(null, null, true, "Unable to parse response body")
                        }
                    } else {
                        MaintenanceResponse(null, null, true, executed.message())
                    }

                } catch (ex: Exception) {
                    MaintenanceResponse(null, null, true, ex.message)
                }
            }
        }
    }

    private val testData by lazy {
        "{\n" +
                "    \"upgrade\": {\n" +
                "        \"platforms\": [{\n" +
                "            \"platform\": \"ios\",\n" +
                "            \"latest_version\": \"5.7.2\",\n" +
                "            \"latest_build_number\": 2387,\n" +
                "            \"store_url\": \"https://apps.apple.com/us/app/invoy/id1444260845?ls=1\",\n" +
                "            \"minimum_version\": \"5.0.0\",\n" +
                "            \"minimum_build_number\": 0,\n" +
                "            \"required_update\": false,\n" +
                "            \"show_version_info\": true,\n" +
                "            \"message\": {\n" +
                "                \"title\": \"App Update\",\n" +
                "                \"body\": \"There's a new version of the Invoy app available. You'll need to update your app to continue using Invoy.\"\n" +
                "            }\n" +
                "        },\n" +
                "        {\n" +
                "            \"platform\": \"android\",\n" +
                "            \"latest_version\": \"2.3.0\",\n" +
                "            \"latest_build_number\": 230,\n" +
                "            \"store_url\": \"https://play.google.com/store/apps/details?id=com.invoy.app\",\n" +
                "            \"minimum_version\": \"2.3.0\",\n" +
                "            \"minimum_build_number\": 230,\n" +
                "            \"required_update\": false,\n" +
                "            \"show_version_info\": true,\n" +
                "            \"message\": {\n" +
                "                \"title\": \"App Update\",\n" +
                "                \"body\": \"There's a new version of the Invoy app available. You'll need to update your app to continue using Invoy.\"\n" +
                "            }\n" +
                "        }]\n" +
                "    },\n" +
                "    \"maintenance\": {\n" +
                "        \"active\": false,\n" +
                "        \"offline\": false,\n" +
                "        \"scheduled\": false,\n" +
                "        \"start_date\": \"2020-03-29T09:40:56+0000\",\n" +
                "        \"end_date\": \"2020-03-31T09:40:56+0000\",\n" +
                "        \"message\": {\n" +
                "            \"title\": \"Invoy Maintenance\",\n" +
                "            \"body\": \"We are currently performing some upgrades. Please check back later.\"\n" +
                "        }\n" +
                "    }\n" +
                "}"
    }
}