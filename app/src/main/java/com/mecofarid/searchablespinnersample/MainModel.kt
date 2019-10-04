package com.mecofarid.searchablespinnersample

import com.mecofarid.searchablemultispinner.annotation.SubCategory
import com.mecofarid.searchablemultispinner.model.ItemSpinner

class MainModel: ItemSpinner(){
    var plant: List<Plant>? = null

}

class Plant: ItemSpinner(){
    var plantId: String? = null
    var plantLocation: String? = null
    @SubCategory
    var unit: List<Unit>? = null

    override fun toString(): String {
        plantLocation?.let {
            return it
        }

        return ""
    }
}

class Unit: ItemSpinner(){
    @SubCategory
    var equipType: List<EquipType>? = null
    var unitId: String? = null
    var unitName: String? = null
}

class EquipType: ItemSpinner(){

    var equip: Equip? = null
    var equipTypeId: String? = null
    var name: String? = null
}

class Equip: ItemSpinner(){
    var equipId: String? = null
    var equipNumber: String? = null
    var service: String? = null
}