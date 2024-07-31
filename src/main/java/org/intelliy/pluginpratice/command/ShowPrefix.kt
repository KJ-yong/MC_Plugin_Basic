package org.intelliy.pluginpratice.command

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.intelliy.pluginpratice.invgui.PrefixGuiListener
import org.intelliy.pluginpratice.util.PlayerManager

object ShowPrefix : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (sender !is Player) return false
        val targetPlayerInfo = PlayerManager.onLinePlayerList.find { it.player.uniqueId == sender.uniqueId } ?: return false
        PrefixGuiListener.open(1, targetPlayerInfo)
        return true
    }
}