package org.intelliy.pluginpratice.entity

import org.bukkit.entity.Player
import org.intelliy.pluginpratice.util.toColoredComponent

data class PlayerInfo(
    val player: Player
) {
    companion object {
        const val NAME = "name"
        const val DISPLAY_NICK = "displayNick"
        const val CURRENT_PREFIX = "current_prefix"
        const val money = "money"
        const val PREFIX_LIST = "prefix_list"
    }
    var displayNick: String = player.name
    var currentPrefix: String = ""
    var prefixList = mutableListOf<String>()
    var money: String = "0"
        set(value) {
            try {
                value.toInt()
                field = value
            } catch (_: Exception) {}
        }

    fun getSettingNick(): String = if (currentPrefix.isNotBlank()) "${currentPrefix}&r $displayNick&r" else "$displayNick&r"
}
