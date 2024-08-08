package org.intelliy.pluginpratice.entity

import kotlinx.coroutines.launch
import org.bukkit.entity.Player
import org.intelliy.pluginpratice.Main
import org.intelliy.pluginpratice.repository.PlayerInfoSaveRepository
import org.intelliy.pluginpratice.util.toNonColorString

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
    var money: Int = 0

    fun getColoredNick(): String = if (currentPrefix.isNotBlank()) "${currentPrefix}&r $displayNick&r" else "$displayNick&r"
    fun getNonColorNick(): String = displayNick.toNonColorString()
    fun addMoney(addMoney: Int) {
        money += addMoney
        Main.ioScope.launch {
            PlayerInfoSaveRepository.saveMoney(player.uniqueId, money)
        }
    }

    fun minusMoney(minusMoney: Int) {
        money -= minusMoney
        Main.ioScope.launch {
            PlayerInfoSaveRepository.saveMoney(player.uniqueId, money)
        }
    }
}
