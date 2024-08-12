package org.intelliy.pluginpratice.entity

import kotlinx.coroutines.launch
import org.bukkit.entity.Player
import org.intelliy.pluginpratice.Main
import org.intelliy.pluginpratice.repository.PlayerInfoSaveRepository
import org.intelliy.pluginpratice.util.toColoredComponent
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

    fun changeDisplayNick(displayNick: String) {
        this.displayNick = displayNick
        setNick()
        Main.ioScope.launch {
            PlayerInfoSaveRepository.saveNickName(player.uniqueId, displayNick)
        }
    }

    fun changePrefix(prefix: String) {
        currentPrefix = prefix
        setNick()
        Main.ioScope.launch {
            PlayerInfoSaveRepository.saveCurrentPrefix(player.uniqueId, currentPrefix)
        }
    }

    fun addPrefix(prefix: String) {
        prefixList.add(prefix)
        Main.ioScope.launch {
            PlayerInfoSaveRepository.addPrefix(player.uniqueId, prefix)
        }
    }

    fun removePrefix(prefix: String) {
        var isCurrentNeedReset = false
        prefixList.remove(prefix)
        if (currentPrefix == prefix) {
            isCurrentNeedReset = true
            currentPrefix = ""
            setNick()
        }
        Main.ioScope.launch {
            PlayerInfoSaveRepository.removePrefix(player.uniqueId, prefix)
            if (isCurrentNeedReset) PlayerInfoSaveRepository.saveCurrentPrefix(player.uniqueId, "")
        }
    }

    fun setNick() {
        player.displayName(getColoredNick().toColoredComponent())
        player.playerListName(getColoredNick().toColoredComponent())
    }

    fun addMoney(addMoney: Int) {
        money += addMoney
        Main.ioScope.launch {
            PlayerInfoSaveRepository.saveMoney(player.uniqueId, money)
        }
    }

    fun minusMoney(minusMoney: Int) {
        if (money >= minusMoney) money -= minusMoney
        else money = 0
        Main.ioScope.launch {
            PlayerInfoSaveRepository.saveMoney(player.uniqueId, money)
        }
    }

    fun settingMoney(setMoney: Int) {
        money = setMoney
        Main.ioScope.launch {
            PlayerInfoSaveRepository.saveMoney(player.uniqueId, money)
        }
    }
}
