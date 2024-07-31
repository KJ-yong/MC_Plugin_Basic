package org.intelliy.pluginpratice.repository

import org.bukkit.entity.Player
import org.intelliy.pluginpratice.datasource.PlayerInfoSaveLocalDataSource
import org.intelliy.pluginpratice.entity.PlayerInfo
import java.util.UUID

object PlayerInfoSaveRepository {
    suspend fun saveNickName(uuid: UUID, nick: String) {
        PlayerInfoSaveLocalDataSource.saveNick(uuid.toString(), nick)
    }

    suspend fun savePrefix(uuid: UUID, prefix: String) {
        PlayerInfoSaveLocalDataSource.saveCurrentPrefix(uuid.toString(), prefix)
    }

    suspend fun loadPlayerInfo(player: Player): PlayerInfo {
        var info = PlayerInfoSaveLocalDataSource.readPlayerInfo(player)
        if (info == null) {
            info = PlayerInfo(player)
            PlayerInfoSaveLocalDataSource.savePlayerInfo(info)
        }
        return info
    }

    suspend fun addPrefix(uuid: UUID, prefix: String) {
        PlayerInfoSaveLocalDataSource.addPrefix(uuid.toString(), prefix)
    }
}