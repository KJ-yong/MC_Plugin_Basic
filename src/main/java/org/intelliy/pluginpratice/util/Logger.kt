package org.intelliy.pluginpratice.util

import org.intelliy.pluginpratice.Main

fun log(message: String) {
    Main.instance.logger.info(message)
}