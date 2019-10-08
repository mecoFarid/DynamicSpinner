package com.mecofarid.searchablemultispinner.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mecofarid.searchablemultispinner.R
import com.mecofarid.searchablemultispinner.model.ItemSpinner
import com.mecofarid.searchablemultispinner.util.ListParserUtils
import com.mecofarid.searchablemultispinner.view.SearchableView
import java.util.logging.Handler

/**
 * @param nestedList - This is nested list
 * @param mOuterSpinnerItemClickedListener - The listener from outer class through which that class will be notified when
 * item selected
 */
class SearchableMultiSpinnerAdapter (nestedList: List<ItemSpinner>, private val mOuterSpinnerItemClickedListener: SpinnerItemSelectedListener):
    RecyclerView.Adapter<SearchableMultiSpinnerAdapter.ViewHolder>() {

    var mParentId = -1L

    val mHierarchicList = ListParserUtils.parseToHierarchicFlatList(nestedList, -1, 0)

    // RecyclerView that observes this adapter
    var mRecyclerView: RecyclerView? =null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        mRecyclerView = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_spinner, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mHierarchicList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        getSubCategoryOf(holder.adapterPosition, mParentId)?.let {
            holder.bind(it.filterNotNull())
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val searchableView = itemView.findViewById<SearchableView>(R.id.searchable_view)
        init {
            // Detect automatic SearchableView item selection
            searchableView.setOnSpinnerItemSelectedListener(object: SpinnerItemSelectedListener{
                override fun onItemSelected(itemSpinner: ItemSpinner) {
                    mParentId = itemSpinner.id

                    mOuterSpinnerItemClickedListener.onItemSelected(itemSpinner)
                    notifyItemsBelow(adapterPosition)
                }

            })
        }
        fun bind(itemList: List<ItemSpinner>) {
            if (itemList.isNotEmpty()) {
                expandViewIfCollapsed(itemView)
                searchableView.updateItemList(itemList)
                mRecyclerView?.post {
                    searchableView.setSelectedItem(itemList[0])

                }
            }else{
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
    private fun getSubCategoryOf(position: Int, parentId: Long): List<ItemSpinner?>?{
        return mHierarchicList[position].filter {
            it.parentId == parentId
        }
    }

    /**
     * Will update all RecyclerView items below item at [currentPosition]
     * @param currentPosition - Position of current RecyclerView item from which other views' update is requested
     */
    private fun notifyItemsBelow(currentPosition: Int){
        for (position in currentPosition+1..itemCount){
            notifyItemChanged(position)
        }
    }

    /**
     * Will expand view. Height will be set to `LayoutParams.WRAP_CONTENT`
     *
     * @param view - View to be expanded
     */
    private fun expandViewIfCollapsed(view: View){
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
    private fun collapseViewIfExpanded(view: View){
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
    private fun isCollapsed(view: View): Boolean{
        return view.layoutParams.height == 0
    }

    // Listener to notify classes (that implement this interface) when item selected
    interface SpinnerItemSelectedListener {
        fun onItemSelected(itemSpinner: ItemSpinner)
    }
}