package me.glaremasters.deluxequeues.queues

import me.glaremasters.deluxequeues.DeluxeQueues
import me.glaremasters.deluxequeues.configuration.sections.ConfigOptions
import me.glaremasters.deluxequeues.tasks.QueueMoveTask
import me.glaremasters.deluxequeues.utils.color
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.config.ServerInfo
import net.md_5.bungee.api.connection.ProxiedPlayer
import java.util.Deque
import java.util.LinkedList
import java.util.concurrent.TimeUnit

/**
 * Created by Glare
 * Date: 7/13/2019
 * Time: 10:30 PM
 */
data class DeluxeQueue(
        private val deluxeQueues: DeluxeQueues,
        val server: ServerInfo,
        private val playersRequired: Int,
        val maxSlots: Int
) {
    val players: Deque<ProxiedPlayer> = LinkedList()

    private val settingsManager = deluxeQueues.conf()
    private var delayLength: Int = -1 //Effectively lateinit
    lateinit var notifyMethod: String

    init {
        loadConfiguration()
        deluxeQueues.proxy.scheduler.schedule(deluxeQueues, QueueMoveTask(this, server), 0, delayLength.toLong(), TimeUnit.SECONDS)
    }

    fun loadConfiguration() {
        delayLength = settingsManager.getProperty(ConfigOptions.DELAY_LENGTH)
        notifyMethod = settingsManager.getProperty(ConfigOptions.INFORM_METHOD)
    }

    fun addPlayer(player: ProxiedPlayer) {
        if (player.hasPermission(settingsManager.getProperty(ConfigOptions.DONATOR_PERMISSION))) {
            players.addFirst(player)
        } else {
            players.add(player)
        }
    }

    fun removePlayer(player: ProxiedPlayer) {
        players.remove(player)
    }

    /**
     * Add in a check to make sure the player can be added to the queue
     * @return added or not
     */
    fun canAddPlayer(): Boolean {
        return server.players.size >= playersRequired
    }

    private fun getQueuePosition(player: ProxiedPlayer): Int {
        return players.indexOf(player)
    }

    /**
     * Notify the player that they are in the queue
     * @param player the player to check
     */
    fun notifyPlayer(player: ProxiedPlayer) {
        val actionbar = settingsManager.getProperty(ConfigOptions.ACTIONBAR_DESIGN)
        val message = settingsManager.getProperty(ConfigOptions.TEXT_DESIGN)
        val titleTop = settingsManager.getProperty(ConfigOptions.TITLE_HEADER)
        val titleBottom = settingsManager.getProperty(ConfigOptions.TITLE_FOOTER)

        fun applyPlaceholders(message: String): String {
            return message
                    .replace("{pos}", (getQueuePosition(player) + 1).toString())
                    .replace("{total}", players.size.toString())
        }

        when (notifyMethod.toLowerCase()) {
            "actionbar" -> {
                player.sendMessage(ChatMessageType.ACTION_BAR, TextComponent(applyPlaceholders(actionbar).color()))
            }
            "text" -> {
                player.sendMessage(TextComponent(applyPlaceholders(message).color()))
            }
            "title" -> {
                val title = deluxeQueues.proxy.createTitle()
                val top = applyPlaceholders(titleTop).color()
                title.title(TextComponent(top))
                val bottom = applyPlaceholders(titleBottom).color()
                title.subTitle(TextComponent(bottom))
                player.sendTitle(title)
            }
        }
    }
}
