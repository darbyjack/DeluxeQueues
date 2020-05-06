package me.glaremasters.deluxequeues.queues

import ch.jalu.configme.SettingsManager
import me.glaremasters.deluxequeues.DeluxeQueues
import me.glaremasters.deluxequeues.configuration.sections.ConfigOptions
import net.md_5.bungee.api.config.ServerInfo
import net.md_5.bungee.api.connection.ProxiedPlayer
import java.util.function.Consumer

/**
 * Created by Glare
 * Date: 7/13/2019
 * Time: 10:31 PM
 */
class QueueHandler(val settingsManager: SettingsManager, val deluxeQueues: DeluxeQueues) {
    private val queues = mutableSetOf<DeluxeQueue>()
    private val servers = mutableSetOf<ServerInfo>()

    /**
     * Create a new queue for a server
     * @param queue the new queue
     */
    fun createQueue(queue: DeluxeQueue) {
        if (queues.add(queue)) {
            servers.add(queue.server)
        }
    }

    /**
     * Delete a queue if it exists
     * @param queue the queue to check
     */
    fun deleteQueue(queue: DeluxeQueue) {
        if (queues.remove(queue)) {
            servers.remove(queue.server)
        }
    }

    /**
     * Check if a queue exists
     * @param queue the queue to check
     * @return queue object
     */
    fun getQueue(queue: DeluxeQueue): DeluxeQueue? {
        return queues.firstOrNull { it == queue }
    }

    /**
     * Get a queue from it's server
     * @param server the server to get the queue from
     * @return the queue
     */
    fun getQueue(server: ServerInfo): DeluxeQueue? {
        return queues.firstOrNull { it.server == server }
    }

    /**
     * Remove a player from all queues
     * @param player the player to remove
     */
    fun clearPlayer(player: ProxiedPlayer) {
        queues.forEach {
            it.queue.removeIf { queuePlayer ->
                queuePlayer.player == player
            }
        }
    }

    /**
     * Check if a server has a queue
     * @param server the server to check
     * @return if the server has a queue or not
     */
    fun checkForQueue(server: ServerInfo): Boolean {
        return server in servers
    }

    /**
     * Enable all the queues on the server
     */
    fun enableQueues() {
        settingsManager.getProperty(ConfigOptions.QUEUE_SERVERS).forEach(Consumer { s: String ->
            try {
                val split = s.split(";".toRegex())
                val queue = DeluxeQueue(deluxeQueues, deluxeQueues.proxy.getServerInfo(split[0]), split[1].toInt(), split[2].toInt())
                createQueue(queue)
            } catch (ex: Exception) {
                deluxeQueues.logger.warning("It seems like one of your servers was configured invalidly in the config.")
            }
        })
    }

    fun getQueues(): Set<DeluxeQueue> {
        return queues
    }

    fun getServers(): Set<ServerInfo> {
        return servers
    }

}
