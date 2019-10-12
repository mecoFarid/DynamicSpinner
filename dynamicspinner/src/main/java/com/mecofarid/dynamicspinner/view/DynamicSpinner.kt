package com.mecofarid.dynamicspinner.view

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DynamicSpinner: RecyclerView {
    constructor(context: Context) : super(context) {
        init(context)
    }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs){
        init(context)
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        init(context)
    }

    private fun init(context: Context) {
        layoutManager = LinearLayoutManager(context)
    }
}