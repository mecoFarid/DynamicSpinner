package com.mecofarid.searchablemultispinner.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.content.ContextCompat
import com.mecofarid.searchablemultispinner.R
import com.mecofarid.searchablemultispinner.interfaces.ItemSelectedListener
import com.mecofarid.searchablemultispinner.model.ItemSpinner

class SearchableView : RelativeLayout {
    private lateinit var mItemSelectedListener: ItemSelectedListener
    private lateinit var mSelectedItem: ItemSpinner
    private lateinit var mAutoCompleteTextView: AutoCompleteTextView
    private lateinit var mOpenSearch_view: ImageView
    private lateinit var mCloseSearch_view: ImageView
    private lateinit var mContext: Context
    private var mInpuMethodManager: InputMethodManager? = null

    // Whether AutoCompleteTextView is editable or not
    private var mIsSearchOpen = false
    
    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr) {
        init(context)
    }

    private fun init(context: Context) {
        mContext = context
        mInpuMethodManager =
            ContextCompat.getSystemService(mContext, InputMethodManager::class.java)

        val view = View.inflate(context,
            R.layout.item_searchable, null)
        mAutoCompleteTextView = view.findViewById(R.id.autocomplete_textview)
        mOpenSearch_view = view.findViewById(R.id.search)
        mCloseSearch_view = view.findViewById(R.id.close)

        // As soon as SearchView is opened make AutoCompleteTextView match its parent
        mOpenSearch_view.setOnClickListener { openSearch() }
        mCloseSearch_view.setOnClickListener { closeSearch() }

        // If focus changes that means other view gained focus then close the current spinner
        mAutoCompleteTextView.onFocusChangeListener =
            OnFocusChangeListener { _, focused ->
                if (!focused) {
                    closeSearch()
                    // When search stopped halfway through, show the last selected item's 'toString()' in AutoCompleteTextView
//                    setAutoCompleteText(mSelectedItem.toString(), false)
                }
            }

        addView(view)
    }

    fun showDropDown() {
        mAutoCompleteTextView.showDropDown()
    }

    private fun openSearch(){
        mIsSearchOpen = true

        requestFocusOnAutocompleteTextView(true)
        showKeyboard(true)

        showStartSearchIcon(false)
        showCloseSearchIcon(true)
    }

    private fun closeSearch(){
        mIsSearchOpen = false

        requestFocusOnAutocompleteTextView(false)
        showKeyboard(false)

        showStartSearchIcon(true)
        showCloseSearchIcon(false)
    }

    private fun requestFocusOnAutocompleteTextView(doRequest: Boolean) {

        mAutoCompleteTextView.isCursorVisible = doRequest
        mAutoCompleteTextView.isFocusableInTouchMode = doRequest
        mAutoCompleteTextView.isFocusable = doRequest


        if (doRequest) {
            mAutoCompleteTextView.requestFocus()
            mAutoCompleteTextView.setSelection(mAutoCompleteTextView.text.toString().length)
        }
    }

    private fun showStartSearchIcon(show: Boolean) {
//        val params = mAutoCompleteTextView.layoutParams as LayoutParams
//        if (show) {
//            params.removeRule(ALIGN_PARENT_START)
//        } else {
//            params.addRule(ALIGN_PARENT_START)
//        }
//        mOpenSearch_view.visibility = if (show) View.VISIBLE else View.GONE
//        mAutoCompleteTextView.layoutParams = params //causes layout update

        mOpenSearch_view.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun showCloseSearchIcon(show: Boolean) {
        mCloseSearch_view.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun showKeyboard(show: Boolean){
        if (show) {
            mInpuMethodManager?.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        } else {
            mInpuMethodManager?.hideSoftInputFromWindow(mAutoCompleteTextView.windowToken, 0)
        }
    }

    /**
     * @param adapter The adapter to be passed to AutoCompleteTextView
     */
    fun setAutoCompleteAdapter(adapter: ArrayAdapter<in ItemSpinner>) {
        mAutoCompleteTextView.setAdapter(adapter)
    }

    /**
     * @param itemClickListener The listener to be passed to AutoCompleteTextView to detect clicks on
     * AutoCompleteTextView's selection list
     */
    fun setOnSpinnerItemClickListener(itemClickListener: AdapterView.OnItemClickListener) {
        mAutoCompleteTextView.onItemClickListener = itemClickListener
    }

    /**
     * @param clickListener The listener to be passed to AutoCompleteTextView to detect clicks on
     * AutoCompleteTextView itself
     */
    fun setOnSpinnerClickListener(clickListener: OnClickListener) {
        mAutoCompleteTextView.setOnClickListener(clickListener)
    }

    fun setAutoCompleteText(text: String, showDropdown: Boolean) {
        mAutoCompleteTextView.setText(text, showDropdown)
    }

    /**
     * @param mItemSelectedListener The listener to pass selected item to classes that implement
     * ItemSelectedListener and requires to be notified when an item is selected
     */
    fun setItemSelectedListener(mItemSelectedListener: ItemSelectedListener) {
        this.mItemSelectedListener = mItemSelectedListener
    }

    fun setSelectedItem(selectedItem: ItemSpinner) {
        mSelectedItem = selectedItem
        mItemSelectedListener.onItemSelected(mSelectedItem)
        // This is necessary to make AutoCompleteTextView uneditable when item selected from AutoCompleteTextView's options list
        if (mIsSearchOpen) {
            closeSearch()
        }
    }
}
