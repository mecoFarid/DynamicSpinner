package com.mecofarid.searchablemultispinner.utils

import com.mecofarid.searchablemultispinner.model.ItemSpinner
import java.io.InputStream

fun getJson(classLoader: ClassLoader?, fileName: String): String? = streamToString(classLoader?.getResourceAsStream(fileName))

private fun streamToString(inputStream: InputStream?): String? {
    return inputStream?.bufferedReader().use { it?.readText() }
}