package com.ssacproject.fourthweek

import androidx.recyclerview.widget.DiffUtil
import com.ssacproject.fourthweek.room.RoomBoard

class BoardItemCallback : DiffUtil.ItemCallback<RoomBoard>() {
    override fun areItemsTheSame(oldItem: RoomBoard, newItem: RoomBoard): Boolean {
        return oldItem.no== newItem.no
    }

    override fun areContentsTheSame(oldItem: RoomBoard, newItem: RoomBoard): Boolean {
        return oldItem == newItem
    }
}