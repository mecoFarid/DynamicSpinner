package com.mecofarid.searchablemultispinner.util

import com.mecofarid.searchablemultispinner.annotation.SubCategory
import com.mecofarid.searchablemultispinner.model.ItemSpinner


data class DataModelWithoutSuperClass(
    val plant: List<DataPlantWithoutSuperClass> = emptyList()
)

data class DataPlantWithoutSuperClass(
    val plantId: String = "",
    val plantLocation: String ="",
    @SubCategory
    val unit: List<DataUnitWithoutSuperClass> = emptyList()
):ItemSpinner()

data class DataUnitWithoutSuperClass(
    @SubCategory
    val equipType: List<DataEquipTypeWithoutSuperClass> = emptyList(),
    val unitId: String = "",
    val unitName: String = ""
)

data class DataEquipTypeWithoutSuperClass(
    val equip: DataEquipWithoutSuperClass = DataEquipWithoutSuperClass(),
    val equipTypeId: String ="",
    val name: String=""
)

data class DataEquipWithoutSuperClass(
    val equipId: String="",
    val equipNumber: String="",
    val service: String=""
)