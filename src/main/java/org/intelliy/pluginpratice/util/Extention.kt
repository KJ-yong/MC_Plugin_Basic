package org.intelliy.pluginpratice.util

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.ChatColor

fun String.toColoredComponent(): Component = Component.text(ChatColor.translateAlternateColorCodes('&', "$this&r"))
fun String.toColorString(): String = ChatColor.translateAlternateColorCodes('&', "$this&r")
fun String.isDigitOnly(): Boolean = Regex("[0-9]+").matches(this)
fun Component.toPlainString() = PlainTextComponentSerializer.plainText().serialize(this)