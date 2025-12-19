package com.example.submissionawal.di
import android.content.Context
import com.example.submissionawal.data.local.settingsPreferences
import com.example.submissionawal.data.local.dataStore
import com.example.submissionawal.data.local.db.FavEventsRoomDB
import com.example.submissionawal.data.remote.retrofit.apiConfigs
import com.example.submissionawal.data.repo.EventsRepo


object Injection {


    fun provideRepository(context: Context): EventsRepo {
        val apiService = apiConfigs.getApiService()
        val database = FavEventsRoomDB.getDatabase(context)
        val dao = database.favEventsDao()
        return EventsRepo.getInstance(apiService, dao)
    }


    fun providePreferences(context: Context): settingsPreferences {
        return settingsPreferences.getInstance(context.dataStore)
    }


}