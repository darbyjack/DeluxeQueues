package me.glaremasters.deluxequeues.utils

import net.md_5.bungee.api.ChatColor


fun String.color(): String = ChatColor.translateAlternateColorCodes('&', this)
