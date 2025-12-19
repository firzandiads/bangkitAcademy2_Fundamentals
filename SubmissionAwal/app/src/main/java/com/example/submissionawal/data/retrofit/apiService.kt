package com.example.submissionawal.data.retrofit
import com.example.submissionawal.data.response.detailResponse
import com.example.submissionawal.data.response.events
import com.example.submissionawal.data.response.eventsResponse
import retrofit2.Call
import retrofit2.http.*


interface apiService {


    @GET("events")
    fun getListEvents(@Query("active") active: Int, ):
            Call<eventsResponse>
    @GET("events/{id}")
    fun getDetailEvent(@Path("id") id: Int):
            Call<detailResponse<events>>
    @GET("events")
    fun searchEvents(@Query("active") active: Int = -1, @Query("q") keyword: String):
            Call<eventsResponse>
}