package ch.upte.maintenance.kit.api

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MaintenanceResponse(
    @SerializedName("maintenance")
    val maintenance: Maintenance?,
    @SerializedName("upgrade")
    val upgrade: Upgrade?,
    val error: Boolean = false,
    val errorMessage: String? = null
) : Parcelable

@Parcelize
data class Maintenance(
    @SerializedName("active")
    val active: Boolean,
    @SerializedName("end_date")
    val endDate: String,
    @SerializedName("message")
    val message: Message?,
    @SerializedName("offline")
    val offline: Boolean,
    @SerializedName("scheduled")
    val scheduled: Boolean,
    @SerializedName("start_date")
    val startDate: String
) : Parcelable

@Parcelize
data class Message(
    @SerializedName("body")
    val body: String,
    @SerializedName("title")
    val title: String
) : Parcelable

@Parcelize
data class Upgrade(
    @SerializedName("platforms")
    val platforms: List<Platform>
) : Parcelable

@Parcelize
data class Platform(
    @SerializedName("latest_build_number")
    val latestBuildNumber: Int,
    @SerializedName("latest_version")
    val latestVersion: String,
    @SerializedName("message")
    val message: Message?,
    @SerializedName("minimum_build_number")
    val minimumBuildNumber: Int,
    @SerializedName("minimum_version")
    val minimumVersion: String,
    @SerializedName("platform")
    val platform: String,
    @SerializedName("required_update")
    val requiredUpdate: Boolean,
    @SerializedName("show_version_info")
    val showVersionInfo: Boolean,
    @SerializedName("store_url")
    val storeUrl: String
) : Parcelable

fun MaintenanceResponse.isMaintenanceOrAppUpdate(
    appVersion: String,
    notNow: Boolean
): MAINTENANCE_TYPE {
    val active = this.maintenance?.active ?: false
    return if (this.error) {
        MAINTENANCE_TYPE.ERROR
    } else {
        if (active) {
            MAINTENANCE_TYPE.MAINTENANCE
        } else {
            //check platform upgrade
            this.upgrade?.platforms?.firstOrNull { it.platform.toLowerCase() == "android" }
                ?.let { platform ->
                    val version = appVersion.split(".").joinToString("").toInt()
                    val minVersion = platform.minimumVersion.split(".").joinToString("").toInt()
                    val latestVersion = platform.latestVersion.split(".").joinToString("").toInt()
                    if (!notNow && (version < minVersion || version < latestVersion)) {
                        MAINTENANCE_TYPE.APP_UPDATE
                    } else {
                        MAINTENANCE_TYPE.NONE
                    }
                } ?: MAINTENANCE_TYPE.NONE
        }
    }
}

enum class MAINTENANCE_TYPE {
    ERROR,
    APP_UPDATE,
    MAINTENANCE,
    NONE
}