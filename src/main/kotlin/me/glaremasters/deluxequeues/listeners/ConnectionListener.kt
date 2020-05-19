package me.glaremasters.deluxequeues.listeners

import me.glaremasters.deluxequeues.DeluxeQueues
import me.glaremasters.deluxequeues.configuration.sections.ConfigOptions
import net.md_5.bungee.api.event.PlayerDisconnectEvent
import net.md_5.bungee.api.event.ServerConnectEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler
import net.md_5.bungee.event.EventPriority

/**
 * Created by Glare
 * Date: 7/14/2019
 * Time: 12:13 AM
 */
class ConnectionListener(deluxeQueues: DeluxeQueues) : Listener {

    private val queueHandler = deluxeQueues.queueHandler
    private val settingsManager = deluxeQueues.conf()

    @EventHandler(priority = EventPriority.LOW)
    fun onJoin(event: ServerConnectEvent) {
        // Don't go further if they are just joining the proxy
        if (event.reason == ServerConnectEvent.Reason.JOIN_PROXY) {
            return
        }
        // Get the server in the event
        val server = event.target
        // Get the player in the event
        val player = event.player
        // Create a boolean for bypass with staff
        val bypass = player.hasPermission(settingsManager.getProperty(ConfigOptions.STAFF_PERMISSION))
        if (bypass) {
            return
        }
        // Get the queue
        val queue = queueHandler.getQueue(server) ?: return
        // If the queue contains the player move them
        if (queue.queue.contains(player) && event.reason != ServerConnectEvent.Reason.COMMAND) {
            event.isCancelled = false
            queue.removePlayer(player)
            return
        }
        // They are not in it, check if they can be added
        // Cancel event and add them to the queue
        if (!queue.queue.contains(player) && queue.canAddPlayer()) {
            event.isCancelled = true
            queue.addPlayer(player)
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    fun onLeave(event: PlayerDisconnectEvent) {
        // Remove player from all queues
        queueHandler.clearPlayer(event.player)
    }

}
