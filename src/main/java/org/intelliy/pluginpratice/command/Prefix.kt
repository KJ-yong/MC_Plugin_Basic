package org.intelliy.pluginpratice.command

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.intelliy.pluginpratice.constant.*
import org.intelliy.pluginpratice.invgui.PrefixGuiListener
import org.intelliy.pluginpratice.util.*

object Prefix : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        log("showPrefix label = $label, commandName = ${command.name}")
        return when (command.name) {
            PREFIX -> showPrefix(sender, args)
            ADD_PREFIX -> addPrefix(sender, args)
            REMOVE_PREFIX -> removePrefix(sender, args)
            else -> false
        }
    }

    private fun showPrefix(sender: CommandSender, args: Array<out String>?): Boolean {
        if (sender !is Player) return false
        val targetPlayerInfo = PlayerManager.onLinePlayerList.find { it.player.uniqueId == sender.uniqueId } ?: return false
        PrefixGuiListener.open(1, targetPlayerInfo)
        return true
    }

    private fun addPrefix(sender: CommandSender, args: Array<out String>?): Boolean {
        if (args == null || args.size <= 1 || args.size > 2) {
            sender.sendMessage("사용 방법 : /$ADD_PREFIX_KO 대상플레이어닉네임 추가할칭호")
        } else {
            val target = args[0]
            val addPrefix = args[1]
            val targetPlayerInfo = PlayerManager.findPlayer(target)
            if (targetPlayerInfo == null) sender.sendMessage("대상 플레이어가 접속 중이지 않거나 존재하지 않습니다.")
            else {
                targetPlayerInfo.addPrefix(addPrefix)
                sender.sendMessage("${target}의 칭호에 ${addPrefix}&r이(가) 추가되었습니다.".toColoredComponent())
                targetPlayerInfo.player.sendMessage("당신은 ${addPrefix}&r 칭호를 획득하셨습니다.".toColoredComponent())
            }
        }
        return true
    }

    private fun removePrefix(sender: CommandSender, args: Array<out String>?): Boolean {
        if (args == null || args.size <= 1 || args.size > 2) {
            sender.sendMessage("사용 방법 : /$REMOVE_PREFIX_KO 대상플레이어닉네임 제거할칭호")
        } else {
            val target = args[0]
            val targetPlayerInfo = PlayerManager.findPlayer(target)
            if (targetPlayerInfo == null) sender.sendMessage("대상 플레이어가 접속 중이지 않거나 존재하지 않습니다.")
            else {
                val removedPrefix = targetPlayerInfo.prefixList.find { it.toNonColorString() == args[1] }
                if (removedPrefix == null) sender.sendMessage("대상 플레이어가 해당 칭호를 갖고 있지 않습니다.")
                else {
                    targetPlayerInfo.removePrefix(removedPrefix)
                    sender.sendMessage("${targetPlayerInfo.getColoredNick()}님의 $removedPrefix 칭호를 제거하였습니다.".toColoredComponent())
                }
            }
        }
        return true
    }
}