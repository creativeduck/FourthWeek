package com.ssacproject.fourthweek.room

import android.content.Context
import android.os.Parcelable
import androidx.room.*
import kotlinx.coroutines.joinAll
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "room_board3")
data class RoomBoard(
    @PrimaryKey(autoGenerate = true)
    val no: Long?,
    @ColumnInfo
    val name: String,
    @ColumnInfo
    val character: Int,
    @ColumnInfo
    val score: Int,
    @ColumnInfo
    val mode: String
) : Parcelable