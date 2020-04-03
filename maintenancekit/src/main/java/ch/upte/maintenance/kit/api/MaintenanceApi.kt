package ch.upte.maintenance.kit.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface MaintenanceApi {
    @GET
    fun maintenanceReport(@Url reportUrl: String): Call<MaintenanceResponse>
}