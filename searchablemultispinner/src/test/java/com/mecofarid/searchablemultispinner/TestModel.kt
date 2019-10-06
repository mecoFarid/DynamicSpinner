package com.mecofarid.searchablemultispinner

import com.mecofarid.searchablemultispinner.annotation.SubCategory
import com.mecofarid.searchablemultispinner.model.ItemSpinner

class TestModel: ItemSpinner(){
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
    override fun toString(): String {
        unitName?.let {
            return it
        }

        return ""
    }
}

class EquipType: ItemSpinner(){

    var equip: Equip? = null
    var equipTypeId: String? = null
    var name: String? = null

    override fun toString(): String {
        name?.let {
            return it
        }

        return ""
    }
}

class Equip: ItemSpinner(){
    var equipId: String? = null
    var equipNumber: String? = null
    var service: String? = null

    override fun toString(): String {
        service?.let {
            return it
        }

        return ""
    }
}