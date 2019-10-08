package com.mecofarid.searchablemultispinner.model

import org.jetbrains.annotations.TestOnly

open class ItemSpinner {

    var level: Int = 0
    var id: Long = -1L
    var parentId = -1L

    @TestOnly
    override fun equals(other: Any?): Boolean {
        other?.let {
            return  (other as ItemSpinner).id == this.id
        }

        return false
    }
}