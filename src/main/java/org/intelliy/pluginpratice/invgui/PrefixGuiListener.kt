package org.intelliy.pluginpratice.invgui

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.intelliy.pluginpratice.constant.PREFIX_INVENTORY
import org.intelliy.pluginpratice.entity.PlayerInfo
import org.intelliy.pluginpratice.util.PlayerManager
import org.intelliy.pluginpratice.util.log
import org.intelliy.pluginpratice.util.toColorString
import org.intelliy.pluginpratice.util.toColoredComponent

class PrefixGuiListener : PageInventoryListener(PREFIX_INVENTORY) {
    override fun clickItem(position: Int, event: InventoryClickEvent) {
        val player = event.whoClicked
        if (player !is Player) return
        val targetPlayerInfo = PlayerManager.onLinePlayerList.find { it.player.uniqueId == player.uniqueId } ?: return
        targetPlayerInfo.changePrefix(targetPlayerInfo.prefixList[position])
        targetPlayerInfo.player.sendMessage("당신의 칭호가 ${targetPlayerInfo.currentPrefix.toColorString()}${"&r".toColorString()}(으)로 변경되었습니다.")
    }

    override fun clickPrevItem(currentPage: Int, event: InventoryClickEvent) {
        log("clickPrevItem")
        val player = event.whoClicked
        if (player !is Player) return
        val targetPlayerInfo = PlayerManager.onLinePlayerList.find { it.player.uniqueId == player.uniqueId } ?: return
        if (currentPage <= 1) return
        open(currentPage - 1, targetPlayerInfo)
    }

    override fun clickNextItem(currentPage: Int, event: InventoryClickEvent) {
        log("clickNextItem")
        val player = event.whoClicked
        if (player !is Player) return
        val targetPlayerInfo = PlayerManager.onLinePlayerList.find { it.player.uniqueId == player.uniqueId } ?: return
        if (targetPlayerInfo.prefixList.size <= currentPage * PAGE_ITEM_SIZE) return
        open(currentPage + 1, targetPlayerInfo)
    }

    companion object {
        fun open(page: Int, playerInfo: PlayerInfo) {
            val prefixGui = PrefixGuiListener().apply {
                setData(playerInfo.prefixList.map {
                    ItemStack(Material.BOOK).apply {
                        val meta = itemMeta
                        meta.displayName(it.toColoredComponent())
                        itemMeta = meta
                    }
                }, page)
            }
            prefixGui.openPageInventory(playerInfo.player)
        }
    }
}