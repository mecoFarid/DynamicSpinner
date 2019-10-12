package com.mecofarid.dynamicspinnersample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.gson.Gson
import com.mecofarid.dynamicspinner.adapter.DynamicSpinnerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import com.mecofarid.dynamicspinner.model.ItemSpinner
import com.mecofarid.dynamicspinnersample.model.*
import com.mecofarid.dynamicspinnersample.model.Byte


class MainActivity : AppCompatActivity(), DynamicSpinnerAdapter.SpinnerItemSelectedListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val planetStructure = "{\"planetList\":[{\"countryList\":[{\"code\":1,\"cityList\":[{\"name\":\"Baku\",\"code\":10,\"boroughList\":[{\"name\":\"Nizami\",\"code\":100,\"districtList\":[{\"name\":\"West madow\",\"code\":1000,\"avenueList\":[{\"name\":\"Azdlig ave.\",\"code\":10000,\"streetList\":[{\"name\":\"Lev Landau\",\"code\":100000,\"alleyList\":[{\"name\":\"Hajibekov alley\",\"code\":1000000,\"buildingList\":[{\"name\":\"Hyatt\",\"code\":1000002,\"entanceList\":[{\"name\":\"Entrance 1\",\"code\":1000003,\"storeyList\":[{\"name\":\"1st Floor\",\"code\":1000004,\"apartmentList\":[{\"name\":\"Apartment 109\",\"code\":1000005,\"roomList\":[{\"name\":\"Bedroom\",\"code\":1000006,\"itemList\":[{\"name\":\"Laptop\",\"code\":1000008,\"folderList\":[{\"name\":\"Folder -1\",\"code\":1000009,\"fileList\":[{\"name\":\"Rok.txt\",\"code\":1000010,\"contentList\":[{\"name\":\"Letter A\",\"code\":1000011,\"byteList\":[{\"name\":\"-1kb\",\"code\":1000012}]}]}]}]}]}]}]}]}]}]}]}]}]},{\"name\":\"East Krakow\",\"code\":1001,\"avenueList\":[{\"name\":\"Bashir Sefereov ave.\",\"code\":10001,\"streetList\":[{\"name\":\"Nikita Labov\",\"code\":100001}]},{\"name\":\"Prehis Albania ave\",\"code\":10003,\"streetList\":[]}]},{\"name\":\"Roden highs\",\"code\":1002,\"avenueList\":[]}]},{\"name\":\"Binagadi\",\"code\":101}]},{\"name\":\"Ganja\",\"code\":11,\"boroughList\":[{\"name\":\"South Ganja\",\"code\":110},{\"name\":\"North Ganja\",\"code\":111}]}],\"name\":\"Azerbaijan\"},{\"code\":2,\"cityList\":[{\"name\":\"Dubai\",\"code\":20},{\"name\":\"Abu Dhabi\",\"code\":21}],\"name\":\"UAE\"},{\"code\":3,\"cityList\":[{\"name\":\"Bangkok\",\"code\":30,\"boroughList\":[{\"name\":\"Kingtown\",\"code\":300}]},{\"name\":\"Pattaya\",\"code\":31}],\"name\":\"Thailand\"},{\"code\":4,\"cityList\":[{\"name\":\"Almaty\",\"code\":40},{\"name\":\"Astana\",\"code\":41}],\"name\":\"Kazakhstan\"},{\"code\":5,\"cityList\":[{\"name\":\"Moscow\",\"code\":50},{\"name\":\"Saint Peterburg\",\"code\":51}],\"name\":\"Russia\"},{\"code\":6,\"cityList\":[{\"name\":\"Tallin\",\"code\":60},{\"name\":\"Tartu\",\"code\":61}],\"name\":\"Estonia\"},{\"code\":7,\"cityList\":[{\"name\":\"Izmir\",\"code\":10},{\"name\":\"Istanbul\",\"code\":11}],\"name\":\"Turkey\"},{\"code\":8,\"cityList\":[{\"name\":\"Rustavi\",\"code\":80},{\"name\":\"Tbilisi\",\"code\":81}],\"name\":\"Georgia\"},{\"code\":1,\"cityList\":[{\"name\":\"Busan\",\"code\":10},{\"name\":\"Daegu\",\"code\":11}],\"name\":\"South Korea\"}],\"name\":\"Earth\"},{\"countryList\":[{\"name\":\"Mercurium country\",\"code\":-1,\"cityList\":[{\"name\":\"Merca city\",\"code\":-2}]}],\"name\":\"Mercury\"},{\"countryList\":[],\"name\":\"Venus\"},{\"countryList\":[],\"name\":\"Mars\"}]}"
        val list = Gson().fromJson<Response>(planetStructure, Response::class.java).planetList

        list?.let {
            val adapter = DynamicSpinnerAdapter(it, this, R.layout.item_spinner)
            dynamic_spinner.adapter = adapter
        }
    }

    // Numbers shows levels of nested items. That means there're 18 nested lists
    override fun onItemSelected(itemSpinner: ItemSpinner) {
        when(itemSpinner.itemSpinnerLevel){
            0 -> showToast((itemSpinner as Planet).name)
            1 -> showToast((itemSpinner as Country).name)
            2 -> showToast((itemSpinner as City).name)
            3 -> showToast((itemSpinner as Borough).name)
            4 -> showToast((itemSpinner as District).name)
            5 -> showToast((itemSpinner as Avenue).name)
            6 -> showToast((itemSpinner as Street).name)
            7 -> showToast((itemSpinner as Alley).name)
            8 -> showToast((itemSpinner as Building).name)
            9 -> showToast((itemSpinner as Entrance).name)
            10 -> showToast((itemSpinner as Storey).name)
            11 -> showToast((itemSpinner as Apartment).name)
            12 -> showToast((itemSpinner as Room).name)
            13 -> showToast((itemSpinner as Item).name)
            14 -> showToast((itemSpinner as Folder).name)
            15 -> showToast((itemSpinner as File).name)
            16 -> showToast((itemSpinner as Content).name)
            17 -> showToast((itemSpinner as Byte).name)
        }
    }
    
    private fun showToast(message: String){
        Toast.makeText(this, "Selected item: ${message}", Toast.LENGTH_SHORT).show()
    }

}
