package com.mecofarid.searchablespinnersample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.ArrayAdapter
import com.google.gson.Gson
import com.mecofarid.searchablemultispinner.adapter.SearchableMultiSpinnerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import com.mecofarid.searchablemultispinner.adapter.SearchableMultiSpinnerAdapter.SpinnerItemSelectedListener
import com.mecofarid.searchablemultispinner.model.ItemSpinner
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), SearchableMultiSpinnerAdapter.SpinnerItemSelectedListener{
    override fun onItemSelected(itemSpinner: ItemSpinner) {
        println("meco here too "+itemSpinner.level)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val json = "{\"plant\":[{\"plantId\":\"2044\",\"plantLocation\":\"Chaney Dell\",\"unit\":[{\"unitId\":\"2360\",\"unitName\":\"CD_Compression\",\"equipType\":[{\"equipTypeId\":\"5479\",\"name\":\"Compressor\",\"equip\":{\"equipId\":\"31646\",\"equipNumber\":\"C-01\",\"service\":\"Compressor 1\"}},{\"equipTypeId\":\"5480\",\"name\":\"Vessel\",\"equip\":{\"equipId\":\"31647\",\"equipNumber\":\"V-01\",\"service\":\"Compression Vessel\"}},{\"equipTypeId\":\"5481\",\"name\":\"RV\",\"equip\":{\"equipId\":\"31648\",\"equipNumber\":\"RV-01\",\"service\":\"Compression RV\"}},{\"equipTypeId\":\"5482\",\"name\":\"Air Cooler\",\"equip\":{\"equipId\":\"31649\",\"equipNumber\":\"AC-01\",\"service\":\"Compression Air Cooler\"}},{\"equipTypeId\":\"5483\",\"name\":\"Piping\"}]},{\"unitId\":\"2361\",\"unitName\":\"CD_Glycol\",\"equipType\":[{\"equipTypeId\":\"5484\",\"name\":\"Vessel\",\"equip\":{\"equipId\":\"31650\",\"equipNumber\":\"V-02\",\"service\":\"Glycol Tower\"}},{\"equipTypeId\":\"5485\",\"name\":\"Tank\",\"equip\":{\"equipId\":\"31652\",\"equipNumber\":\"TK-02\",\"service\":\"Glycol Tank\"}},{\"equipTypeId\":\"5486\",\"name\":\"RV\"},{\"equipTypeId\":\"5487\",\"name\":\"Piping\"},{\"equipTypeId\":\"5488\",\"name\":\"Filter\",\"equip\":{\"equipId\":\"31651\",\"equipNumber\":\"F-02\",\"service\":\"Glycol Filter\"}},{\"equipTypeId\":\"5489\",\"name\":\"Exchanger\",\"equip\":{\"equipId\":\"31653\",\"equipNumber\":\"HE-02\",\"service\":\"Glycol Exchanger\"}},{\"equipTypeId\":\"5490\",\"name\":\"Pump\"}]}]}]}"

        val list = Gson().fromJson<MainModel>(json, MainModel::class.java).plant

        list?.let {
            val adapter = SearchableMultiSpinnerAdapter(list, this)
            recyclerview.adapter = adapter
        }


        val strings = arrayListOf("ara","aru","bara","cara","dara","gara","hara")
        val stroong = ArrayList<String>()
        val stringadapter = CustomArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, stroong)
        lete.setAdapter(stringadapter)

        stroong.addAll(strings)


        strings[0] = "arra"
        Handler().postDelayed({
            println("meco bara")
            println("meco bara "+strings[1])
        }, 5000)

    }

}
