package com.mecofarid.searchablemultispinner.model

import org.jetbrains.annotations.TestOnly

open class ItemSpinner {

    var itemSpinnerLevel: Int = 0
    var itemSpinnerId: Long = -1L
    var itemSpinnerParentId = -1L

    @TestOnly
    override fun equals(other: Any?): Boolean {
        other?.let {
            return  (other as ItemSpinner).itemSpinnerId == this.itemSpinnerId
        }

        return false
    }
}