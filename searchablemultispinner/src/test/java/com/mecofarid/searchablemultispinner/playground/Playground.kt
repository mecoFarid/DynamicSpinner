package com.mecofarid.searchablemultispinner.playground

import org.junit.Test

class Playground {

    @Test
    fun TestPlayground() {

        PlaygroundDataModel::class.java.constructors.forEach {
            println("meoc constr 1 $it")
        }

        Class.forName(PlaygroundDataModel::class.java.name).getConstructor().newInstance()
    }



}