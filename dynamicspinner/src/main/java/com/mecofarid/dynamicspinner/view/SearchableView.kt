package com.mecofarid.dynamicspinner.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.mecofarid.dynamicspinner.R
import com.mecofarid.dynamicspinner.adapter.DynamicSpinnerAdapter.SpinnerItemClickedListener
import com.mecofarid.dynamicspinner.model.ItemSpinner

class SearchableView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0) : CardView(context, attrs, defStyleAttr) {

    companion object{
        const val TAG = "InternalSearchableViewTag"
    }

    private enum class SelectionMode {
        END,
        START,
        ALL
    }

    override fun getTag(): Any {
        return TAG
    }

    override fun setTag(tag: Any?) {
        if (tag != TAG) throw IllegalAccessException("Please refrain setting tag. Library is already using tag on this element")
        else super.setTag(tag)
    }

    private val mSpinnerItemList: ArrayList<ItemSpinner> = ArrayList()
    private var mSpinnerItemClickedListener: SpinnerItemClickedListener? = null
    private var mSelectedItem: ItemSpinner? = null
    private var mAutoCompleteTextView: AutoCompleteTextView
    private var mOpenSearchViewIcon: ImageView
    private var mCloseSearchViewIcon: ImageView
    private var mInputMethodManager: InputMethodManager? = null
    private var mTextSelectionMode: SelectionMode

    // Whether AutoCompleteTextView is editable or not
    private var mIsSearchOpen = false

    // Initializes basic built-in functionality of SearchableView
    init {
        // Set tag so view can be found with
        tag = TAG

        mInputMethodManager = ContextCompat.getSystemService(context, InputMethodManager::class.java)

        //Initialize views
        val view = View.inflate(context, R.layout.item_searchable, null)
        mAutoCompleteTextView = view.findViewById(R.id.autocomplete_textview)
        mOpenSearchViewIcon = view.findViewById(R.id.search)
        mCloseSearchViewIcon = view.findViewById(R.id.close)
//        val rel = view.findViewById(R.id.searchable_view_content) as RelativeLayout

        //Set values from attributes or default
        with(context.obtainStyledAttributes(attrs, R.styleable.SearchableView, defStyleAttr, 0)){
            setOpenSearchIcon(getResourceId(R.styleable.SearchableView_ds_icon_openSearchView,
                R.drawable.ic_default_open_search))
            setOpenSearchIconColor(getColor(R.styleable.SearchableView_ds_iconColor_openSearchView,
                ContextCompat.getColor(context, R.color.default_color_open_searchview_icon)))

            setCloseSearchIcon(getResourceId(R.styleable.SearchableView_ds_icon_closeSearchView,
                R.drawable.ic_default_close_search))
            setCloseSearchIconColor(getColor(R.styleable.SearchableView_ds_iconColor_closeSearcView,
                ContextCompat.getColor(context, R.color.default_color_close_searchview_icon)))

            /**
             * Show/hide [mOpenSearchViewIcon] depending on whether view is searchable or not. Show if searchable, hide otherwise
              */
            mOpenSearchViewIcon.visibility = if (getBoolean(R.styleable.SearchableView_ds_isSearchable,
                resources.getBoolean(R.bool.default_is_searchable))) View.VISIBLE else View.GONE

            mTextSelectionMode = SelectionMode.values()[getInt(R.styleable.SearchableView_ds_textSelectionMode, 0)]

            cardElevation = resources.getDimension(R.dimen.default_cardview_elevation)
            radius = resources.getDimension(R.dimen.default_cardview_radius)
            useCompatPadding = resources.getBoolean(R.bool.uses_compat_padding)

            recycle()
        }

//        rel.requestFocus()
//
//        rel.setOnFocusChangeListener { view, b ->
//            println("meoc multi self down $b}")
//        }

        mAutoCompleteTextView.showSoftInputOnFocus = false

        //Initializes adapter with empty item list
        initializeAndSetAdapter()

        // Handles all touch events on the view
        handleTouchEvents()

        // Handle all focus changes on the view
        handleFocusChanges()

        addView(view)
    }

    private fun initializeAndSetAdapter(){
        // Items will added later
        val arrayAdapter = ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, mSpinnerItemList)
        mAutoCompleteTextView.setAdapter(arrayAdapter)
    }

    private fun handleTouchEvents(){
        mOpenSearchViewIcon.setOnClickListener { openSearchIfClosed() }
        mCloseSearchViewIcon.setOnClickListener { closeSearchIfOpen() }
        mAutoCompleteTextView.setOnItemClickListener { p0, p1, p2, p3 ->
            mSpinnerItemClickedListener?.onItemClicked(mAutoCompleteTextView.adapter.getItem(p2) as ItemSpinner)
            setSelectedItem(mAutoCompleteTextView.adapter.getItem(p2) as ItemSpinner)
        }
        mAutoCompleteTextView.setOnClickListener {
            if (isSearchOpen()){
                requestFocusOnAutocompleteTextView(true, true)
                showKeyboard(true)
            }else {
                // Requesting focus so that any other view with focus can loose their focus. If focus not requested on current item
                // and there is another view in focused state (focused/editable AutoCompleteTextView), then RecyclerView will crash
                // because of focus
                requestFocusOnAutocompleteTextView(true, false)

                showDropDown()
            }
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

    // Will show all selection list of AutoCompleteTextView
    private fun showDropDown() {
        mAutoCompleteTextView.showDropDown()
    }

    // Opens SearchableView and makes search by typing possible
    private fun openSearchIfClosed(){
        if (isSearchOpen().not()){
            requestFocusOnAutocompleteTextView(true, true)
            setTextSelectionMode(mTextSelectionMode)

            showStartSearchIcon(false)
            showCloseSearchIcon(true)
        }
        showKeyboard(true)
        mIsSearchOpen = true
    }

    // Closes SearchableView and makes search by typing impossible
    private fun closeSearchIfOpen(){
        if (isSearchOpen()){
            requestFocusOnAutocompleteTextView(false, false)

            showStartSearchIcon(true)
            showCloseSearchIcon(false)
        }
        showKeyboard(false)
        mIsSearchOpen = false
    }

    /**
     * Requests focus on AutoCompleteTextView so that search by typing is possible
     * @param requestFocus - Request focus on View if [true]
     * @param showCursor - show cursor if true
     */
    private fun requestFocusOnAutocompleteTextView(requestFocus: Boolean, showCursor: Boolean) {

        mAutoCompleteTextView.apply {
            isCursorVisible = showCursor
            isFocusableInTouchMode = requestFocus
            isFocusable = requestFocus

            if (requestFocus){
                requestFocus()
            }
        }
    }

    /**
     * Sets selection of text when view focused (search is open)
     */
    private fun setTextSelectionMode(selectionMode: SelectionMode){
        when(selectionMode){
            SelectionMode.START -> mAutoCompleteTextView.setSelection(0)
            SelectionMode.END -> mAutoCompleteTextView.setSelection(mAutoCompleteTextView.text.toString().length)
            SelectionMode.ALL -> mAutoCompleteTextView.selectAll()
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
    internal fun setOnSpinnerClickedListener(spinnerItemClickedListener: SpinnerItemClickedListener) {
        mSpinnerItemClickedListener = spinnerItemClickedListener
    }

    /**
     * Sets text to AutoCompleteTextView
     *
     * @param text -Text to be set
     * @param showDropdown - Whether to show drop down selection or not
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

        // This is necessary to make AutoCompleteTextView uneditable when item selected from AutoCompleteTextView's options list
        closeSearchIfOpen()
    }

    // Returns selected item
    internal fun getSelectedItem(): ItemSpinner? {
        return mSelectedItem
    }

    // Returns weather SearchableView is open or not. Namely, search by typing is possible or not
    private fun isSearchOpen():Boolean{
        return mIsSearchOpen
    }
}
