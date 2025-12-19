package com.example.submissionawal.data.local.model
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Entity(tableName = "favorite_event")
@Parcelize
data class FavEvents(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "event_id")
    var eventId: Int? = null,
    @ColumnInfo(name = "name")
    var name: String? = null,
    @ColumnInfo(name = "desctiption")
    var description: String? = null,
    @ColumnInfo(name = "ownerName")
    var ownerName: String? = null,
    @ColumnInfo(name = "cityName")
    var cityName: String? = null,
    @ColumnInfo(name = "image")
    var image: String? = null
) : Parcelable