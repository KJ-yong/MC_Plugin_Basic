package org.intelliy.pluginpratice.command

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.permissions.Permission
import org.intelliy.pluginpratice.constant.MONEY
import org.intelliy.pluginpratice.constant.SEND_MONEY
import org.intelliy.pluginpratice.util.PlayerManager
import org.intelliy.pluginpratice.util.isDigitOnly
import org.intelliy.pluginpratice.util.toColoredComponent

object Money : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        return when(command.name) {
            MONEY -> showMoney(sender, args)
            SEND_MONEY -> sendMoney(sender, args)
            else -> false
        }
    }

    private fun showMoney(sender: CommandSender, args: Array<out String>?): Boolean {
        if (args == null || args.isEmpty()) {
            if (sender !is Player) return false
            val playerInfo = PlayerManager.onLinePlayerList.find { it.player.uniqueId == sender.uniqueId } ?: return false
            playerInfo.player.sendMessage("보유 금액 : ${playerInfo.money}".toColoredComponent())
        } else {
            val targetPlayer = PlayerManager.findPlayer(args[0])
            if (targetPlayer == null) sender.sendMessage("대상 플레이어를 찾을 수 없습니다.".toColoredComponent())
            else {
                sender.sendMessage("${targetPlayer.getColoredNick()}님의 보유 금액 : ${targetPlayer.money}원".toColoredComponent())
            }
        }
        return true
    }

    private fun sendMoney(sender: CommandSender, args: Array<out String>?): Boolean {
        if (sender !is Player) return false
        val senderInfo = PlayerManager.findPlayer(sender.uniqueId) ?: return false
        if (args == null || args.size != 2) {
            sender.sendMessage("사용 방법 : /$SEND_MONEY 대상플레이어 보낼금액")
            return false
        }
        val targetPlayer = PlayerManager.findPlayer(args[0])
        if (targetPlayer == null || !args[1].isDigitOnly()) {
            sender.sendMessage("사용 방법 : /$SEND_MONEY 대상플레이어 보낼금액")
            return false
        }
        val sendMoney = args[1].toInt()
        if (senderInfo.money < sendMoney) {
            sender.sendMessage("돈이 부족합니다.")
            return false
        }
        senderInfo.minusMoney(sendMoney)
        targetPlayer.addMoney(sendMoney)
        targetPlayer.player.sendMessage("${senderInfo.getColoredNick()}님께서 ${sendMoney}원을 보내셨습니다.")
        sender.sendMessage("${targetPlayer.getColoredNick()}님에게 ${sendMoney}원을 보냈습니다.")
        return true
    }
}