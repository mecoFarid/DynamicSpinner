package com.mecofarid.searchablemultispinner

import com.google.gson.Gson
import com.mecofarid.searchablemultispinner.model.ItemSpinner
import com.mecofarid.searchablemultispinner.util.Utils
import com.mecofarid.searchablemultispinner.utils.getJson
import org.junit.Before
import org.junit.Test


class UtilTest {


    @Test
    fun parseOriginalList_nestedToHierarchicFlatList() {

        // Parse nested list to hierarchic flat list
        val hierarchichFlatList =
            Utils.parseToHierarchicFlatList(getNestedList("original.json"), -1, 0)

        // Assert that all objects at index `n` is of level `n`
        hierarchichFlatList.forEach { itemList ->
            itemList.forEach { item ->
                assert(item.level == hierarchichFlatList.indexOf(itemList))
            }
        }
    }

    fun getNestedList(filename: String): List<ItemSpinner> {
        val json = getJson(javaClass.classLoader, filename)
        return Gson().fromJson<TestModel>(json, TestModel::class.java).plant.orEmpty()

    }
}