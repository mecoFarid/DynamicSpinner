package com.mecofarid.searchablemultispinner.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.content.ContextCompat
import com.mecofarid.searchablemultispinner.R
import com.mecofarid.searchablemultispinner.adapter.SearchableMultiSpinnerAdapter.SpinnerItemSelectedListener
//import com.mecofarid.searchablemultispinner.adapter.SearchableMultiSpinnerAdapter.SpinnerItemClickedListener
import com.mecofarid.searchablemultispinner.model.ItemSpinner


internal class SearchableView : RelativeLayout{

//    private var mSpinnerItemClickedListener: SpinnerItemClickedListener? = null
    private var mSpinnerItemSelectedListener: SpinnerItemSelectedListener? = null
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

    // Initializes basic built-in functionality of SearchableView
    private fun init(context: Context) {
        mContext = context
        mInpuMethodManager = ContextCompat.getSystemService(mContext, InputMethodManager::class.java)

        val view = View.inflate(context, R.layout.item_searchable, null)
        mAutoCompleteTextView = view.findViewById(R.id.autocomplete_textview)
        mOpenSearch_view = view.findViewById(R.id.search)
        mCloseSearch_view = view.findViewById(R.id.close)

        // As soon as SearchView is opened make AutoCompleteTextView match its parent
        mOpenSearch_view.setOnClickListener { openSearchIfClosed() }
        mCloseSearch_view.setOnClickListener { closeSearchIfOpen() }
        mAutoCompleteTextView.setOnItemClickListener { p0, p1, p2, p3 ->
            setSelectedItem(mAutoCompleteTextView.adapter.getItem(p2) as ItemSpinner)
        }

        // If focus changes that means other view gained focus then close the current spinner
        mAutoCompleteTextView.onFocusChangeListener =
            OnFocusChangeListener { _, focused ->
                if (!focused) {
                    closeSearchIfOpen()
                    // When search stopped halfway through, show the last selected item's 'toString()' in AutoCompleteTextView
//                    setAutoCompleteText(mSelectedItem.toString(), false)
                }
            }

        addView(view)
    }

    // Will show all selection list of AutoCompleteTextView if search by is closed, namely, typing is not possible
    fun showDropDownIfSearchNotOpen() {
        if (isSearchOpen().not()) mAutoCompleteTextView.showDropDown()
    }

    // Opens SearchableView and makes search by typing possible
    private fun openSearchIfClosed(){
        if (isSearchOpen().not()){
            requestFocusOnAutocompleteTextView(true)
            showKeyboard(true)

            showStartSearch_icon(false)
            showCloseSearch_icon(true)
        }
        mIsSearchOpen = true
    }

    // Closes SearchableView and makes search by typing impossible
    private fun closeSearchIfOpen(){
        if (isSearchOpen()){
            requestFocusOnAutocompleteTextView(false)
            showKeyboard(false)

            showStartSearch_icon(true)
            showCloseSearch_icon(false)
        }
        mIsSearchOpen = false
    }

    /**
     * Requests focus on AutoCompleteTextView so that search by typing is possible
     * @param requestFocus - Request focus on View if [true]
     */
    private fun requestFocusOnAutocompleteTextView(requestFocus: Boolean) {

        mAutoCompleteTextView.isCursorVisible = requestFocus
        mAutoCompleteTextView.isFocusableInTouchMode = requestFocus
        mAutoCompleteTextView.isFocusable = requestFocus


        if (requestFocus) {
            mAutoCompleteTextView.requestFocus()
            mAutoCompleteTextView.setSelection(mAutoCompleteTextView.text.toString().length)
        }
    }

    /**
     * Shows open_search icon in the start of the View
     * @param show - Shows icons if [true] hides if [false]
     */
    private fun showStartSearch_icon(show: Boolean) {
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

    /**
     * Shows close_search icon in the end of the View
     * @param show - Shows icons if [true] hides if [false]
     */
    private fun showCloseSearch_icon(show: Boolean) {
        mCloseSearch_view.visibility = if (show) View.VISIBLE else View.GONE
    }

    /**
     * Toggles soft keyboard
     * @param show - Shows keyboard is [true] hides if [false]
     */
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
     * @param itemClickListener The listener to be passed to SearchableView to detect automatic selections
     */
    fun setOnSpinnerItemSelectedListener(spinnerItemSelectedListener: SpinnerItemSelectedListener) {
        mSpinnerItemSelectedListener = spinnerItemSelectedListener
    }

    fun setAutoCompleteText(text: String, showDropdown: Boolean) {
        mAutoCompleteTextView.setText(text, showDropdown)
    }

    /**
     * @param selectedItem - Currently selected item
     */
    fun setSelectedItem(selectedItem: ItemSpinner) {
        mSelectedItem = selectedItem
        mSpinnerItemSelectedListener?.onItemSelected(mSelectedItem)

        // This is necessary to make AutoCompleteTextView uneditable when item selected from AutoCompleteTextView's options list
        closeSearchIfOpen()
    }

    // Returns weather SearchableView is open or not. Namely, search by typing is possible or not
    private fun isSearchOpen():Boolean{
        return mIsSearchOpen
    }
}
