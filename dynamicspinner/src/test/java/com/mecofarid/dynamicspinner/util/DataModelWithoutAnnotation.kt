package com.mecofarid.dynamicspinner.util

import com.mecofarid.dynamicspinner.model.ItemSpinner


data class DataModelWithoutAnnotation(
    val plant: List<DataPlantWithoutAnnotation> = emptyList()
)

data class DataPlantWithoutAnnotation(
    val plantId: String = "",
    val plantLocation: String ="",
    val unit: List<DataUnitWithoutAnnotation> = emptyList()
):ItemSpinner()

data class DataUnitWithoutAnnotation(
    val equipType: List<DataEquipTypeWithoutAnnotation> = emptyList(),
    val unitId: String = "",
    val unitName: String = ""
):ItemSpinner()

data class DataEquipTypeWithoutAnnotation(
    val equip: DataEquipWithoutAnnotation = DataEquipWithoutAnnotation(),
    val equipTypeId: String ="",
    val name: String=""
):ItemSpinner()

data class DataEquipWithoutAnnotation(
    val equipId: String="",
    val equipNumber: String="",
    val service: String=""
):ItemSpinner()