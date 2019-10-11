package com.mecofarid.searchablemultispinner.adapter

import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mecofarid.searchablemultispinner.model.ItemSpinner
import com.mecofarid.searchablemultispinner.model.ItemMultiSpinner
import com.mecofarid.searchablemultispinner.util.ListParserUtils
import com.mecofarid.searchablemultispinner.view.SearchableView

const val ABSENT_PARENT_ID = -5L
/**
 * @param nestedList - This is nested list
 * @param mOuterSpinnerItemSelectedListener - The listener from outer class through which that class will be notified when
 * item selected
 */
class SearchableMultiSpinnerAdapter (
    nestedList: List<ItemSpinner>,
    private val mOuterSpinnerItemSelectedListener: SpinnerItemSelectedListener,
    private val layoutResId: Int
    ): RecyclerView.Adapter<SearchableMultiSpinnerAdapter.ViewHolder>() {

    val mHierarchicList = ListParserUtils.parseToHierarchicFlatList(nestedList, -1, 0)

    lateinit var mRecyclerView: RecyclerView

    // Map to pair Selected item's ID with position
//    val mSelectedItemIdPositionMap = SparseArray<Long>()

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecyclerView = recyclerView
    }

    val mTestModelList = List(mHierarchicList.size){ItemMultiSpinner()}

    // As soon as initialized send latest item in hierarchy
    init {
        mOuterSpinnerItemSelectedListener.onItemSelected(getLatestItemInHierarchy(0, ItemSpinner()))
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
            mTestModelList.getOrElse(position - 1) { ItemMultiSpinner() }.selectedItemSpinner?.itemSpinnerId
                ?: -1
        )
            ?.let {
                holder.bind(it.filterNotNull())
            }
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val searchableView = itemView.findViewWithTag<SearchableView>(SearchableView.TAG)

        init {
            // Detect Physical SearchableView item selection
            searchableView.setOnSpinnerClickedListener(object : SpinnerItemClickedListener{
                override fun onItemClicked(itemSpinner: ItemSpinner) {
                    val position = adapterPosition

                    mTestModelList[position].selectedItemSpinner = mHierarchicList[position].first { itemSpinner.itemSpinnerId == it.itemSpinnerId }
                    resetParentIdsBelow(position)
                    notifyItemsBelow(position)
                    mOuterSpinnerItemSelectedListener.onItemSelected(getLatestItemInHierarchy(position+1, itemSpinner))
                }
            })
        }

        fun bind(itemList: List<ItemSpinner>) {
            if (itemList.isNotEmpty()) {
                val position = adapterPosition

                expandViewIfCollapsed(itemView)
                searchableView.updateItemList(itemList)

                val selectedItemSpinner = mTestModelList[position].selectedItemSpinner ?: itemList[0]
                searchableView.setSelectedItem(selectedItemSpinner)
                mTestModelList[position].selectedItemSpinner = selectedItemSpinner

            } else {
                collapseViewIfExpanded(itemView)
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
            Handler().postDelayed({ notifyItemChanged(position) }, position*100L)
        }
    }

    /**
     * Will reset all parent ids below [currentPosition]
     * @param currentPosition - Current position under which all parent IDs will be reset to default
     */
    private fun resetParentIdsBelow(currentPosition: Int){
        for (position in currentPosition + 1 until itemCount) {
            println("meco prev do do " +position)
            mTestModelList[position].selectedItemSpinner = null
        }
    }

    /**
     * Will expand view. Height will be set to `LayoutParams.WRAP_CONTENT`
     *
     * @param view - View to be expanded
     */
    private fun expandViewIfCollapsed(view: View) {
        if (isCollapsed(view)) {
            val layoutParams = view.layoutParams
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
            view.layoutParams = layoutParams
        }
    }

    /**
     * Will collapse view. Height will be set to `0`
     *
     * @param view - View to be collapsed/expanded
     * @param collpase - Decide whether view wil be collpased or expanded
     */
    private fun collapseViewIfExpanded(view: View) {
        if (isCollapsed(view).not()) {
            val layoutParams = view.layoutParams
            layoutParams.height = 0
            view.layoutParams = layoutParams
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