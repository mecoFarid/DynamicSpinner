package com.mecofarid.searchablemultispinner.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mecofarid.searchablemultispinner.R
import com.mecofarid.searchablemultispinner.model.ItemSpinner
import com.mecofarid.searchablemultispinner.util.Utils
import com.mecofarid.searchablemultispinner.view.SearchableView

/**
 * @param nestedList - This is nested list
 * @param mOuterSpinnerItemClickedListener - The listener from outer class through which that class will be notified when
 * item selected
 */
class SearchableMultiSpinnerAdapter (nestedList: List<ItemSpinner>, private val mOuterSpinnerItemClickedListener: SpinnerItemSelectedListener):
    RecyclerView.Adapter<SearchableMultiSpinnerAdapter.ViewHolder>() {

    val hierarchicList = Utils.toHierarchicFlatList(nestedList, -1, 0)
    /**
     * Map ArrayAdapter to recyclerview's item position. This will ensure that we cache ArrayAdapter on each position instead
     * of recreating it, every time [onBindViewHolder] is called
     */
    val hierarchicArrayAdapterSet = HashMap<Int, ArrayAdapter<ItemSpinner>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_spinner, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return hierarchicList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        val position = holder.adapterPosition
        val context = holder.searchableView.context
        holder.bind(getArrayAdapterAtOrCreate(position, context))
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val searchableView = itemView.findViewById<SearchableView>(R.id.searchable_view)
        init {
            // Physical click on SearchableView will open dop-down selection list
            searchableView.setOnClickListener {
                searchableView.showDropDownIfSearchNotOpen()
            }

            // Detect automatic SearchableView item selection
            searchableView.setOnSpinnerItemSelectedListener(object: SpinnerItemSelectedListener{
                override fun onItemSelected(itemSpinner: ItemSpinner) {
                    mOuterSpinnerItemClickedListener.onItemSelected(itemSpinner)
                    notifyItemsBelow(adapterPosition)
                }

            })
        }

        fun bind(adapter: ArrayAdapter<ItemSpinner>){
            searchableView.setAutoCompleteAdapter(adapter)
            println("mecod upd")
        }
    }

    private fun getArrayAdapterAtOrCreate(position: Int, context: Context): ArrayAdapter<ItemSpinner> {
        hierarchicArrayAdapterSet[position]?.let {
            return it
        }
        hierarchicArrayAdapterSet[position] = ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item,
            hierarchicList[position])

        // Can never be null because above we already set a value if value is null
        return hierarchicArrayAdapterSet[position]!!
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

    // Listener to notify classes (that implement this interface) when item selected
    interface SpinnerItemSelectedListener {
        fun onItemSelected(itemSpinner: ItemSpinner)
    }
}