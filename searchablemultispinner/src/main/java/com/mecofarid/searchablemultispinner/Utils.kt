package com.mecofarid.searchablemultispinner

import com.mecofarid.searchablemultispinner.annotation.SubCategory
import com.mecofarid.searchablemultispinner.model.ItemSpinner
import java.lang.reflect.Field

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
public fun hierarchicToRelationalList(list: List<ItemSpinner>, parentId: Int, level: Int){
    var id: Long = -1
    list.forEach {
        id ++
        it.id = id
        it.parentId = parentId
        it.level = level

        val subCategory = getSubcategory<List<ItemSpinner>>(it)
        if (subCategory != null){
            hierarchicToRelationalList(subCategory, parentId, level+1)
        }
    }
}

// Return subcategory
private inline fun <reified T> getSubcategory(obj: ItemSpinner): T?{
    obj.javaClass.declaredFields.forEach {
        if (isSubCategory(it)){
           return it.get(obj) as T
        }
    }
    return null
}

/**
 * @param field - Check if field is annotated with subcategory
 */
private fun isSubCategory(field: Field) = field.isAnnotationPresent(SubCategory::class.java)