package com.mecofarid.searchablemultispinner

import com.mecofarid.searchablemultispinner.annotation.SubCategory
import com.mecofarid.searchablemultispinner.model.ItemSpinner
import java.lang.reflect.Field

class Utils {
// This method will convert hierarchic list to relational list. Meaning, in hierarchic form, list objects looks like
// nested JSON object, however in case of relational from, list object will look like JSON with single root and all items
// will have id and parentId to indicate how they relate to other items of higher level.

// Hierarchic example:
/*
__ plant 1
      |____unit 1
              |____equipment 1
              |____equipment 2
              |____equipment 3

      |____unit 2
              |____equipment 4
              |____equipment 5
              |____equipment 6

      |____unit 3
              |____equipment 7
              |____equipment 8
              |____equipment 9

__ plant 2
      |_____unit 4
              |____


              AND ETC.
 */

// Relational example:
// NOTE: every item will have parentId to detect who is parent
/*
--|__ plant 1, plant 2
  |__ unit 1, unit 2, unit 3, unit 4
  |__ equipment 1, equipment 2, equipment 3, equipment 4.......
 */

    /**
     * First time passed parameters should be as follows: id = -1, parentId = -1, level = 0
     */
    var itemId: Long = -1

    fun hierarchicToRelationalList(
        inputList: List<ItemSpinner>,
        parentId: Int,
        level: Int
    ): List<ItemSpinner> {

        val outputList = ArrayList<ItemSpinner>()
        inputList.forEach { item ->
            itemId++
            item.id = itemId
            item.parentId = parentId
            item.level = level

            // Add current item to list to be returned to calling function
            outputList.add(cloneObjectWithoutSubcategory(item))

            getSubcategory<List<ItemSpinner>>(item)?.let { itemList ->

                // Add subcategories to list to be returned to calling function
                outputList.addAll(
                    hierarchicToRelationalList(
                        itemList,
                        itemId.toInt(),
                        level + 1
                    )
                )
            }
        }
        return outputList;
    }

    // Return subcategory
    private inline fun <reified T> getSubcategory(obj: ItemSpinner): T? {
        obj.javaClass.declaredFields.forEach {
            if (isSubCategory(it)) {
                return it.get(obj) as T
            }
        }
        return null
    }

    /**
     * @param field - Check if field is annotated with subcategory
     */
    private fun isSubCategory(field: Field) = field.isAnnotationPresent(SubCategory::class.java)

    /**
     * @param obj Object will be copied to another object except field annotated with [SubCategory]
     */
    private fun cloneObjectWithoutSubcategory(obj: ItemSpinner): ItemSpinner{
        javaClass.declaredFields.forEach {
            if (isSubCategory(it)){
                /**
                 * Set field's, annotated with [SubCategory], value to null
                 */
                it.set(obj, null)
            }
        }

        return obj
    }

}