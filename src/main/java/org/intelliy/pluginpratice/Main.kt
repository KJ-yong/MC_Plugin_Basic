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
        registerCommand(TELEPORT, Teleport)
        registerCommand(OPEN_INVENTORY, OpenInventory)
        registerCommand(FLY, Fly)
        registerCommand(MONEY, Money)
        registerCommand(SEND_MONEY, Money)
//        server.getPluginCommand(TELEPORT)?.tabCompleter = TabCompleter { sender, command, label, args ->
//            log("sender = $sender, command = $command, label = $label, args = ${args.joinToString("/")}")
//            val list = mutableListOf<String>()
//            list.addAll(args)
//            list.addAll(PlayerManager.onLinePlayerList.map { it.getNonColorNick() })
//            list
//        }
    }

    private fun registerCommandsTab() {
        registerTab(ADD_PREFIX) { sender, command, label, args ->
            val tabList = mutableListOf<String>()
            if (args.size == 1) {
                tabList.addAll(PlayerManager.onLinePlayerList.map { it.displayNick.toNonColorString() })
                tabList.addAll(PlayerManager.onLinePlayerList.map { it.player.name })
            }
            tabList
        }
        registerTab(TELEPORT) { sender, command, label, args ->
            val tabList = mutableListOf<String>()
            if (args.size == 1) {
                tabList.addAll(PlayerManager.onLinePlayerList.map { it.displayNick.toNonColorString() })
                tabList.addAll(PlayerManager.onLinePlayerList.map { it.player.name })
            } else if (args.size == 2) {
                if (PlayerManager.findPlayer(args[0]) != null) {
                    tabList.addAll(PlayerManager.onLinePlayerList.map { it.displayNick.toNonColorString() })
                    tabList.addAll(PlayerManager.onLinePlayerList.map { it.player.name })
                }
            }
            tabList
        }
        registerTab(SEND_MONEY) { sender, command, label, args ->
            val tabList = mutableListOf<String>()
            if (args.size == 1) {
                tabList.addAll(PlayerManager.onLinePlayerList.map { it.displayNick.toNonColorString() })
                tabList.addAll(PlayerManager.onLinePlayerList.map { it.player.name })
            }
            tabList
        }
        registerTab(OPEN_INVENTORY) { sender, command, label, args ->
            val tabList = mutableListOf<String>()
            if (args.size == 1) {
                tabList.addAll(PlayerManager.onLinePlayerList.map { it.displayNick.toNonColorString() })
                tabList.addAll(PlayerManager.onLinePlayerList.map { it.player.name })
            }
            tabList
        }
        registerTab(MONEY) { sender, command, label, args ->
            val tabList = mutableListOf<String>()
            if (args.size == 1) {
                tabList.addAll(PlayerManager.onLinePlayerList.map { it.displayNick.toNonColorString() })
                tabList.addAll(PlayerManager.onLinePlayerList.map { it.player.name })
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
}