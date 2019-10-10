package com.mecofarid.searchablespinnersample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import com.google.gson.Gson
import com.mecofarid.searchablemultispinner.adapter.SearchableMultiSpinnerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import com.mecofarid.searchablemultispinner.model.ItemSpinner
import com.mecofarid.searchablemultispinner.view.SearchableView
import com.mecofarid.searchablespinnersample.model.Response


class MainActivity : AppCompatActivity(), SearchableMultiSpinnerAdapter.SpinnerItemSelectedListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val json = "{\"planetList\":[{\"countryList\":[{\"code\":1,\"cityList\":[{\"name\":\"Baku\",\"code\":10,\"boroughList\":[{\"name\":\"Nizami\",\"code\":100,\"districtList\":[{\"name\":\"West madow\",\"code\":1000,\"avenueList\":[{\"name\":\"Azdlig ave.\",\"code\":10000,\"streetList\":[{\"name\":\"Lev Landau\",\"code\":100000,\"alleyList\":[{\"name\":\"Hajibekov alley\",\"code\":1000000,\"buildingList\":[{\"name\":\"Hyatt\",\"code\":1000002,\"entanceList\":[{\"name\":\"Entrance 1\",\"code\":1000003,\"storeyList\":[{\"name\":\"1st Floor\",\"code\":1000004,\"apartmentList\":[{\"name\":\"Apartment 109\",\"code\":1000005,\"roomList\":[{\"name\":\"Bedroom\"}]}]}]}]}]}]}]}]}]},{\"name\":\"Binagadi\",\"code\":101}]},{\"name\":\"Ganja\",\"code\":11,\"boroughList\":[{\"name\":\"South Ganja\",\"code\":110},{\"name\":\"North Ganja\",\"code\":111}]}],\"name\":\"Azerbaijan\"},{\"code\":2,\"cityList\":[{\"name\":\"Dubai\",\"code\":20},{\"name\":\"Abu Dhabi\",\"code\":21}],\"name\":\"UAE\"},{\"code\":3,\"cityList\":[{\"name\":\"Bangkok\",\"code\":30},{\"name\":\"Pattaya\",\"code\":31}],\"name\":\"Thailand\"},{\"code\":4,\"cityList\":[{\"name\":\"Almaty\",\"code\":40},{\"name\":\"Astana\",\"code\":41}],\"name\":\"Kazakhstan\"},{\"code\":5,\"cityList\":[{\"name\":\"Moscow\",\"code\":50},{\"name\":\"Saint Peterburg\",\"code\":51}],\"name\":\"Russia\"},{\"code\":6,\"cityList\":[{\"name\":\"Tallin\",\"code\":60},{\"name\":\"Tartu\",\"code\":61}],\"name\":\"Estonia\"},{\"code\":7,\"cityList\":[{\"name\":\"Izmir\",\"code\":10},{\"name\":\"Istanbul\",\"code\":11}],\"name\":\"Turkey\"},{\"code\":8,\"cityList\":[{\"name\":\"Rustavi\",\"code\":80},{\"name\":\"Tbilisi\",\"code\":81}],\"name\":\"Georgia\"},{\"code\":1,\"cityList\":[{\"name\":\"Busan\",\"code\":10},{\"name\":\"Daegu\",\"code\":11}],\"name\":\"South Korea\"}],\"name\":\"Earth\"},{\"countryList\":[],\"name\":\"Mercury\"},{\"countryList\":[],\"name\":\"Venus\"},{\"countryList\":[],\"name\":\"Mars\"}]}"

        val list = Gson().fromJson<Response>(json, Response::class.java).planetList

        list?.let {
            val adapter = SearchableMultiSpinnerAdapter(list, this, R.layout.item_spinner)
            multi_spinner.adapter = adapter
        }
    }

    override fun onItemSelected(itemSpinner: ItemSpinner) {
        Log.d("Selected item id: ", itemSpinner.itemSpinnerId.toString())
    }

}
