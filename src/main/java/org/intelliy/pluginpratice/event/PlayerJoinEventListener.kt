package org.intelliy.pluginpratice.event

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerKickEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.intelliy.pluginpratice.util.PlayerManager

object PlayerJoinEventListener: Listener {
    @EventHandler
    fun onPlayerJoinEvent(event: PlayerJoinEvent) {
        PlayerManager.joinPlayer(event.player)
    }

    @EventHandler
    fun onPlayerQuitEvent(event: PlayerQuitEvent) {
        PlayerManager.exitPlayer(event.player)
    }

    @EventHandler
    fun onPlayerKickEvent(event: PlayerKickEvent) {
        PlayerManager.exitPlayer(event.player)
    }
}