package com.example.submissionawal.data.remote.response
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
data class detailResponse<T>(


    @field:SerializedName("error") val error: Boolean,
    @field:SerializedName("message") val message: String,
    @field:SerializedName("event") val event: events
) : Parcelable

@Parcelize
data class events(


    @field:SerializedName("id") val id: Int? = null,
    @field:SerializedName("name") val name: String? = null,
    @field:SerializedName("summary") val summary: String? = null,
    @field:SerializedName("description") val description: String? = null,
    @field:SerializedName("imageLogo") val imageLogo: String? = null,
    @field:SerializedName("mediaCover") val mediaCover: String? = null,
    @field:SerializedName("category") val category: String? = null,
    @field:SerializedName("ownerName") val ownerName: String? = null,
    @field:SerializedName("cityName") val cityName: String? = null,
    @field:SerializedName("quota") val quota: Int? = null,
    @field:SerializedName("registrants") val registrants: Int? = null,
    @field:SerializedName("beginTime") val beginTime: String? = null,
    @field:SerializedName("endTime") val endTime: String? = null,
    @field:SerializedName("link") val link: String? = null
) : Parcelable