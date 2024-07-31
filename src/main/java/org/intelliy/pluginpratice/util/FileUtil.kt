package org.intelliy.pluginpratice.util

import java.io.File

fun createFolder(folderPath: String) {
    if (!File(folderPath).exists()) {
        File(folderPath).mkdir()
    }
}