package com.ssacproject.fourthweek

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssacproject.fourthweek.databinding.FragmentModeBinding

class FragmentMode : Fragment() {
    private var binding: FragmentModeBinding? = null
    private lateinit var boardViewModel : RoomBoardViewModel
    lateinit var boardListAdapter : BoardListAdapter
    var whatMode = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            whatMode = arguments?.getString("mode", "")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentModeBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        boardListAdapter = BoardListAdapter()
        boardViewModel = ViewModelProvider(this, ViewModelFactory(requireActivity().application, whatMode)).get(RoomBoardViewModel::class.java)
        boardViewModel.getModeList().observe(viewLifecycleOwner, Observer {
            //
            val tmp = it.sortedByDescending { it.score }
            boardListAdapter.submitList(tmp)
        })
        binding!!.easyRecyclerView.apply{
            layoutManager= LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter= boardListAdapter
        }
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                boardViewModel.delete(boardListAdapter.currentList[viewHolder.adapterPosition])
                // 삭제하고 바로 sort 하면 정렬되지 않을까.

            }
        }).attachToRecyclerView(binding!!.easyRecyclerView)



    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}

class ViewModelFactory(
    val application: Application,
    val mode: String) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RoomBoardViewModel(application, mode) as T
    }
}


//class FragmentMode : Fragment() {
//
//    private var binding: FragmentModeBinding? = null
//    lateinit var boardListAdapter: BoardListAdapter
//    lateinit var viewModel : RoomBoardViewModel
//    var whatMode = ""
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            whatMode = arguments?.getString("mode", "")!!
//        }
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding = FragmentModeBinding.inflate(inflater, container, false)
//        return binding!!.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        boardListAdapter = BoardListAdapter()
////
////        viewModel = ViewModelProvider(this).get(RoomBoardViewModel::class.java)
////        viewModel.setList(whatMode)
////
////        viewModel.list.observe(viewLifecycleOwner, Observer{
////            boardListAdapter.submitList(it)
////        })
////        val newList = boardHelper?.roomBoardDao?.getModeList(whatMode)?: listOf()
////        val tmpList = newList.sortedByDescending { it.score }
////        boardListAdapter.submitList(tmpList)
//
//        CoroutineScope(Main).launch {
//            val newList = boardHelper?.roomBoardDao?.getModeListSuspend(whatMode)?: listOf()
//            val tmpList = newList.sortedByDescending { it.score }
//            boardListAdapter.submitList(tmpList)
//        }
//
//        binding!!.easyRecyclerView.apply{
//            layoutManager= LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//            adapter= boardListAdapter
//        }
//
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        binding = null
//    }
//}