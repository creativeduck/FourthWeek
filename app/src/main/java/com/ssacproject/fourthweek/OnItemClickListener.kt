package com.ssacproject.fourthweek

import android.view.View

interface OnItemClickListener {
    fun onItemClicked(view: View, pos: Int)
}

interface OnItemLongClickListener {
    fun onItemLongClicked(view: View, pos: Int)
}

interface OnMinusClickListener {
    fun onMinusClick(view: View, pos: Int)
}

interface onPlusClickListener {
    fun onPlusClick(view: View, pos: Int)
}

interface onDeleteClickListener {
    fun onDeleteClick(view: View, pos: Int)
}