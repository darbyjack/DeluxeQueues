package me.glaremasters.deluxequeues.base

import me.glaremasters.deluxequeues.DeluxeQueues
import me.glaremasters.deluxequeues.DeluxeQueuesPlugin
import net.md_5.bungee.api.ProxyServer
import java.util.logging.Logger

/**
 * Defines an aspect of the plugin that requires access to spigot api
 */
internal interface Addon
{

    val plugin: DeluxeQueuesPlugin

    val server: ProxyServer
        get() = plugin.proxy

    val logger: Logger
        get() = plugin.logger

    val queues: DeluxeQueues
        get() = checkNotNull(plugin.deluxeQueues)
        {
            "deluxe qeueues is unavailable"
        }

}