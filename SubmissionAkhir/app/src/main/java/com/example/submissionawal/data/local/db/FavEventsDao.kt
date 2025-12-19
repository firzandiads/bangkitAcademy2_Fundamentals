package com.example.submissionawal.data.local.db
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.submissionawal.data.local.model.FavEvents


@Dao
interface FavEventsDao {


    @Insert(onConflict = OnConflictStrategy.IGNORE) suspend fun insertFavoriteEvent(event: FavEvents): Long

    @Delete suspend fun deleteFavoriteEvent(event: FavEvents)

    @Query("SELECT * from favorite_event") fun getAllFavoriteEvent(): LiveData<List<FavEvents>>

    @Query("SELECT * FROM favorite_event WHERE event_id = :eventId") fun getFavoriteEventById(eventId: Int): LiveData<FavEvents>
}