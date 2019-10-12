package com.mecofarid.searchablespinnersample

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.gson.Gson
import com.mecofarid.searchablemultispinner.adapter.SearchableMultiSpinnerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import com.mecofarid.searchablemultispinner.model.ItemSpinner
import com.mecofarid.searchablespinnersample.model.*
import com.mecofarid.searchablespinnersample.model.Byte


class MainActivity : AppCompatActivity(), SearchableMultiSpinnerAdapter.SpinnerItemSelectedListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val plantJson = "{\"plant\":[{\"plantId\":\"2044\",\"plantLocation\":\"Chaney Dell\",\"unit\":[{\"unitId\":\"2360\",\"unitName\":\"CD_Compression\",\"equipType\":[{\"equipTypeId\":\"5479\",\"name\":\"Compressor\",\"equip\":{\"equipId\":\"31646\",\"equipNumber\":\"C-01\",\"service\":\"Compressor 1\"}},{\"equipTypeId\":\"5480\",\"name\":\"Vessel\",\"equip\":{\"equipId\":\"31647\",\"equipNumber\":\"V-01\",\"service\":\"Compression Vessel\"}},{\"equipTypeId\":\"5481\",\"name\":\"RV\",\"equip\":{\"equipId\":\"31648\",\"equipNumber\":\"RV-01\",\"service\":\"Compression RV\"}},{\"equipTypeId\":\"5482\",\"name\":\"Air Cooler\",\"equip\":{\"equipId\":\"31649\",\"equipNumber\":\"AC-01\",\"service\":\"Compression Air Cooler\"}},{\"equipTypeId\":\"5483\",\"name\":\"Piping\"}]},{\"unitId\":\"2361\",\"unitName\":\"CD_Glycol\",\"equipType\":[{\"equipTypeId\":\"5484\",\"name\":\"Vessel\",\"equip\":{\"equipId\":\"31650\",\"equipNumber\":\"V-02\",\"service\":\"Glycol Tower\"}},{\"equipTypeId\":\"5485\",\"name\":\"Tank\",\"equip\":{\"equipId\":\"31652\",\"equipNumber\":\"TK-02\",\"service\":\"Glycol Tank\"}},{\"equipTypeId\":\"5486\",\"name\":\"RV\"},{\"equipTypeId\":\"5487\",\"name\":\"Piping\"},{\"equipTypeId\":\"5488\",\"name\":\"Filter\",\"equip\":{\"equipId\":\"31651\",\"equipNumber\":\"F-02\",\"service\":\"Glycol Filter\"}},{\"equipTypeId\":\"5489\",\"name\":\"Exchanger\",\"equip\":{\"equipId\":\"31653\",\"equipNumber\":\"HE-02\",\"service\":\"Glycol Exchanger\"}},{\"equipTypeId\":\"5490\",\"name\":\"Pump\"}]},{\"unitId\":\"2362\",\"unitName\":\"CD_Commodity\",\"equipType\":[{\"equipTypeId\":\"5477\",\"name\":\"Vessel\",\"equip\":{\"equipId\":\"31644\",\"equipNumber\":\"V-03\",\"service\":\"Commodity Vessel\"}},{\"equipTypeId\":\"5478\",\"name\":\"Tank\",\"equip\":{\"equipId\":\"31645\",\"equipNumber\":\"TK-03\",\"service\":\"Commodity Tank\"}}]},{\"unitId\":\"2363\",\"unitName\":\"CD_Loadout\",\"equipType\":[{\"equipTypeId\":\"5491\",\"name\":\"Tank\",\"equip\":{\"equipId\":\"31654\",\"equipNumber\":\"TK-04\",\"service\":\"Loadout Tank\"}},{\"equipTypeId\":\"5492\",\"name\":\"Vessel\",\"equip\":{\"equipId\":\"31655\",\"equipNumber\":\"V-04\",\"service\":\"Loadout Vessel\"}},{\"equipTypeId\":\"5493\",\"name\":\"RV\"}]}]},{\"plantId\":\"2045\",\"plantLocation\":\"Waynoka\"},{\"plantId\":\"2046\",\"plantLocation\":\"Chester\"},{\"plantId\":\"2047\",\"plantLocation\":\"Waynoka 2\"},{\"plantId\":\"2048\",\"plantLocation\":\"Coalgate\"},{\"plantId\":\"2049\",\"plantLocation\":\"Tupelo\"},{\"plantId\":\"2050\",\"plantLocation\":\"Atoka\"},{\"plantId\":\"2051\",\"plantLocation\":\"Velma\"},{\"plantId\":\"2052\",\"plantLocation\":\"Velma 60\"},{\"plantId\":\"2053\",\"plantLocation\":\"Mansfield\"},{\"plantId\":\"2054\",\"plantLocation\":\"Driver\"},{\"plantId\":\"2055\",\"plantLocation\":\"MidkiffCons\"},{\"plantId\":\"2056\",\"plantLocation\":\"Benedum\"},{\"plantId\":\"2057\",\"plantLocation\":\"Silver Oak\"}]}"
        val json = "{\"planetList\":[{\"countryList\":[{\"code\":1,\"cityList\":[{\"name\":\"Baku\",\"code\":10,\"boroughList\":[{\"name\":\"Nizami\",\"code\":100,\"districtList\":[{\"name\":\"West madow\",\"code\":1000,\"avenueList\":[{\"name\":\"Azdlig ave.\",\"code\":10000,\"streetList\":[{\"name\":\"Lev Landau\",\"code\":100000,\"alleyList\":[{\"name\":\"Hajibekov alley\",\"code\":1000000,\"buildingList\":[{\"name\":\"Hyatt\",\"code\":1000002,\"entanceList\":[{\"name\":\"Entrance 1\",\"code\":1000003,\"storeyList\":[{\"name\":\"1st Floor\",\"code\":1000004,\"apartmentList\":[{\"name\":\"Apartment 109\",\"code\":1000005,\"roomList\":[{\"name\":\"Bedroom\",\"code\":1000006,\"itemList\":[{\"name\":\"Laptop\",\"code\":1000008,\"folderList\":[{\"name\":\"Folder -1\",\"code\":1000009,\"fileList\":[{\"name\":\"Rok.txt\",\"code\":1000010,\"contentList\":[{\"name\":\"Letter A\",\"code\":1000011,\"byteList\":[{\"name\":\"-1kb\",\"code\":1000012}]}]}]}]}]}]}]}]}]}]}]}]}]}]},{\"name\":\"Binagadi\",\"code\":101}]},{\"name\":\"Ganja\",\"code\":11,\"boroughList\":[{\"name\":\"South Ganja\",\"code\":110},{\"name\":\"North Ganja\",\"code\":111}]}],\"name\":\"Azerbaijan\"},{\"code\":2,\"cityList\":[{\"name\":\"Dubai\",\"code\":20},{\"name\":\"Abu Dhabi\",\"code\":21}],\"name\":\"UAE\"},{\"code\":3,\"cityList\":[{\"name\":\"Bangkok\",\"code\":30},{\"name\":\"Pattaya\",\"code\":31}],\"name\":\"Thailand\"},{\"code\":4,\"cityList\":[{\"name\":\"Almaty\",\"code\":40},{\"name\":\"Astana\",\"code\":41}],\"name\":\"Kazakhstan\"},{\"code\":5,\"cityList\":[{\"name\":\"Moscow\",\"code\":50},{\"name\":\"Saint Peterburg\",\"code\":51}],\"name\":\"Russia\"},{\"code\":6,\"cityList\":[{\"name\":\"Tallin\",\"code\":60},{\"name\":\"Tartu\",\"code\":61}],\"name\":\"Estonia\"},{\"code\":7,\"cityList\":[{\"name\":\"Izmir\",\"code\":10},{\"name\":\"Istanbul\",\"code\":11}],\"name\":\"Turkey\"},{\"code\":8,\"cityList\":[{\"name\":\"Rustavi\",\"code\":80},{\"name\":\"Tbilisi\",\"code\":81}],\"name\":\"Georgia\"},{\"code\":1,\"cityList\":[{\"name\":\"Busan\",\"code\":10},{\"name\":\"Daegu\",\"code\":11}],\"name\":\"South Korea\"}],\"name\":\"Earth\"},{\"countryList\":[{\"name\":\"Mercurium country\",\"code\":-1,\"cityList\":[{\"name\":\"Merca city\",\"code\":-2}]}],\"name\":\"Mercury\"},{\"countryList\":[],\"name\":\"Venus\"},{\"countryList\":[],\"name\":\"Mars\"}]}"
        val jsonMercuryFirst = "{\"planetList\":[{\"countryList\":[{\"name\":\"Mercurium country\",\"code\":-1,\"cityList\":[{\"name\":\"Merca city\",\"code\":-2}]}],\"name\":\"Mercury\"},{\"countryList\":[{\"code\":1,\"cityList\":[{\"name\":\"Baku\",\"code\":10,\"boroughList\":[{\"name\":\"Nizami\",\"code\":100,\"districtList\":[{\"name\":\"West madow\",\"code\":1000,\"avenueList\":[{\"name\":\"Azdlig ave.\",\"code\":10000,\"streetList\":[{\"name\":\"Lev Landau\",\"code\":100000,\"alleyList\":[{\"name\":\"Hajibekov alley\",\"code\":1000000,\"buildingList\":[{\"name\":\"Hyatt\",\"code\":1000002,\"entanceList\":[{\"name\":\"Entrance 1\",\"code\":1000003,\"storeyList\":[{\"name\":\"1st Floor\",\"code\":1000004,\"apartmentList\":[{\"name\":\"Apartment 109\",\"code\":1000005,\"roomList\":[{\"name\":\"Bedroom\",\"code\":1000006,\"itemList\":[{\"name\":\"Laptop\",\"code\":1000008,\"folderList\":[{\"name\":\"Folder -1\",\"code\":1000009,\"fileList\":[{\"name\":\"Rok.txt\",\"code\":1000010,\"contentList\":[{\"name\":\"Letter A\",\"code\":1000011,\"byteList\":[{\"name\":\"-1kb\",\"code\":1000012}]}]}]}]}]}]}]}]}]}]}]}]}]}]},{\"name\":\"Binagadi\",\"code\":101}]},{\"name\":\"Ganja\",\"code\":11,\"boroughList\":[{\"name\":\"South Ganja\",\"code\":110},{\"name\":\"North Ganja\",\"code\":111}]}],\"name\":\"Azerbaijan\"},{\"code\":2,\"cityList\":[{\"name\":\"Dubai\",\"code\":20},{\"name\":\"Abu Dhabi\",\"code\":21}],\"name\":\"UAE\"},{\"code\":3,\"cityList\":[{\"name\":\"Bangkok\",\"code\":30},{\"name\":\"Pattaya\",\"code\":31}],\"name\":\"Thailand\"},{\"code\":4,\"cityList\":[{\"name\":\"Almaty\",\"code\":40},{\"name\":\"Astana\",\"code\":41}],\"name\":\"Kazakhstan\"},{\"code\":5,\"cityList\":[{\"name\":\"Moscow\",\"code\":50},{\"name\":\"Saint Peterburg\",\"code\":51}],\"name\":\"Russia\"},{\"code\":6,\"cityList\":[{\"name\":\"Tallin\",\"code\":60},{\"name\":\"Tartu\",\"code\":61}],\"name\":\"Estonia\"},{\"code\":7,\"cityList\":[{\"name\":\"Izmir\",\"code\":10},{\"name\":\"Istanbul\",\"code\":11}],\"name\":\"Turkey\"},{\"code\":8,\"cityList\":[{\"name\":\"Rustavi\",\"code\":80},{\"name\":\"Tbilisi\",\"code\":81}],\"name\":\"Georgia\"},{\"code\":1,\"cityList\":[{\"name\":\"Busan\",\"code\":10},{\"name\":\"Daegu\",\"code\":11}],\"name\":\"South Korea\"}],\"name\":\"Earth\"},{\"countryList\":[],\"name\":\"Venus\"},{\"countryList\":[],\"name\":\"Mars\"}]}"

        val listPlant = Gson().fromJson<MainModel>(plantJson, MainModel::class.java).plant
        val list = Gson().fromJson<Response>(json, Response::class.java).planetList

        listPlant?.let {
            val adapter = SearchableMultiSpinnerAdapter(list, this, R.layout.item_spinner)
            multi_spinner.adapter = adapter
        }
    }

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
