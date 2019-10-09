package com.mecofarid.searchablemultispinner.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.mecofarid.searchablemultispinner.R
import com.mecofarid.searchablemultispinner.adapter.SearchableMultiSpinnerAdapter.SpinnerItemSelectedListener
import com.mecofarid.searchablemultispinner.model.ItemSpinner


class SearchableView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0) : CardView(context, attrs, defStyleAttr) {

    enum class Selection {
        START,
        END,
        ALL
    }

    private val mSpinnerItemList: ArrayList<ItemSpinner> = ArrayList()
    private var mSpinnerItemSelectedListener: SpinnerItemSelectedListener? = null
    private lateinit var mSelectedItem: ItemSpinner
    private var mAutoCompleteTextView: AutoCompleteTextView
    private var mOpenSearchViewIcon: ImageView
    private var mCloseSearchViewIcon: ImageView
    private var mInputMethodManager: InputMethodManager? = null

    // Defines if the spinner is both (searchable and selectable) or only selectable
    private var isSearchable = true

    // Whether AutoCompleteTextView is editable or not
    private var mIsSearchOpen = false

    // Initializes basic built-in functionality of SearchableView
    init {
        mInputMethodManager = ContextCompat.getSystemService(context, InputMethodManager::class.java)

        //Initialize views
        val view = View.inflate(context, R.layout.item_searchable, null)
        mAutoCompleteTextView = view.findViewById(R.id.autocomplete_textview)
        mOpenSearchViewIcon = view.findViewById(R.id.search)
        mCloseSearchViewIcon = view.findViewById(R.id.close)

        //Set values from attributes or default
        with(context.obtainStyledAttributes(attrs, R.styleable.SearchableView, defStyleAttr, 0)){
            setOpenSearchIcon(getResourceId(R.styleable.SearchableView_ss_icon_openSearchView,
                R.drawable.ic_default_open_search))
            setOpenSearchIconColor(getColor(R.styleable.SearchableView_ss_iconColor_openSearchView,
                ContextCompat.getColor(context, R.color.default_color_open_searchview_icon)))

            setCloseSearchIcon(getResourceId(R.styleable.SearchableView_ss_icon_closeSearchView,
                R.drawable.ic_default_close_search))
            setCloseSearchIconColor(getColor(R.styleable.SearchableView_ss_iconColor_closeSearcView,
                ContextCompat.getColor(context, R.color.default_color_close_searchview_icon)))


            cardElevation = resources.getDimension(R.dimen.default_cardview_elevation)
            radius = resources.getDimension(R.dimen.default_cardview_radius)
            useCompatPadding = resources.getBoolean(R.bool.uses_compat_padding)

            recycle()
        }


        //Initializes adapter with empty item list
        initializeAndSetAdpater()

        // Handles all touch events on the view
        handleTouchEvents()

        // Handle all focus changes on the view
        handleFocusChanges()

        addView(view)
    }

    private fun initializeAndSetAdpater(){
        // Items will added later
        val arrayAdapter = ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, mSpinnerItemList)
        mAutoCompleteTextView.setAdapter(arrayAdapter)
    }

    private fun handleTouchEvents(){
        mOpenSearchViewIcon.setOnClickListener { openSearchIfClosed() }
        mCloseSearchViewIcon.setOnClickListener { closeSearchIfOpen() }
        mAutoCompleteTextView.setOnItemClickListener { p0, p1, p2, p3 ->
            setSelectedItem(mAutoCompleteTextView.adapter.getItem(p2) as ItemSpinner)
        }
        mAutoCompleteTextView.setOnClickListener {
            showDropDownIfSearchNotOpen()
        }
    }

    private fun handleFocusChanges(){
        // If focus changes that means other view gained focus then close the current spinner
        mAutoCompleteTextView.onFocusChangeListener =
            OnFocusChangeListener { _, focused ->
                if (!focused) {
                    closeSearchIfOpen()
                    // When search stopped halfway through, show the last selected item's 'toString()' in AutoCompleteTextView
                    setAutoCompleteText(mSelectedItem.toString())
                }
            }
    }

    /**
     * Defines if the spinner is both (searchable and selectable) or only selectable
     * @param searchable - Searchable if true, not searchable otherwise
     */
    fun setSearchable(searchable: Boolean){
        isSearchable = searchable
    }

    /**
     * Set OpenSearch_icon color
     * @param color - Resource ID
     */
    fun setOpenSearchIconColor(color: Int){
        mOpenSearchViewIcon.apply {
            setColorFilter(color)
        }
    }

    /**
     * Set CloseSearch_icon color
     * @param color - color
     */
    fun setCloseSearchIconColor(color: Int){
        mCloseSearchViewIcon.apply {
            setColorFilter(color)
        }
    }

    /**
     * Set OpenSearch_icon
     * @param resId - Resource ID
     */
    fun setOpenSearchIcon(resId: Int){
        mOpenSearchViewIcon.setImageResource(resId)
    }

    /**
     * Set CloseSearch_icon
     * @param resId - Resource ID
     */
    fun setCloseSearchIcon(resId: Int){
        mCloseSearchViewIcon.setImageResource(resId)
    }

    // Will show all selection list of AutoCompleteTextView if search by is closed, namely, typing is not possible
    private fun showDropDownIfSearchNotOpen() {
        if (isSearchOpen().not()) mAutoCompleteTextView.showDropDown()
    }

    // Opens SearchableView and makes search by typing possible
    private fun openSearchIfClosed(){
        if (isSearchOpen().not()){
            requestFocusOnAutocompleteTextView(true)
            showKeyboard(true)

            showStartSearchIcon(false)
            showCloseSearchIcon(true)
        }
        mIsSearchOpen = true
    }

    // Closes SearchableView and makes search by typing impossible
    private fun closeSearchIfOpen(){
        if (isSearchOpen()){
            requestFocusOnAutocompleteTextView(false)
            showKeyboard(false)

            showStartSearchIcon(true)
            showCloseSearchIcon(false)
        }
        mIsSearchOpen = false
    }

    /**
     * Requests focus on AutoCompleteTextView so that search by typing is possible
     * @param requestFocus - Request focus on View if [true]
     */
    private fun requestFocusOnAutocompleteTextView(requestFocus: Boolean) {

        mAutoCompleteTextView.apply {
            isCursorVisible = requestFocus
            isFocusableInTouchMode = requestFocus
            isFocusable = requestFocus

            if (requestFocus){
                requestFocus()
                setSelection(Selection.END)
            }
        }
    }

    /**
     * Sets selection of text when view focused (search is open)
     */
    private fun setSelection(selection: Selection){
        when(selection){
            Selection.START -> mAutoCompleteTextView.setSelection(0)
            Selection.END -> mAutoCompleteTextView.setSelection(mAutoCompleteTextView.text.toString().length)
            Selection.ALL -> mAutoCompleteTextView.selectAll()
        }
    }

    /**
     * Shows open_search icon in the start of the View
     * @param show - Shows icons if [true] hides if [false]
     */
    private fun showStartSearchIcon(show: Boolean) {
        mOpenSearchViewIcon.visibility = if (show) View.VISIBLE else View.GONE
    }

    /**
     * Shows close_search icon in the end of the View
     * @param show - Shows icons if [true] hides if [false]
     */
    private fun showCloseSearchIcon(show: Boolean) {
        mCloseSearchViewIcon.visibility = if (show) View.VISIBLE else View.GONE
    }

    /**
     * Toggles soft keyboard
     * @param show - Shows keyboard is [true] hides if [false]
     */
    private fun showKeyboard(show: Boolean){
        if (show) {
            mInputMethodManager?.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        } else {
            mInputMethodManager?.hideSoftInputFromWindow(mAutoCompleteTextView.windowToken, 0)
        }
    }

    /**
     * @param itemList- ItemSpinner list to be passed to AutoCompleteTextView
     */
    internal fun updateItemList(itemList: List<ItemSpinner>) {
        // To prevent item list build up, clear and then add all items
        mSpinnerItemList.clear()
        mSpinnerItemList.addAll(itemList)
    }

    /**
     * @param itemClickListener The listener to be passed to SearchableView to detect automatic selections
     */
    internal fun setOnSpinnerItemSelectedListener(spinnerItemSelectedListener: SpinnerItemSelectedListener) {
        mSpinnerItemSelectedListener = spinnerItemSelectedListener
    }

    /**
     * Sets text to AutoCompleteTextView
     *
     * @param text -Text to be set
     * @param showDropdown - Wheather to show drop down selection or not
     */
    private fun setAutoCompleteText(text: String) {
        mAutoCompleteTextView.setText(text, false)
    }

    /**
     * @param selectedItem - Currently selected item
     */
    internal fun setSelectedItem(selectedItem: ItemSpinner) {
        // Visually set selected item on each selection. Otherwise RecyclerView will recycle view from upper items
        setAutoCompleteText(selectedItem.toString())

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
