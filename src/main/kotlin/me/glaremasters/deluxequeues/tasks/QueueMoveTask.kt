package me.glaremasters.deluxequeues.tasks

import me.glaremasters.deluxequeues.queues.DeluxeQueue
import net.md_5.bungee.api.config.ServerInfo
import net.md_5.bungee.api.event.ServerConnectEvent

/**
 * Created by Glare
 * Date: 7/13/2019
 * Time: 10:47 PM
 */
class QueueMoveTask(
        private val queue: DeluxeQueue,
        private val server: ServerInfo
) : Runnable {

    override fun run() {
        // Make sure the queue isn't empty
        if (queue.players.isEmpty()) {
            return
        }

        // Persist the notification to the user
        queue.players.forEach(queue::notifyPlayer)

        // Check if the max amount of players on the server are the max slots
        if (queue.server.players.size >= queue.maxSlots) {
            return
        }
        // Get the player next in line
        val player = queue.players.firstOrNull() ?: return
        // Move the player to that server
        player.connect(server, ServerConnectEvent.Reason.PLUGIN)
    }

}
