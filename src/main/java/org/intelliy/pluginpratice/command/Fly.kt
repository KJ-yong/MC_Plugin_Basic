package org.intelliy.pluginpratice.command

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.intelliy.pluginpratice.constant.FLY
import org.intelliy.pluginpratice.util.PlayerManager

object Fly : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (args == null || args.isEmpty()) {
            if (sender !is Player) sender.sendMessage("사용방법 : /$FLY 대상플레이어")
            else sender.isFlying = !sender.isFlying
        } else {
            val targetPlayer = PlayerManager.findPlayer(args[0])
            if (targetPlayer == null) sender.sendMessage("대상 플레이어를 찾을 수 없습니다.")
            else {
                targetPlayer.player.isFlying = !targetPlayer.player.isFlying
            }
        }
        return true
    }
}