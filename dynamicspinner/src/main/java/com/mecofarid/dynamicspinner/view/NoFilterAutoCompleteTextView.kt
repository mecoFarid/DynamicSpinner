package com.mecofarid.dynamicspinner.view

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.MotionEvent
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import com.mecofarid.dynamicspinner.model.ItemSpinner

class NoFilterAutoCompleteTextView: AppCompatAutoCompleteTextView {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun performClick(): Boolean {
        /**
         * Should set filter to "" (empty), otherwise [showDropDown] will display only filtered results.
         * Filter constraint (filter text), in this case, is text from previous typing. Lets say we have ["aar","aab","abb"]
         * we type "aa" and select "aar" next time [showDropDown] will only show "aar" and "aab" because filter constraint
         * is "aa". To prevent this filter has to be set to "" (empty)
         */
        filter.filter("")

        /**
         * Every time filter changes to "", FilterListener is triggered in [AppCompatAutoCompleteTextView] and updates dropdown. Considering
         * that constraint text (filtering text) is "" (empty), also [enoughToFilter] returns true because [getText] is greater
         * than [getThreshold] (because on click we don't clear visible text in [AppCompatAutoCompleteTextView]). As a result, [showDropDown]
         * shows all selection in dropdown. So basically, dropdown should be dismissed here
         * in favor of [SearchableView.showDropDownIfSearchNotOpen]
          */
        dismissDropDown()

        return super.performClick()
    }
}