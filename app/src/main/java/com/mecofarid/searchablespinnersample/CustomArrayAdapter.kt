package com.mecofarid.searchablespinnersample

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable

class CustomArrayAdapter: ArrayAdapter<String>, Filterable {

    lateinit var ase: MutableList<String>

    constructor(context: Context, resource: Int) : super(context, resource)
    constructor(context: Context, resource: Int, textViewResourceId: Int) : super(
        context,
        resource,
        textViewResourceId
    )

    constructor(context: Context, resource: Int, objects: Array<out String>) : super(
        context,
        resource,
        objects
    )

    constructor(
        context: Context,
        resource: Int,
        textViewResourceId: Int,
        objects: Array<out String>
    ) : super(context, resource, textViewResourceId, objects)

    constructor(context: Context, resource: Int, objects: MutableList<String>) : super(
        context,
        resource,
        objects
    ){
        ase = objects
    }

    constructor(
        context: Context,
        resource: Int,
        textViewResourceId: Int,
        objects: MutableList<String>
    ) : super(context, resource, textViewResourceId, objects)

    override fun getFilter(): Filter {
        val filter = object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val a  = FilterResults()
                val list = ArrayList<String>()

                ase.forEach {
                    if (it.equals(p0).not()){
                        list.add(it)
                    }
                }

                a.count = list.count()
                a.values = list

                println("meco whe "+a.count)
                return a
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                notifyDataSetChanged()
            }

        }

        return filter

    }
}