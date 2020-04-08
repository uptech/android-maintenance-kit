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
        testData: String? = null
    ): Deferred<MaintenanceResponse> {

        return CoroutineScope(coroutineContext).async {
            testData?.let { data ->
                gson.fromJson<MaintenanceResponse>(data, MaintenanceResponse::class.java)
            } ?: kotlin.run {
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
}