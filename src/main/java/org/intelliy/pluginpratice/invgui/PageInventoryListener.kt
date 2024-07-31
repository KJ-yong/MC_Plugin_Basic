package org.intelliy.pluginpratice.invgui

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.intelliy.pluginpratice.constant.NEXT_PAGE
import org.intelliy.pluginpratice.constant.NOTHING
import org.intelliy.pluginpratice.constant.PREV_PAGE
import org.intelliy.pluginpratice.util.log
import org.intelliy.pluginpratice.util.toColoredComponent
import org.intelliy.pluginpratice.util.toPlainString

abstract class PageInventoryListener(private val inventoryName: String): Listener {
    companion object {
        val PAGE_ITEM_SIZE = 45
    }
    private val inventory = Bukkit.createInventory(null, 54, inventoryName.toColoredComponent()).apply {
        setItem(45, ItemStack(Material.PAPER).apply {
            val meta = itemMeta
            meta.displayName(PREV_PAGE.toColoredComponent())
            itemMeta = meta
        })
        setItem(53, ItemStack(Material.PAPER).apply {
            val meta = itemMeta
            meta.displayName(NEXT_PAGE.toColoredComponent())
            itemMeta = meta
        })
        for (i in 46..48) {
            setItem(i, ItemStack(Material.GRAY_STAINED_GLASS_PANE).apply {
                val meta = itemMeta
                meta.displayName(NOTHING.toColoredComponent())
                itemMeta = meta
            })
        }
        for (i in 50..52) {
            setItem(i, ItemStack(Material.GRAY_STAINED_GLASS_PANE).apply {
                val meta = itemMeta
                meta.displayName(NOTHING.toColoredComponent())
                itemMeta = meta
            })
        }
    }

    fun setData(itemList: List<ItemStack>, page: Int) {
        inventory.apply {
            val offset = (page - 1) * PAGE_ITEM_SIZE
            val max = if (itemList.size >= PAGE_ITEM_SIZE + offset) PAGE_ITEM_SIZE else itemList.size - offset
            for (i in offset until offset + max) {
                setItem(i - offset, itemList[i])
            }
            setItem(49, ItemStack(Material.PAPER).apply {
                val meta = itemMeta
                meta.displayName(toPage(page.toString()).toColoredComponent())
                itemMeta = meta
            })
        }
    }

    @EventHandler
    fun onItemClick(event: InventoryClickEvent) {
        if (event.view.title() != inventoryName.toColoredComponent()) {
            log("inventory click not.")
            return
        }
        val currentPage = event.clickedInventory?.getItem(49)?.let { item ->
            item.displayName().toPlainString().split("-")[1].toInt()
        }
        event.isCancelled = true
        currentPage?.let { page ->
            if (event.slot == 45) {
                clickPrevItem(page, event)
            } else if (event.slot == 53) {
                clickNextItem(page, event)
            } else if (event.slot < 36) {
                clickItem(((page - 1) * PAGE_ITEM_SIZE) + event.slot, event)
            }
        }
    }

    fun openPageInventory(player: Player) {
        player.openInventory(inventory)
    }

    private fun toPage(page: String) = "&6&l-$page-"

    abstract fun clickItem(position: Int, event: InventoryClickEvent)
    abstract fun clickPrevItem(currentPage: Int, event: InventoryClickEvent)
    abstract fun clickNextItem(currentPage: Int, event: InventoryClickEvent)
}