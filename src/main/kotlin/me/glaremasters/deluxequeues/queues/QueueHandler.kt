package me.glaremasters.deluxequeues.queues

import ch.jalu.configme.SettingsManager
import me.glaremasters.deluxequeues.DeluxeQueues
import me.glaremasters.deluxequeues.configuration.sections.ConfigOptions
import net.md_5.bungee.api.config.ServerInfo
import net.md_5.bungee.api.connection.ProxiedPlayer

/**
 * Created by Glare
 * Date: 7/13/2019
 * Time: 10:31 PM
 */
class QueueHandler(private val settingsManager: SettingsManager, private val deluxeQueues: DeluxeQueues) {
    private val queues = mutableSetOf<DeluxeQueue>()

    /**
     * Create a new queue for a server
     * @param queue the new queue
     */
    private fun createQueue(queue: DeluxeQueue) {
        queues.add(queue)
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
            it.removePlayer(player)
        }
    }

    fun enableQueues() {
        val servers = settingsManager.getProperty(ConfigOptions.QUEUE_SERVERS)
        servers.forEach { server ->
            try {
                val split = server.split(";".toRegex())
                val queue = DeluxeQueue(deluxeQueues, deluxeQueues.proxy.getServerInfo(split[0]), split[1].toInt(), split[2].toInt())
                createQueue(queue)
            } catch (ex: Exception) {
                deluxeQueues.logger.warning("It seems like one of your servers was configured invalidly in the config.")
            }
        }
    }

    fun getQueues(): Set<DeluxeQueue> {
        return queues
    }

}
