package org.intelliy.pluginpratice.command

import kotlinx.coroutines.launch
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.intelliy.pluginpratice.Main
import org.intelliy.pluginpratice.constant.CHANGE_NICKNAME_KO
import org.intelliy.pluginpratice.entity.PlayerInfo
import org.intelliy.pluginpratice.repository.PlayerInfoSaveRepository
import org.intelliy.pluginpratice.util.PlayerManager
import org.intelliy.pluginpratice.util.toColorString
import org.intelliy.pluginpratice.util.toColoredComponent

object ChangeNickName: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (args == null || args.size <= 1 || args.size > 2) {
            sender.sendMessage("사용 방법 : /$CHANGE_NICKNAME_KO 대상플레이어닉네임 바꿀닉네임")
        } else {
            val target = args[0]
            val setNick = args[1]
            val targetPlayerInfo = PlayerManager.onLinePlayerList.find { it.player.name == target }
            if (targetPlayerInfo == null) sender.sendMessage("대상 플레이어가 접속 중이지 않거나 존재하지 않습니다.")
            else {
                targetPlayerInfo.changeDisplayNick(setNick)
                sender.sendMessage("${target}의 닉네임이 ${setNick.toColorString()}${"&r".toColorString()}(으)로 변경되었습니다.")
                targetPlayerInfo.player.sendMessage("당신의 닉네임이 ${setNick.toColorString()}${"&r".toColorString()}(으)로 변경되었습니다.")
            }
        }
        return true
    }
}