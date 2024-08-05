package org.intelliy.pluginpratice.command

import org.bukkit.Location
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.intelliy.pluginpratice.constant.TELEPORT
import org.intelliy.pluginpratice.util.PlayerManager
import org.intelliy.pluginpratice.util.isDigitOnly

object Teleport : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (args == null || args.isEmpty()) {
            sender.sendMessage("사용 방법 : /$TELEPORT 대상플레이어")
        } else {
            val targetPlayer = PlayerManager.findPlayer(args[0])
            if (targetPlayer != null) {
                if (args.size == 1) {
                    if (sender !is Player) return false
                    sender.teleport(targetPlayer.player.location)
                    sender.sendMessage("${targetPlayer.getSettingNick()}에게 이동하였습니다.")
                } else if (args.size == 2) {
                    val targetPlayer2 = PlayerManager.findPlayer(args[1])
                    if (targetPlayer2 != null) {
                        targetPlayer.player.teleport(targetPlayer2.player.location)
                        sender.sendMessage("${targetPlayer.getSettingNick()}을(를) ${targetPlayer2.getSettingNick()}에게 이동하였습니다.")
                    } else {
                        sender.sendMessage("대상을 찾을 수 없습니다.")
                    }
                } else if (args.size == 4) {
                    if (args[1].isDigitOnly() && args[2].isDigitOnly() && args[3].isDigitOnly()) {
                        if (sender is Player) {
                            targetPlayer.player.teleport(Location(sender.world, args[1].toDouble(), args[2].toDouble(), args[3].toDouble()))
                        } else {
                            targetPlayer.player.teleport(Location(targetPlayer.player.world, args[1].toDouble(), args[2].toDouble(), args[3].toDouble()))
                        }
                    } else {
                        sender.sendMessage("사용방법 : /$TELEPORT 대상플레이어 x좌표 y좌표 z좌표")
                    }
                }
            } else {
                if (args.size == 3) {
                    if (args[0].isDigitOnly() && args[1].isDigitOnly() && args[2].isDigitOnly()) {
                        if (sender is Player) sender.teleport(Location(sender.world, args[0].toDouble(), args[1].toDouble(), args[2].toDouble()))
                    } else {
                        sender.sendMessage("사용방법 : /$TELEPORT x좌표 y좌표 z좌표")
                    }
                } else {
                    sender.sendMessage("$TELEPORT 명령어의 인수가 잘못되었습니다.\n/$TELEPORT 대상플레이어\n/${TELEPORT} 대상플레이어1 대상플레이어2" +
                            "\n/$TELEPORT 대상플레이어 x좌표 y좌표 z좌표\n/$TELEPORT x좌표 y좌표 z좌표")
                }
            }
        }
        return true
    }
}