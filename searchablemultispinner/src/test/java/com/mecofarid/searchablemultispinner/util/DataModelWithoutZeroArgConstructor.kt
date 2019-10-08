package com.mecofarid.searchablemultispinner.util

import com.mecofarid.searchablemultispinner.annotation.SubCategory
import com.mecofarid.searchablemultispinner.model.ItemSpinner

data class DataModelWithoutZeroArgConstructor(
    val plant: List<DataPlantWithoutZeroArgConstructor>
):ItemSpinner()

data class DataPlantWithoutZeroArgConstructor(
    val plantId: String,
    val plantLocation: String,
    @SubCategory
    val unit: List<DataUnitWithoutZeroArgConstructor>
):ItemSpinner()

data class DataUnitWithoutZeroArgConstructor(
    @SubCategory
    val equipType: List<DataEquipTypeWithoutZeroArgConstructor>,
    val unitId: String,
    val unitName: String
):ItemSpinner()

data class DataEquipTypeWithoutZeroArgConstructor(
    val equip: DataEquipWithoutZeroArgConstructor,
    val equipTypeId: String,
    val name: String
):ItemSpinner()

data class DataEquipWithoutZeroArgConstructor(
    val equipId: String,
    val equipNumber: String,
    val service: String
):ItemSpinner()