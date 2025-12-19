package com.example.submissionawal.data.remote.retrofit
import com.example.submissionawal.data.remote.response.detailResponse
import com.example.submissionawal.data.remote.response.events
import com.example.submissionawal.data.remote.response.eventsResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface apiServices {


    @GET("events")
    suspend fun getAllActiveEvent(
        @Query("active") active: Int = 1,
    ): Response<eventsResponse>



    @GET("events")
    suspend fun getAllFinishedEvent(
        @Query("active") active: Int = 0,
    ): Response<eventsResponse>



    @GET("events")
    suspend fun searchEvents(
        @Query("q") keyword: String,
        @Query("active") active: Int = -1,
    ): Response<eventsResponse>



    @GET("events/{id}")
    suspend fun getDetailEvent(
        @Path("id") id: Int
    ): Response<detailResponse<events?>>

}