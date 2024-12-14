package com.app.java.trackingrunningapp.ui.history

import com.app.java.trackingrunningapp.data.model.history.Run

interface OnItemHistoryRunClickListener {
    fun onClick(itemRun: Run)
}