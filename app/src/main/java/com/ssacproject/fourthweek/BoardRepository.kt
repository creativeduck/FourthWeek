package com.ssacproject.fourthweek

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.ssacproject.fourthweek.room.RoomBoard
import com.ssacproject.fourthweek.room.RoomBoardDao
import com.ssacproject.fourthweek.room.RoomBoardHelper

class BoardRepository(application: Application, mode: String) {
    private var roomBoardDao : RoomBoardDao
    private var boardList : LiveData<List<RoomBoard>>

    init {
        val database: RoomBoardHelper = RoomBoardHelper.getInstance(application)
        roomBoardDao = database.roomBoardDao
        boardList = roomBoardDao.getModeList(mode)
    }

    fun insert(board: RoomBoard) {
        InsertBoardAsyncTask(roomBoardDao).execute(board)
    }
    fun delete(board: RoomBoard) {
        DeleteBoardAsyncTask(roomBoardDao).execute(board)
    }
    fun deleteAll() {
        DeleteAllBoardAsyncTask(roomBoardDao).execute()
    }
    fun getModeList() : LiveData<List<RoomBoard>> {
        return boardList
    }
    fun sortModeList() : LiveData<List<RoomBoard>> {
        

        return boardList
    }

    companion object {
        class InsertBoardAsyncTask(boardDao : RoomBoardDao) : AsyncTask<RoomBoard, Void, Void>() {
            private var boardDao : RoomBoardDao

            init {
                this.boardDao = boardDao
            }

            override fun doInBackground(vararg roomBoards: RoomBoard?): Void? {
                boardDao.insert(roomBoards[0]!!)
                return null
            }
        }

        class DeleteBoardAsyncTask(boardDao : RoomBoardDao) : AsyncTask<RoomBoard, Void, Void>() {
            private var boardDao : RoomBoardDao

            init {
                this.boardDao = boardDao
            }

            override fun doInBackground(vararg roomBoards: RoomBoard?): Void? {
                boardDao.delete(roomBoards[0]!!)

                return null
            }
        }
        class DeleteAllBoardAsyncTask(boardDao : RoomBoardDao) : AsyncTask<RoomBoard, Void, Void>() {
            private var boardDao : RoomBoardDao

            init {
                this.boardDao = boardDao
            }

            override fun doInBackground(vararg roomBoards: RoomBoard?): Void? {
                boardDao.deleteAll()
                return null
            }
        }
    }

}