package org.intelliy.pluginpratice.command

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.intelliy.pluginpratice.constant.OPEN_INVENTORY_KO
import org.intelliy.pluginpratice.util.PlayerManager

object OpenInventory : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (sender !is Player) return false
        if (args == null || args.size != 1) sender.sendMessage("사용 방법 : /$OPEN_INVENTORY_KO 대상플레이어")
        else {
            val targetPlayer = PlayerManager.findPlayer(args[0])
            if (targetPlayer == null) {
                sender.sendMessage("대상을 찾을 수 없습니다.")
            } else {
                sender.openInventory(targetPlayer.player.inventory)
            }
        }
        return true
    }
}