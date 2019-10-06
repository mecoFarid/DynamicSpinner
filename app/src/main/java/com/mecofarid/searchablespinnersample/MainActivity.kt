package com.mecofarid.searchablespinnersample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.gson.Gson
import com.mecofarid.searchablemultispinner.adapter.SearchableMultiSpinnerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import com.mecofarid.searchablemultispinner.model.ItemSpinner


class MainActivity : AppCompatActivity(), SearchableMultiSpinnerAdapter.SpinnerItemSelectedListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val json = "{\"plant\":[{\"plantId\":\"2044\",\"plantLocation\":\"Chaney Dell\",\"unit\":[{\"unitId\":\"2360\",\"unitName\":\"CD_Compression\",\"equipType\":[{\"equipTypeId\":\"5479\",\"name\":\"Compressor\",\"equip\":{\"equipId\":\"31646\",\"equipNumber\":\"C-01\",\"service\":\"Compressor 1\"}},{\"equipTypeId\":\"5480\",\"name\":\"Vessel\",\"equip\":{\"equipId\":\"31647\",\"equipNumber\":\"V-01\",\"service\":\"Compression Vessel\"}},{\"equipTypeId\":\"5481\",\"name\":\"RV\",\"equip\":{\"equipId\":\"31648\",\"equipNumber\":\"RV-01\",\"service\":\"Compression RV\"}},{\"equipTypeId\":\"5482\",\"name\":\"Air Cooler\",\"equip\":{\"equipId\":\"31649\",\"equipNumber\":\"AC-01\",\"service\":\"Compression Air Cooler\"}},{\"equipTypeId\":\"5483\",\"name\":\"Piping\"}]},{\"unitId\":\"2361\",\"unitName\":\"CD_Glycol\",\"equipType\":[{\"equipTypeId\":\"5484\",\"name\":\"Vessel\",\"equip\":{\"equipId\":\"31650\",\"equipNumber\":\"V-02\",\"service\":\"Glycol Tower\"}},{\"equipTypeId\":\"5485\",\"name\":\"Tank\",\"equip\":{\"equipId\":\"31652\",\"equipNumber\":\"TK-02\",\"service\":\"Glycol Tank\"}},{\"equipTypeId\":\"5486\",\"name\":\"RV\"},{\"equipTypeId\":\"5487\",\"name\":\"Piping\"},{\"equipTypeId\":\"5488\",\"name\":\"Filter\",\"equip\":{\"equipId\":\"31651\",\"equipNumber\":\"F-02\",\"service\":\"Glycol Filter\"}},{\"equipTypeId\":\"5489\",\"name\":\"Exchanger\",\"equip\":{\"equipId\":\"31653\",\"equipNumber\":\"HE-02\",\"service\":\"Glycol Exchanger\"}},{\"equipTypeId\":\"5490\",\"name\":\"Pump\"}]},{\"unitId\":\"2362\",\"unitName\":\"CD_Commodity\",\"equipType\":[{\"equipTypeId\":\"5477\",\"name\":\"Vessel\",\"equip\":{\"equipId\":\"31644\",\"equipNumber\":\"V-03\",\"service\":\"Commodity Vessel\"}},{\"equipTypeId\":\"5478\",\"name\":\"Tank\",\"equip\":{\"equipId\":\"31645\",\"equipNumber\":\"TK-03\",\"service\":\"Commodity Tank\"}}]},{\"unitId\":\"2363\",\"unitName\":\"CD_Loadout\",\"equipType\":[{\"equipTypeId\":\"5491\",\"name\":\"Tank\",\"equip\":{\"equipId\":\"31654\",\"equipNumber\":\"TK-04\",\"service\":\"Loadout Tank\"}},{\"equipTypeId\":\"5492\",\"name\":\"Vessel\",\"equip\":{\"equipId\":\"31655\",\"equipNumber\":\"V-04\",\"service\":\"Loadout Vessel\"}},{\"equipTypeId\":\"5493\",\"name\":\"RV\"}]}]},{\"plantId\":\"2045\",\"plantLocation\":\"Waynoka\"},{\"plantId\":\"2046\",\"plantLocation\":\"Chester\"},{\"plantId\":\"2047\",\"plantLocation\":\"Waynoka 2\"},{\"plantId\":\"2048\",\"plantLocation\":\"Coalgate\"},{\"plantId\":\"2049\",\"plantLocation\":\"Tupelo\"},{\"plantId\":\"2050\",\"plantLocation\":\"Atoka\"},{\"plantId\":\"2051\",\"plantLocation\":\"Velma\"},{\"plantId\":\"2052\",\"plantLocation\":\"Velma 60\"},{\"plantId\":\"2053\",\"plantLocation\":\"Mansfield\"},{\"plantId\":\"2054\",\"plantLocation\":\"Driver\"},{\"plantId\":\"2055\",\"plantLocation\":\"MidkiffCons\"},{\"plantId\":\"2056\",\"plantLocation\":\"Benedum\"},{\"plantId\":\"2057\",\"plantLocation\":\"Silver Oak\"}]}"

        val list = Gson().fromJson<MainModel>(json, MainModel::class.java).plant

        list?.let {
            val adapter = SearchableMultiSpinnerAdapter(list, this)
            multi_spinner.adapter = adapter
        }

    }

    override fun onItemSelected(itemSpinner: ItemSpinner) {
        Log.d("Selected item id: ", itemSpinner.id.toString())
    }

}
