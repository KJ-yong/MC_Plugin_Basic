package org.intelliy.pluginpratice.datasource

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.intelliy.pluginpratice.Main
import org.intelliy.pluginpratice.constant.USER_INFO_PATH
import org.intelliy.pluginpratice.entity.PlayerInfo
import org.intelliy.pluginpratice.util.log
import java.io.File

object PlayerInfoSaveLocalDataSource {
    suspend fun readPlayerInfo(player: Player): PlayerInfo? {
        val userInfo = File("$USER_INFO_PATH${player.uniqueId}.yml")
        return withContext(Dispatchers.IO) {
            try {
                if (userInfo.exists()) {
                    val playerInfo = PlayerInfo(player)
                    val infoYml = YamlConfiguration.loadConfiguration(userInfo)
                    playerInfo.displayNick = infoYml.get(PlayerInfo.DISPLAY_NICK) as String
                    playerInfo.currentPrefix = infoYml.get(PlayerInfo.CURRENT_PREFIX) as String
                    playerInfo.money = infoYml.getInt(PlayerInfo.money, 0)
                    playerInfo.prefixList.addAll(infoYml.getStringList(PlayerInfo.PREFIX_LIST))
                    playerInfo
                } else {
                    log("Player info is not exist. UUID = ${player.uniqueId}")
                    null
                }
            } catch (e: Exception) {
                log("Read player info fail. UUID = ${player.uniqueId}. exception = ${e.message}")
                null
            }
        }
    }

    fun savePlayerInfo(playerInfo: PlayerInfo) {
        val infoFile = File("$USER_INFO_PATH${playerInfo.player.uniqueId}.yml")
        if (!infoFile.exists()) {
            infoFile.createNewFile()
        }
        val infoYml = YamlConfiguration.loadConfiguration(infoFile)
        infoYml.set(PlayerInfo.NAME, playerInfo.player.name)
        infoYml.set(PlayerInfo.DISPLAY_NICK, playerInfo.displayNick)
        infoYml.set(PlayerInfo.CURRENT_PREFIX, playerInfo.currentPrefix)
        infoYml.set(PlayerInfo.money, playerInfo.money)
        infoYml.save(infoFile)
    }

    suspend fun saveNick(uuid: String, nick: String) {
        val playerInfoFile = File("$USER_INFO_PATH$uuid.yml")
        withContext(Dispatchers.IO) {
            try {
                if (!playerInfoFile.exists()) return@withContext
                val infoYml = YamlConfiguration.loadConfiguration(playerInfoFile)
                infoYml.set(PlayerInfo.DISPLAY_NICK, nick)
                infoYml.save(playerInfoFile)
            } catch (e: Exception) {
                log("saveNick exception : ${e.message}")
            }
        }
    }

    suspend fun saveCurrentPrefix(uuid: String, prefix: String) {
        val playerInfoFile = File("$USER_INFO_PATH$uuid.yml")
        withContext(Dispatchers.IO) {
            try {
                if (!playerInfoFile.exists()) return@withContext
                val infoYml = YamlConfiguration.loadConfiguration(playerInfoFile)
                infoYml.set(PlayerInfo.CURRENT_PREFIX, prefix)
                infoYml.save(playerInfoFile)
            } catch (e: Exception) {
                log("saveCurrentPrefix exception : ${e.message}")
            }
        }
    }

    suspend fun addPrefix(uuid: String, prefix: String) {
        val playerInfoFile = File("$USER_INFO_PATH$uuid.yml")
        withContext(Dispatchers.IO) {
            try {
                if (!playerInfoFile.exists()) return@withContext
                val infoYml = YamlConfiguration.loadConfiguration(playerInfoFile)
                val prefixList = infoYml.getStringList(PlayerInfo.PREFIX_LIST)
                prefixList.add(prefix)
                infoYml.set(PlayerInfo.PREFIX_LIST, prefixList)
                infoYml.save(playerInfoFile)
            } catch (e: Exception) {
                log("addPrefix exception : ${e.message}")
            }
        }
    }

    suspend fun saveMoney(uuid: String, money: Int) {
        val playerInfoFile = File("$USER_INFO_PATH$uuid.yml")
        try {
            if (!playerInfoFile.exists()) return
            val infoYml = YamlConfiguration.loadConfiguration(playerInfoFile)
            infoYml.set(PlayerInfo.money, money)
            infoYml.save(playerInfoFile)
        } catch (e: Exception) {
            log("saveMoney exception : ${e.message}")
        }
    }
}