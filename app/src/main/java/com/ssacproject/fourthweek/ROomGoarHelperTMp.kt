package com.ssacproject.fourthweek

import com.ssacproject.fourthweek.room.RoomBoard
import com.ssacproject.fourthweek.room.RoomBoardDao

class ROomGoarHelperTMp {
}

//package com.ssacproject.fourthweek.room
//
//import android.content.Context
//import androidx.room.Database
//import androidx.room.Room
//import androidx.room.RoomDatabase
//
//@Database(entities = [RoomBoard::class], version = 1, exportSchema = false)
//abstract class RoomBoardHelper : RoomDatabase() {
//    abstract val roomBoardDao : RoomBoardDao
//
//    companion object {
//        @Volatile
//        private var INSTANCE: RoomBoardHelper? = null
//
//        fun getInstance(context: Context) : RoomBoardHelper {
//            synchronized(this) {
//                var instance = INSTANCE
//
//                if (instance == null) {
//                    instance = Room.databaseBuilder(
//                        context.applicationContext,
//                        RoomBoardHelper::class.java,
//                        "room_board3"
//                    )
//                        .fallbackToDestructiveMigration()
//                        .build()
//                    INSTANCE = instance
//                }
//                return instance
//            }
//        }
//
//    }
//}