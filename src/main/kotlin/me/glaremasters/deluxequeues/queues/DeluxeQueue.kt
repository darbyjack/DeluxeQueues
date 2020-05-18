package me.glaremasters.deluxequeues.queues

import ch.jalu.configme.SettingsManager
import me.glaremasters.deluxequeues.DeluxeQueues
import me.glaremasters.deluxequeues.configuration.sections.ConfigOptions
import me.glaremasters.deluxequeues.tasks.QueueMoveTask
import me.glaremasters.deluxequeues.utils.color
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.config.ServerInfo
import net.md_5.bungee.api.connection.ProxiedPlayer
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by Glare
 * Date: 7/13/2019
 * Time: 10:30 PM
 */
class DeluxeQueue(
        private val deluxeQueues: DeluxeQueues,
        val server: ServerInfo,
        private val playersRequired: Int,
        val maxSlots: Int
) {
    val queue: Deque<QueuePlayer> = LinkedList()

    private val settingsManager = deluxeQueues.settingsHandler.settingsManager
    private val delayLength = settingsManager.getProperty(ConfigOptions.DELAY_LENGTH)
    var notifyMethod = settingsManager.getProperty(ConfigOptions.INFORM_METHOD)

    init {
        deluxeQueues.proxy.scheduler.schedule(deluxeQueues, QueueMoveTask(this, server), 0, delayLength.toLong(), TimeUnit.SECONDS)
    }

    /**
     * Add a player to a queue
     * @param player the player to add
     */
    fun addPlayer(player: ProxiedPlayer) {
        if (getFromProxy(player) == null) {
            val qp = QueuePlayer(player, false)
            if (player.hasPermission(settingsManager.getProperty(ConfigOptions.DONATOR_PERMISSION))) {
                queue.addFirst(qp)
            } else {
                queue.add(qp)
            }
        }
    }

    fun removePlayer(player: QueuePlayer) {
        queue.remove(player)
    }

    fun getFromProxy(player: ProxiedPlayer): QueuePlayer? {
        return queue.firstOrNull { it.player == player }
    }

    /**
     * Add in a check to make sure the player can be added to the queue
     * @return added or not
     */
    fun canAddPlayer(): Boolean {
        return server.players.size >= playersRequired
    }

    /**
     * Get the position of a player in a queue
     * @param player the player to check
     * @return their position
     */
    fun getQueuePos(player: QueuePlayer): Int {
        return queue.indexOf(player)
    }

    /**
     * Notify the player that they are in the queue
     * @param player the player to check
     */
    fun notifyPlayer(player: QueuePlayer) {
        val actionbar = settingsManager.getProperty(ConfigOptions.ACTIONBAR_DESIGN)
        val message = settingsManager.getProperty(ConfigOptions.TEXT_DESIGN)
        val titleTop = settingsManager.getProperty(ConfigOptions.TITLE_HEADER)
        val titleBottom = settingsManager.getProperty(ConfigOptions.TITLE_FOOTER)

        fun applyPlaceholders(message: String): String {
            return message
                    .replace("{pos}", (getQueuePos(player) + 1).toString())
                    .replace("{total}", queue.size.toString())
        }

        when (notifyMethod.toLowerCase()) {
            "actionbar" -> {
                player.player.sendMessage(ChatMessageType.ACTION_BAR, TextComponent(applyPlaceholders(actionbar).color()))
            }
            "text" -> {
                player.player.sendMessage(TextComponent(applyPlaceholders(message).color()))
            }
            "title" -> {
                val title = deluxeQueues.proxy.createTitle()
                title.title(TextComponent(titleTop.color()))
                val bottom = applyPlaceholders(titleBottom).color()
                title.subTitle(TextComponent(bottom))
                player.player.sendTitle(title)
            }
        }
    }

    override fun toString(): String {
        return """
        |DeluxeQueue(deluxeQueues=$deluxeQueues, queue=${queue}, 
        |server=$server, delayLength=$delayLength, playersRequired=$playersRequired, 
        |maxSlots=$maxSlots, settingsManager=$settingsManager, notifyMethod=$notifyMethod)
        |""".trimMargin()
    }
}
