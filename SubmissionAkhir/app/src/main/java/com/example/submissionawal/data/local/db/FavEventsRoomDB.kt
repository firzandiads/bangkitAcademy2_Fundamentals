package com.example.submissionawal.data.local.db
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.submissionawal.data.local.model.FavEvents


@Database(entities = [FavEvents::class], version = 1, exportSchema = false)

abstract class FavEventsRoomDB : RoomDatabase() {

    abstract fun favEventsDao(): FavEventsDao


    companion object {
        @Volatile private var INSTANCE: FavEventsRoomDB? = null

        @JvmStatic fun getDatabase(context: Context): FavEventsRoomDB {
            if (INSTANCE == null) {
                synchronized(FavEventsRoomDB::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        FavEventsRoomDB::class.java, "favorite_event_database"
                    ).build()
                }
            }
            return INSTANCE as FavEventsRoomDB
        }
    }
}