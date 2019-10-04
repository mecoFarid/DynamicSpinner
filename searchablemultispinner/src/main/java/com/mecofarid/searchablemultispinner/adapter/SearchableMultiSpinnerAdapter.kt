package com.mecofarid.searchablemultispinner.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mecofarid.searchablemultispinner.R
import com.mecofarid.searchablemultispinner.interfaces.SpinnerItemSelectedListener
import com.mecofarid.searchablemultispinner.model.ItemSpinner
import com.mecofarid.searchablemultispinner.util.Utils
import com.mecofarid.searchablemultispinner.view.AnapticAutoCompleteTextView
import com.mecofarid.searchablemultispinner.view.SearchableView

class SearchableMultiSpinnerAdapter (nestedList: List<ItemSpinner>, spinnerItemSelectedListener: SpinnerItemSelectedListener):
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

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val searchableView = itemView.findViewById<SearchableView>(R.id.searchable_view)
        fun bind(adapter: ArrayAdapter<ItemSpinner>){
            searchableView.setAutoCompleteAdapter(adapter)
            searchableView.setOnClickListener {
                searchableView.showDropDownIfSearchNotOpen()
            }
            searchableView.setOnSpinnerItemSelectedListener(object: SpinnerItemSelectedListener{
                override fun onItemSelected(itemSpinner: ItemSpinner) {
                    println("meoc "+itemSpinner.level)
                }

            })
        }
    }

    private fun getArrayAdapterAtOrCreate(position: Int, context: Context): ArrayAdapter<ItemSpinner> {
        hierarchicArrayAdapterSet[position]?.let {
            return it
        }
        hierarchicArrayAdapterSet[position] = ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item,
            hierarchicList[position])

        // Can never be null because
        return hierarchicArrayAdapterSet[position]!!
    }
}