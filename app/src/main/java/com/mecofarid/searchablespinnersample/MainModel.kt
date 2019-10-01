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