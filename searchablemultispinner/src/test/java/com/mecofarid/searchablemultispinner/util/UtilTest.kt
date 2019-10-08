package com.mecofarid.searchablemultispinner.util

import com.google.gson.Gson
import com.mecofarid.searchablemultispinner.getJson
import com.mecofarid.searchablemultispinner.model.ItemSpinner
import org.junit.Test

class UtilTest {

    @Test
    fun parseOriginalList_withConventionalModelClass() {
        val list = ListParserUtils.parseToHierarchicFlatList(getNestedList<ConventionalModel>(
            "original.json").plant.orEmpty(), -1,0)

        //Assert that list is not empty
        assert(list.isNotEmpty())
    }

    // Failure Test
    @Test
    fun failureTest_shouldThrowNoZeroArgumentConstructorException(){
        /**
         * Assert that conversion will fail if any model class doesn't have zero argument constructor
         */
        val list = ListParserUtils.parseToHierarchicFlatList(getNestedList<DataModelWithoutZeroArgConstructor>("original.json").plant, -1,0)
        assert(list.isEmpty())
    }

    // Failure Test
    @Test
    fun failureTest_shouldThrowClassNotFoundException(){
        /**
         * Assert that conversion will fail with proper developer message if any of model class doesn't extend [ItemSpinner]
         */
        val list =ListParserUtils.parseToHierarchicFlatList(getNestedList<DataModelWithoutSuperClass>("original.json").plant, -1,0)
        assert(list.isNotEmpty())
    }


    // Failure Test
    @Test
    fun failureTest_shouldThrowClassCastEception(){
        /**
         * Assert that conversion will fail with proper developer message if any non-list item is annotated with [SubCategory]
         */
        val list =ListParserUtils.parseToHierarchicFlatList(getNestedList<DataModelAnnotationOnNonList>("original.json").plant, -1,0)
        assert(list.size == 1)
    }

    @Test
    fun assertHiearachy_shouldHaveSingleHierarchy(){
        /**
         * Assert that hierarchic list will have `1` level if [SubCategory] annotation is not added
         */
        val list =ListParserUtils.parseToHierarchicFlatList(getNestedList<DataModelWithoutAnnotation>("original.json").plant, -1,0)
        assert(list.size == 1)
    }

    @Test
    fun assertHierarchy() {
        val list = ListParserUtils.parseToHierarchicFlatList(getNestedList<ConventionalModel>(
        "original.json").plant.orEmpty(), -1,0)

        list.apply {
            forEach { innerList ->
                innerList.forEach { item ->

                    // Assert all objects at index `n` is of level `n`
                    assert(item.itemSpinnerLevel == this.indexOf(innerList))
                }
            }
        }
    }

    @Test
    fun assertUniqueId(){
        val list = ListParserUtils.parseToHierarchicFlatList(getNestedList<ConventionalModel>(
            "original.json").plant.orEmpty(), -1,0)

        list.apply {
            val frequencyMap = HashMap<ItemSpinner, Int>()

            this.forEach {innerList->
                frequencyMap.putAll(innerList.groupingBy { it }.eachCount().filter { it.value>1 })
            }

            //Assert no duplicate ID
            assert(frequencyMap.entries.size < 2 )
        }
    }

    inline fun <reified T> getNestedList(filename: String): T {
        val json = getJson(javaClass.classLoader, filename)
        return (Gson().fromJson(json, T::class.java) as T)

    }
}