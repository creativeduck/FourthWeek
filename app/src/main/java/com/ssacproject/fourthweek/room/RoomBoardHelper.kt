package com.ssacproject.fourthweek.room

import android.content.Context
import android.os.AsyncTask
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ssacproject.fourthweek.view.LoadingActivity

@Database(entities = [RoomBoard::class], version = 1, exportSchema = false)
abstract class RoomBoardHelper : RoomDatabase() {
    abstract val roomBoardDao : RoomBoardDao

    companion object {
        @Volatile
        private var INSTANCE: RoomBoardHelper? = null

        fun getInstance(context: Context) : RoomBoardHelper {
            // only single data base can be made
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        RoomBoardHelper::class.java,
                        "room_board3"
                    )
                        .fallbackToDestructiveMigration()
                        .addCallback(roomCallback)
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }

        private val roomCallback: RoomDatabase.Callback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                PopulateDbAsyncTask(INSTANCE!!).execute()
            }
        }
        private class PopulateDbAsyncTask(db: RoomBoardHelper) : AsyncTask<Void, Void, Void>() {
            private lateinit var boardDao: RoomBoardDao

            init {
                boardDao = db.roomBoardDao
            }

            override fun doInBackground(vararg p0: Void?): Void? {
                boardDao.insert(RoomBoard(null, "dksrhkdals", LoadingActivity.CHARACTER_RABBIT,
                                10000, "EASY"))

                return null
            }
        }

    }
}