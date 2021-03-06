package com.mecofarid.dynamicspinner.util

import com.mecofarid.dynamicspinner.annotation.SubCategory
import com.mecofarid.dynamicspinner.model.ItemSpinner


data class DataModelAnnotationOnNonList(
    val plant: List<DataPlantAnnotationOnNonList> = emptyList()
)

data class DataPlantAnnotationOnNonList(
    @SubCategory
    val plantId: String = "",
    val plantLocation: String ="",
    val unit: List<DataUnitAnnotationOnNonList> = emptyList()
):ItemSpinner()

data class DataUnitAnnotationOnNonList(
    val equipType: List<DataEquipTypeAnnotationOnNonList> = emptyList(),
    val unitId: String = "",
    val unitName: String = ""
):ItemSpinner()

data class DataEquipTypeAnnotationOnNonList(
    val equip: DataEquipAnnotationOnNonList = DataEquipAnnotationOnNonList(),
    val equipTypeId: String ="",
    val name: String=""
):ItemSpinner()

data class DataEquipAnnotationOnNonList(
    val equipId: String="",
    val equipNumber: String="",
    val service: String=""
):ItemSpinner()