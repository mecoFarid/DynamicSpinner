package com.mecofarid.searchablemultispinner.view

import android.content.Context
import android.util.AttributeSet
import androidx.cardview.widget.CardView

class SpinnerView: CardView {

    constructor(context: Context) : super(context){
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

    private fun init(context: Context){
        val view = SearchableView(context)


        setOnClickListener {
            println("Josiah clicks ")
        }

        this.addView(view)
    }
}