package com.ssacproject.fourthweek.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RoomBoardDao {
    @Query("select * from room_board3")
    fun getAll() : List<RoomBoard>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(board: RoomBoard)

    @Delete
    fun delete(board: RoomBoard)

    @Query("delete from room_board3")
    fun deleteAll()

    @Query("select * from room_board3 where mode = :inputTitle")
    fun getModeList(inputTitle: String) : LiveData<List<RoomBoard>>

//    @Query("select * from room_board3 where mode = :inputTitle")
//    suspend fun getModeListSuspend(inputTitle: String) : LiveData<List<RoomBoard>>
//
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSuspend(board: RoomBoard)


}