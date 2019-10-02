package com.mecofarid.searchablespinnersample

var id: Int = -1

    fun main(args: Array<String>) {

    }

private fun sumUntil(int: Int){
    if (int > 0) {

        id++
        println("meco lis "+ id)
        sumUntil(int - 1)
    }
}


