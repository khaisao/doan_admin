package com.khaipv.attendance.util

import android.content.Context
import android.util.AttributeSet
import android.widget.HorizontalScrollView

class MyHorizontalScrollView  constructor(context: Context
) : HorizontalScrollView(context) {

    private var listener: OnScrollListener? = null

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        listener?.onScroll(this, l, t)
    }

    fun setOnScrollListener(listener: OnScrollListener) {
        this.listener = listener
    }

    interface OnScrollListener {
        fun onScroll(view: HorizontalScrollView, x: Int, y: Int)
    }
}