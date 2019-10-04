package com.mecofarid.searchablemultispinner.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatAutoCompleteTextView

class AnapticAutoCompleteTextView: AppCompatAutoCompleteTextView {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        //This line ensures that Touch events on this view will be dispatched to parent ViewGroup. Namely, Touch event
        // listeners on this view won't get callback
        println("meoc ")
        return false
    }
}