package com.ssacproject.fourthweek


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ssacproject.fourthweek.databinding.ItemBoardBinding
import com.ssacproject.fourthweek.room.RoomBoard
import com.ssacproject.fourthweek.view.LoadingActivity

class BoardListAdapter : ListAdapter<RoomBoard, BoardListAdapter.Holder>(BoardItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemBoardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setBoard(currentList[position], position+1)
    }

    inner class Holder(val binding: ItemBoardBinding) : RecyclerView.ViewHolder(binding.root) {
        var mBoard: RoomBoard? = null

        fun setBoard(item: RoomBoard, position: Int) {
            val image = when (item.character) {
                LoadingActivity.CHARACTER_BIRD -> {
                    R.drawable.fly1
                }
                LoadingActivity.CHARACTER_PIG -> {
                    R.drawable.pig1
                }
                LoadingActivity.CHARACTER_GHOST -> {
                    R.drawable.ghost1
                }
                else -> {
                    R.drawable.rabbit_normal
                }
            }
            binding.boardImage.setImageResource(image)
            binding.boardName.text= item.name
            binding.boardScore.text = "${item.score}점"
            binding.boardNo.text = "${position}."
            mBoard = item

        }
    }
}