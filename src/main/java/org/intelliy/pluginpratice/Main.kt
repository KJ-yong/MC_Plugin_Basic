package org.intelliy.pluginpratice

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import org.bukkit.command.CommandExecutor
import org.bukkit.command.TabCompleter
import org.bukkit.plugin.java.JavaPlugin
import org.intelliy.pluginpratice.command.*
import org.intelliy.pluginpratice.constant.*
import org.intelliy.pluginpratice.event.PlayerJoinEventListener
import org.intelliy.pluginpratice.invgui.PrefixGuiListener
import org.intelliy.pluginpratice.util.PlayerManager
import org.intelliy.pluginpratice.util.createFolder
import org.intelliy.pluginpratice.util.toNonColorString

class Main : JavaPlugin() {
    companion object {
        lateinit var instance: Main
        lateinit var ioScope: CoroutineScope
    }
    override fun onEnable() {
        instance = this
        ioScope = CoroutineScope(Dispatchers.IO)
        PlayerManager.loadOnlinePlayer()
        // Plugin startup logic
        registerJoinEvent()
        registerCommands()
        registerCommandsTab()
        createFolder(dataFolder.path)
        createFolder(USER_INFO_PATH)
        logger.info("test")

    }

    override fun onDisable() {
        // Plugin shutdown logic
        ioScope.cancel()
    }

    private fun registerJoinEvent() {
        server.pluginManager.run {
            registerEvents(PlayerJoinEventListener, this@Main)
            registerEvents(PrefixGuiListener(), this@Main)
        }
    }

    private fun registerCommands() {
        registerCommand(CHANGE_NICKNAME, ChangeNickName)
        registerCommand(ADD_PREFIX, Prefix)
        registerCommand(PREFIX, Prefix)
        registerCommand(REMOVE_PREFIX, Prefix)
        registerCommand(TELEPORT, Teleport)
        registerCommand(OPEN_INVENTORY, OpenInventory)
        registerCommand(FLY, Fly)
        registerCommand(MONEY, Money)
        registerCommand(SEND_MONEY, Money)
        registerCommand(ADD_MONEY, Money)
        registerCommand(MINUS_MONEY, Money)
        registerCommand(SET_MONEY, Money)
    }

    private fun registerCommandsTab() {
        registerSameTabList(listOf(ADD_PREFIX, OPEN_INVENTORY, MONEY, SEND_MONEY, ADD_MONEY, MINUS_MONEY, SET_MONEY)) { _, _, _, args ->
            val tabList = mutableListOf<String>()
            if (args.size == 1) {
                tabList.addAll(PlayerManager.onLinePlayerList.map { it.getNonColorNick() })
                tabList.addAll(PlayerManager.onLinePlayerList.map { it.player.name })
            }
            tabList
        }
        registerTab(REMOVE_PREFIX) { sender, command, label, args ->
            val tabList = mutableListOf<String>()
            if (args.size == 1) {
                tabList.addAll(PlayerManager.onLinePlayerList.map { it.getNonColorNick() })
                tabList.addAll(PlayerManager.onLinePlayerList.map { it.player.name })
            } else if (args.size == 2) {
                val targetPlayer = PlayerManager.findPlayer(args[0])
                if (targetPlayer != null) {
                    tabList.addAll(targetPlayer.prefixList.map { it.toNonColorString() })
                }
            }
            tabList
        }
        registerTab(TELEPORT) { sender, command, label, args ->
            val tabList = mutableListOf<String>()
            if (args.size == 1) {
                tabList.addAll(PlayerManager.onLinePlayerList.map { it.getNonColorNick() })
                tabList.addAll(PlayerManager.onLinePlayerList.map { it.player.name })
            } else if (args.size == 2) {
                if (PlayerManager.findPlayer(args[0]) != null) {
                    tabList.addAll(PlayerManager.onLinePlayerList.map { it.getNonColorNick() })
                    tabList.addAll(PlayerManager.onLinePlayerList.map { it.player.name })
                }
            }
            tabList
        }
    }

    private fun registerCommand(command: String, executor: CommandExecutor) {
        server.getPluginCommand(command)?.setExecutor(executor)
    }

    private fun registerTab(command: String, tabCompleter: TabCompleter) {
        server.getPluginCommand(command)?.tabCompleter = tabCompleter
    }

    private fun registerSameTabList(commands: List<String>, tabCompleter: TabCompleter) {
        commands.forEach { command ->
            registerTab(command, tabCompleter)
        }
    }
}