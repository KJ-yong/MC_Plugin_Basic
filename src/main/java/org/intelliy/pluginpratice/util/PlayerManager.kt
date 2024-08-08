package org.intelliy.pluginpratice.util

import kotlinx.coroutines.launch
import org.bukkit.entity.Player
import org.intelliy.pluginpratice.Main
import org.intelliy.pluginpratice.entity.PlayerInfo
import org.intelliy.pluginpratice.repository.PlayerInfoSaveRepository
import java.util.*

object PlayerManager {
    val onLinePlayerList = mutableListOf<PlayerInfo>()

    fun loadOnlinePlayer() {
        Main.ioScope.launch {
            Main.instance.server.onlinePlayers.forEach { player ->
                val info = PlayerInfoSaveRepository.loadPlayerInfo(player)
                playerSet(info)
                onLinePlayerList.add(info)
            }
        }
    }

    fun joinPlayer(player: Player) {
        Main.ioScope.launch {
            val info = PlayerInfoSaveRepository.loadPlayerInfo(player)
            playerSet(info)
            onLinePlayerList.add(info)
        }
    }

    fun exitPlayer(player: Player) {
        onLinePlayerList.removeIf {
            it.player.uniqueId == player.uniqueId
        }
    }

    private fun playerSet(playerInfo: PlayerInfo) {
        playerInfo.player.displayName((if (playerInfo.currentPrefix.isNotBlank()) "${playerInfo.currentPrefix}&r ${playerInfo.displayNick}" else playerInfo.displayNick).toColoredComponent())
        playerInfo.player.playerListName((if (playerInfo.currentPrefix.isNotBlank()) "${playerInfo.currentPrefix}&r ${playerInfo.displayNick}" else playerInfo.displayNick).toColoredComponent())
    }

    fun findPlayer(name: String): PlayerInfo? {
        val nameTarget = onLinePlayerList.find { it.player.name == name }
        if (nameTarget != null) return nameTarget
        return onLinePlayerList.find { it.displayNick.toNonColorString() == name }
    }

    fun findPlayer(uuid: UUID): PlayerInfo? = onLinePlayerList.find { it.player.uniqueId == uuid }
}