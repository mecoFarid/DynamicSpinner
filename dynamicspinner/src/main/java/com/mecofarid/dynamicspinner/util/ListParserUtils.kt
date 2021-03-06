package com.mecofarid.dynamicspinner.util

import com.mecofarid.dynamicspinner.annotation.SubCategory
import com.mecofarid.dynamicspinner.model.ItemSpinner
import java.lang.ClassCastException
import java.lang.reflect.Field
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


/**
    This method will convert nested list to flat list. Meaning, in nested form, list objects looks like
    nested JSON object, however in case of flat from, list object will look like JSON with single root and all items
    will have id and parentId to indicate how they relate to other items of higher level.

     Hierarchic example:

   ___ plant 1
    |     |____unit 1
    |             |____equipment 1
    |             |____equipment 2
    |             |____equipment 3
    |
    |     |____unit 2
    |             |____equipment 4
    |             |____equipment 5
    |             |____equipment 6
    |
    |     |____unit 3
    |             |____equipment 7
    |             |____equipment 8
    |             |____equipment 9
    |
    |_ plant 2
          |_____unit 4
                  |____


                  AND ETC.


    Relational example:
    NOTE: every item will have parentId to detect who is parent

    --|__ plant 1, plant 2
      |__ unit 1, unit 2, unit 3, unit 4
      |__ equipment 1, equipment 2, equipment 3, equipment 4.......
*/

internal class ListParserUtils {
    companion object {

        private val sFieldSet = HashMap<Class<*>, Array<Field>>()
        private val sPairedFieldSet = HashMap<Class<*>, Map<Field, Field>>()

        private var sItemId: Long = -1

        /**
         * First time passed parameters should be as follows: id = -1, parentId = -1, level = 0
         * Converts nested [nestedList] to hierarchic flat list of items
         *
         * @param nestedList - The nested list
         * @param parentId - Id of parent item in flat list. Since returned list is flat, we have to keep reference
         * to parents in nested list
         * @param level - Category level of current item
         */
        internal fun parseToHierarchicFlatList(
            nestedList: List<ItemSpinner?>,
            parentId: Long,
            level: Int
        ): List<List<ItemSpinner>> {
            val hierarchicList = ArrayList<ArrayList<ItemSpinner>>()

            getFlatList(nestedList, parentId, level).forEach { itemSpinner ->
                hierarchicList.apply {
                    this.elementAtOrNull(itemSpinner.itemSpinnerLevel)?.let { innerList ->
                        innerList.add(itemSpinner)
                        // continue to next item otherwise `add(arrayListOf(itemSpinner))` will be executed and add
                        // unnecessary hierarchy
                        return@forEach
                    }
                    add(arrayListOf(itemSpinner))
                }
            }
            /**
             * Reset static fields before returning results otherwise will
             * cause @see <a href =https://github.com/mecoFarid/DynamicSpinner/issues/2>Issue 2</a>
             */
            resetStaticFileds()
            return hierarchicList
        }

        /**
         * Converts nested [nestedList] to flat list of items
         *
         * @param nestedList - The nested list
         * @param parentId - Id of parent item in flat list. Since returned list is flat, we have to keep reference
         * to parents in nested list
         * @param level - Category level of current item
         */
        fun getFlatList(
            nestedList: List<ItemSpinner?>,
            parentId: Long,
            level: Int
        ): List<ItemSpinner> {
            val outputList = ArrayList<ItemSpinner>()
            nestedList.forEach { item ->
                item?.let {
                    sItemId++
                    item.itemSpinnerId = sItemId
                    item.itemSpinnerParentId = parentId
                    item.itemSpinnerLevel = level

                    // Add current item to list to be returned to calling function
                    outputList.add(cloneObjectExceptAnnotatedField(item, SubCategory::class.java))

                    getSubcategory<List<ItemSpinner>>(item)?.let { itemList ->

                        // Add subcategories to list to be returned to calling function
                        outputList.addAll(
                            getFlatList(
                                itemList,
                                sItemId,
                                level + 1
                            )
                        )
                    }
                }
            }
            return outputList;
        }

        /**
         * Return subcategory of given object
         *
         * @param obj - Subcategory of this object will be returned
         */
        private inline fun <reified T> getSubcategory(obj: ItemSpinner): T? {
            getFields(obj.javaClass).forEach {
                if (it.isAnnotationPresent(SubCategory::class.java)) {
                    /**
                     * If field is annotated with [SubCategory] it should be of type List
                     */
                    kotlin.runCatching { return it.get(obj) as T? }.getOrNull()
                        ?: throw ClassCastException(
                            "$it cannot be cast to ${T::class.java} " +
                                    "make sure you annotated correct field with ${SubCategory::class.java}"
                        )
                }
            }
            return null
        }

        /**
         * @param sourceObject Object to be copied to another object except field annotated with [SubCategory]
         */
        private inline fun <reified T: Any>  cloneObjectExceptAnnotatedField(
            sourceObject: T,
            exceptionAnnotation: Class<out Annotation>
        ): T {
            val targetObject = kotlin.runCatching {
                Class.forName(sourceObject.javaClass.name).getConstructor().newInstance()
            }.getOrNull()
                ?: throw NoSuchMethodException(
                    "${sourceObject::class.java} has no zero argument constructor; please, generate zero argument constructor for this class"
                )
            val pairedFieldSet = getPairedFields(sourceObject.javaClass, targetObject.javaClass)

            pairedFieldSet.keys.forEach { targetField ->
                pairedFieldSet[targetField]?.let { sourceField ->
                    getValue(sourceField, sourceObject)?.let { sourceValue ->
                        setValueExcept(
                            targetField,
                            targetObject,
                            sourceValue,
                            exceptionAnnotation
                        )
                    }
                }
            }
            return targetObject as T
        }

        /**
         * Returns mapped field set that exists in both [source] and [target]
         *
         * @param target - Target class
         * @param source - Source class
         *
         * Will never return null because [mapCorrespondingFields] never returns null
         */
        private fun getPairedFields(source: Class<*>, target: Class<*>): Map<Field, Field> {
            sPairedFieldSet[target]?.let {
                return it
            }

            val pairedFieldSet = mapCorrespondingFields(source, target)
            sPairedFieldSet[target] = pairedFieldSet
            return sPairedFieldSet.getOrElse(target,{pairedFieldSet})
        }

        /**
         * Maps corresponding fields in [source] to [target]
         *
         * @param target - Target class
         * @param source - Source class
         */
        private fun mapCorrespondingFields(
            source: Class<*>,
            target: Class<*>
        ): Map<Field, Field> {
            val mappedFields = HashMap<Field, Field>()
            val sourceFieldList = getFields(source)
            val targetFieldList = getFields(target)

            targetFieldList.forEach { targetField ->
                getCorrespondingSourceField(
                    targetField,
                    sourceFieldList
                )?.let { correspondingSourceField ->
                    mappedFields[targetField] = correspondingSourceField
                }
            }

            return mappedFields
        }

        /**
         * @param clazz - Get fields of this class (check cache first)
         * Will never return null because [Class.getDeclaredFields] is Non-Null
         */
        private fun getFields(clazz: Class<*>): Array<Field> {
            // Check if that classes fields are in cache use them other wise add them
            sFieldSet[clazz]?.let {
                return it
            }

            val classFields = clazz.declaredFields
            val rootParentFields = clazz.superclass?.declaredFields
                ?: throw ClassNotFoundException(
                    "${clazz::class.java} must extends ${ItemSpinner::class.java}. For more info, " +
                            "check library's README.md here https://github.com/mecoFarid/DynamicSpinner"
                )
            val allFields = arrayOf(*classFields, *rootParentFields)
            sFieldSet[clazz] = allFields
            return sFieldSet.getOrElse(clazz, {allFields})
        }


        /**
         * Check if [targetField] is present in [sourceFields], if yes return corresponding source field
         *
         * @param targetField - Target field
         * @param sourceFields - Source fields
         */
        private fun getCorrespondingSourceField(
            targetField: Field,
            sourceFields: Array<Field>
        ): Field? {
            for (sourceField in sourceFields) {
                if (targetField.name == sourceField.name) {
                    return sourceField
                }
            }
            return null
        }

        /**
         * Set [sourceValue] to corresponding [targetField] of [target] object
         * @param sourceValue - Source value
         * @param targetField - Target field that corresponds field of source value
         * @param target - Target object that will receive the source value
         * @param exceptionAnnotation - Value of a field annotated with this annotation will not be set to target object
         */
        private fun setValueExcept(
            targetField: Field,
            target: Any,
            sourceValue: Any,
            exceptionAnnotation: Class<out Annotation>
        ) {
            if (targetField.isAnnotationPresent(exceptionAnnotation).not()) {
                setValue(targetField, target, sourceValue)
            }
        }

        /**
         * Set [sourceValue] to corresponding [targetField] of [target] object
         *
         * @param sourceValue - Source value
         * @param targetField - Target field that corresponds field of source value
         * @param target - Target object that will receive the source value
         */
        private fun setValue(targetField: Field, target: Any, sourceValue: Any) {
            targetField.isAccessible = true
            targetField.set(target, sourceValue)
        }

        /**
         * Get value of [sourceField] from [source] object that is being copied
         *
         * @param sourceField - Field of a source object
         * @param source - Source object
         */
        private fun getValue(sourceField: Field, source: Any): Any? {
            sourceField.isAccessible = true
            return sourceField.get(source)
        }

        // Reset static fieled to default values
        private fun resetStaticFileds(){
            sItemId = -1
            with(sFieldSet.iterator()){
                forEach { _ ->
                    remove()
                }
            }
            with(sPairedFieldSet.iterator()){
                forEach { _ ->
                    remove()
                }
            }
        }
    }
}