package com.app.java.trackingrunningapp.ui.history

import com.app.java.trackingrunningapp.data.model.entity.RunSession

interface OnItemHistoryRunClickListener {
    fun onItemClick(itemRun: RunSession)
    fun onAddFavouriteClick(action:Int)
}