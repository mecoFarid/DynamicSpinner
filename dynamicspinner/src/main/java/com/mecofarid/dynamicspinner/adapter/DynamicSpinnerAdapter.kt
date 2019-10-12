package com.mecofarid.dynamicspinner.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.mecofarid.dynamicspinner.R
import com.mecofarid.dynamicspinner.model.ItemSpinner
import com.mecofarid.dynamicspinner.model.ItemDynamicSpinner
import com.mecofarid.dynamicspinner.util.ListParserUtils
import com.mecofarid.dynamicspinner.view.SearchableView

/**
 * @param nestedList - This is nested list
 * @param mOuterSpinnerItemSelectedListener - The listener from outer class through which that class will be notified when
 * item selected
 */
class DynamicSpinnerAdapter (
    nestedList: List<ItemSpinner>,
    private val mOuterSpinnerItemSelectedListener: SpinnerItemSelectedListener,
    private val layoutResId: Int
    ): RecyclerView.Adapter<DynamicSpinnerAdapter.ViewHolder>() {
    val mHierarchicList = ListParserUtils.parseToHierarchicFlatList(nestedList, -1, 0)

    val mMultiSpinnerItemList = List(mHierarchicList.size){ItemDynamicSpinner()}

    // As soon as initialized send latest item in hierarchy
    init {
        mOuterSpinnerItemSelectedListener.onItemSelected(getLatestItemInHierarchy(0, ItemSpinner()))
    }

    var mRecyclerView: RecyclerView?= null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        mRecyclerView = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mHierarchicList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        val position = holder.adapterPosition
        getSubCategoryAtWithParentId(position,
            mMultiSpinnerItemList.getOrElse(position - 1) { ItemDynamicSpinner() }.selectedItemSpinner?.itemSpinnerId
                ?: -1
        )
            ?.let {
                holder.bind(it.filterNotNull())
            }
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val searchableView = itemView.findViewWithTag<SearchableView>(SearchableView.TAG)
        init {
            // Detect Physical SearchableView item selection
            searchableView.setOnSpinnerClickedListener(object : SpinnerItemClickedListener{
                override fun onItemClicked(itemSpinner: ItemSpinner) {
                    val position = adapterPosition

                    mMultiSpinnerItemList[position].selectedItemSpinner = mHierarchicList[position].first { itemSpinner.itemSpinnerId == it.itemSpinnerId }
                    resetSelectedItemsBelow(position)
                    notifyItemsBelow(position)
                    mOuterSpinnerItemSelectedListener.onItemSelected(getLatestItemInHierarchy(position+1, itemSpinner))
                }
            })
        }

        fun bind(itemList: List<ItemSpinner>) {
            if (itemList.isNotEmpty()) {
                val position = adapterPosition

                expandViewIfCollapsed(searchableView)

                searchableView.updateItemList(itemList)
                val selectedItemSpinner = mMultiSpinnerItemList[position].selectedItemSpinner ?: itemList[0]
                searchableView.setSelectedItem(selectedItemSpinner)
                mMultiSpinnerItemList[position].selectedItemSpinner = selectedItemSpinner

            } else {
                collapseViewIfExpanded(searchableView)
            }
        }
    }

    /**
     * Returns subcategory for selected [ItemSpinner] of [SearchableView]
     *
     * @param position - Position of RecyclerView item
     * @param parentId - Id of parent category
     */
    private fun getSubCategoryAtWithParentId(position: Int, parentId: Long): List<ItemSpinner?>? {
        return mHierarchicList[position].filter {
            it.itemSpinnerParentId == parentId
        }
    }

    /**
     * Return latest item in hierarchy for given parent
     * @param level - Hierarchy level
     * @param parentItem - Parent item whose hierarchy is being searched
     */
    private fun getLatestItemInHierarchy(level: Int, parentItem: ItemSpinner): ItemSpinner{
        val firstChild =
            kotlin.runCatching {  mHierarchicList[level].firstOrNull { it.itemSpinnerParentId == parentItem.itemSpinnerId }}.getOrNull()
        firstChild?.let {
            return getLatestItemInHierarchy(level + 1, firstChild)
        }
        return parentItem
    }

    /**
     * Will update all RecyclerView items below item at [currentPosition]
     * @param currentPosition - Position of current RecyclerView item from which other views' update is requested
     */
    private fun notifyItemsBelow(currentPosition: Int) {
        for (position in currentPosition + 1 until itemCount) {
            notifyItemChanged(position)
        }
        mRecyclerView?.let{
            it.post {
                // Re-layout to expand RecyclerView to wrap its current content, otherwise layout params won't change and will keep the same
                // height from previous selection. For example, if there were 3 categories (3 items in RecyclerView) in previous hierarchy
                // and 10 categories (10 items in RecyclerView) in current hierarchy then height of RecyclerView will keep the same height
                // when there were 3 categories
                it.requestLayout()
            }
        }
    }

    /**
     * Will reset all parent ids below [currentPosition]
     * @param currentPosition - Current position under which all parent IDs will be reset to default
     */
    private fun resetSelectedItemsBelow(currentPosition: Int){
        for (position in currentPosition + 1 until itemCount) {
            mMultiSpinnerItemList[position].selectedItemSpinner = null
        }
    }

    /**
     * Will expand view. Height will be set to `LayoutParams.WRAP_CONTENT`
     *
     * @param view - View to be expanded
     */
    private fun expandViewIfCollapsed(searchableView: SearchableView) {
        with(searchableView.findViewById<RelativeLayout>(R.id.searchable_view_content)) {
            if (searchableView.useCompatPadding.not() && this.isVisible.not()){
                searchableView.useCompatPadding = true
                this.visibility = View.VISIBLE
            }
        }
    }

    /**
     * Will collapse view. Height will be set to `0`
     *
     * @param view - View to be collapsed/expanded
     * @param collpase - Decide whether view wil be collpased or expanded
     */
    private fun collapseViewIfExpanded(searchableView: SearchableView) {
        with(searchableView.findViewById<RelativeLayout>(R.id.searchable_view_content)) {
            if (searchableView.useCompatPadding && this.isVisible){
                searchableView.useCompatPadding = false
                this.visibility = View.GONE
            }
        }
    }

    /**
     *
     *Check if view is collapsed. Height `0` means collapsed
     * @param view - View to check height
     */
    private fun isCollapsed(view: View): Boolean {
        return view.layoutParams.height == 0
    }

    // Listener to notify classes (that implement this interface) when item selected
    interface SpinnerItemSelectedListener {
        fun onItemSelected(itemSpinner: ItemSpinner)
    }

    // Listener to notify classes (that implement this interface) when item physically clicked
    internal interface SpinnerItemClickedListener {
        fun onItemClicked(itemSpinner: ItemSpinner)
    }
}