package org.intelliy.pluginpratice.command

import kotlinx.coroutines.launch
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.intelliy.pluginpratice.Main
import org.intelliy.pluginpratice.constant.CHANGE_PREFIX_KO
import org.intelliy.pluginpratice.repository.PlayerInfoSaveRepository
import org.intelliy.pluginpratice.util.PlayerManager
import org.intelliy.pluginpratice.util.toColorString

object AddPrefix : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (args == null || args.size <= 1 || args.size > 2) {
            sender.sendMessage("사용 방법 : /$CHANGE_PREFIX_KO 대상플레이어닉네임 추가할칭호")
        } else {
            val target = args[0]
            val addPrefix = args[1]
            val targetPlayerInfo = PlayerManager.onLinePlayerList.find { it.player.name == target }
            if (targetPlayerInfo == null) sender.sendMessage("대상 플레이어가 접속 중이지 않거나 존재하지 않습니다.")
            else {
//                val nickComponent = "$setPrefix&r ${targetPlayerInfo.displayNick}".toColoredComponent()
//                targetPlayerInfo.player.displayName(nickComponent)
//                targetPlayerInfo.player.playerListName(nickComponent)
                targetPlayerInfo.currentPrefix = addPrefix
                targetPlayerInfo.prefixList.add(addPrefix)
                Main.ioScope.launch {
                    PlayerInfoSaveRepository.addPrefix(targetPlayerInfo.player.uniqueId, addPrefix)
                }
                sender.sendMessage("${target}의 칭호에 ${addPrefix.toColorString()}${"&r".toColorString()}이(가) 추가되었습니다.")
                targetPlayerInfo.player.sendMessage("당신은 ${addPrefix.toColorString()}${"&r".toColorString()} 칭호를 획득하셨습니다.")
            }
        }
        return true
    }
}