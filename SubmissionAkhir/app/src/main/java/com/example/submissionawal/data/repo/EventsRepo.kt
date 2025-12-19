package com.example.submissionawal.data.repo
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.submissionawal.data.local.db.FavEventsDao
import com.example.submissionawal.data.local.model.FavEvents
import com.example.submissionawal.data.remote.response.events
import com.example.submissionawal.data.remote.response.listEventsItem
import com.example.submissionawal.data.remote.retrofit.apiServices

class EventsRepo(

    private val favoriteEventDao: FavEventsDao,
    private val apiService: apiServices

) {
    suspend fun getUpcomingEvent(): Result<List<listEventsItem?>> {
        return try {
            val response = apiService.getAllActiveEvent()
            if (response.isSuccessful) {
                Result.success(response.body()?.listEvents ?: emptyList())
            } else {
                Result.failure(Exception("Failed to load data from API, Status code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun getFinishedEvent(): Result<List<listEventsItem?>> {
        return try {
            val response = apiService.getAllFinishedEvent()
            if (response.isSuccessful) {
                Result.success(response.body()?.listEvents ?: emptyList())
            } else {
                Result.failure(Exception("Failed to load data from API, Status code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun getDetailEvent(id: Int): Result<events?> {
        return try {
            val response = apiService.getDetailEvent(id)
            if (response.isSuccessful) {
                val event = response.body()?.event ?: throw Exception("Event not found")
                Result.success(event)
            } else {
                Result.failure(Exception("Failed to load data from API, Status code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun searchEvent(keyword: String): Result<List<listEventsItem?>> {
        return try {
            val response = apiService.searchEvents(keyword)
            if (response.isSuccessful) {
                Result.success(response.body()?.listEvents ?: emptyList())
            } else {
                Result.failure(Exception("Failed to load data from API, Status code: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun insertFavoriteEvent(event: FavEvents): Boolean {
        return try {
            val result = favoriteEventDao.insertFavoriteEvent(event)
            Log.d("Insert Event Respository", "Insert Favorite Event: $result")
            result != -1L
            true
        } catch (e: Exception) {
            Log.e("Insert Event Repository", "Error inserting favorite event: ${e.message}")
            false
        }
    }


    suspend fun deleteFavoriteEvent(event: FavEvents): Boolean {
        return try {
            val result = favoriteEventDao.deleteFavoriteEvent(event)
            Log.d("Delete event Respositoty", "Delete Favorite Event: $result")
            true
        } catch (e: Exception) {
            Log.e("Delete EventRepository", "Error deleting favorite event: ${e.message}")
            false
        }
    }


    fun getAllFavoriteEvent(): LiveData<List<FavEvents>> {
        return favoriteEventDao.getAllFavoriteEvent()
    }


    fun getFavoriteEventById(Id: Int): LiveData<FavEvents> {
        return favoriteEventDao.getFavoriteEventById(Id)
    }


    companion object {
        @Volatile
        private var instance: EventsRepo? = null
        fun getInstance(
            apiService: apiServices,
            favoriteEventDao: FavEventsDao
        ): EventsRepo =
            instance ?: synchronized(this) {
                instance ?: EventsRepo(favoriteEventDao, apiService)
            }
                .also { instance = it }
    }
}