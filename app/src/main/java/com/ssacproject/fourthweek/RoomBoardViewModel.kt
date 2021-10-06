package com.ssacproject.fourthweek

import android.app.Application
import androidx.lifecycle.*
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ssacproject.fourthweek.room.RoomBoard
import com.ssacproject.fourthweek.room.RoomBoardDao
import com.ssacproject.fourthweek.room.RoomBoardDao_Impl
import com.ssacproject.fourthweek.room.RoomBoardHelper
import com.ssacproject.fourthweek.view.LoadingActivity
import kotlinx.coroutines.launch


class RoomBoardViewModel(application: Application, mode: String) : AndroidViewModel(application) {
    private lateinit var repository: BoardRepository
    private lateinit var boardList: LiveData<List<RoomBoard>>

    init {
        repository = BoardRepository(application, mode)
        boardList = repository.getModeList()
    }

    fun insert(board: RoomBoard) {
        repository.insert(board)
    }
    fun delete(board: RoomBoard) {
        repository.delete(board)
    }

    fun deleteAll(board: RoomBoard) {
        repository.deleteAll()
    }

    fun getModeList() : LiveData<List<RoomBoard>> {
        return boardList
    }
    fun sortModeList() : LiveData<List<RoomBoard>> {
        val tmp = boardList


        return boardList
    }
}












//class RoomBoardViewModel(val database: RoomBoardDao, application: Application) : AndroidViewModel(application) {
//
//    private val _list = MutableLiveData<List<RoomBoard>>()
//
//    val list : LiveData<List<RoomBoard>>
//        get() = _list
//
//    init {
//        viewModelScope.launch {
//            val newList = LoadingActivity.boardHelper?.roomBoardDao?.getModeList("EASY")
//            _list.value = newList!!.sortedByDescending { it.score }
//        }
//    }
//
//    fun setList(mode: String) {
//        viewModelScope.launch {
//            val newList = LoadingActivity.boardHelper?.roomBoardDao?.getModeList(mode)
//            _list.value = newList!!.sortedByDescending { it.score }
//        }
//    }
//}

//class RoomBoardViewModelFactory(
//    private val dataSource : RoomBoardDao,
//    private val application: Application) : ViewModelProvider.Factory {
//    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(RoomBoardViewModel::class.java)) {
//            return RoomBoardViewModel(dataSource, application) as T
//        }
//        throw IllegalAccessException("Unknown ViewModel class")
//    }
//
//}