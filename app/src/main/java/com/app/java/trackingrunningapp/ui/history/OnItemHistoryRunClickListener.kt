package com.app.java.trackingrunningapp.ui.history

import com.app.java.trackingrunningapp.data.model.dataclass.history.Run
import com.app.java.trackingrunningapp.data.model.entity.RunSession

interface OnItemHistoryRunClickListener {
    fun onItemClick(itemRun: Run)
    fun onAddFavouriteClick(action:Int)
}