package org.intelliy.pluginpratice

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import org.bukkit.plugin.java.JavaPlugin
import org.intelliy.pluginpratice.command.ChangeNickName
import org.intelliy.pluginpratice.command.AddPrefix
import org.intelliy.pluginpratice.command.ShowPrefix
import org.intelliy.pluginpratice.constant.CHANGE_NICKNAME
import org.intelliy.pluginpratice.constant.CHANGE_PREFIX
import org.intelliy.pluginpratice.constant.PREFIX
import org.intelliy.pluginpratice.constant.USER_INFO_PATH
import org.intelliy.pluginpratice.event.PlayerJoinEventListener
import org.intelliy.pluginpratice.invgui.PageInventoryListener
import org.intelliy.pluginpratice.invgui.PrefixGuiListener
import org.intelliy.pluginpratice.util.createFolder

class Main : JavaPlugin() {
    companion object {
        lateinit var instance: Main
        lateinit var ioScope: CoroutineScope
    }
    override fun onEnable() {
        instance = this
        ioScope = CoroutineScope(Dispatchers.IO)
        // Plugin startup logic
        registerJoinEvent()
        registerChangeNickNameCommand()
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

    private fun registerChangeNickNameCommand() {
        server.getPluginCommand(CHANGE_NICKNAME)?.setExecutor(ChangeNickName)
        server.getPluginCommand(CHANGE_PREFIX)?.setExecutor(AddPrefix)
        server.getPluginCommand(PREFIX)?.setExecutor(ShowPrefix)
    }
}