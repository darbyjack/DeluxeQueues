package queues

import ch.jalu.configme.SettingsManager
import co.aikar.commands.ACFBungeeUtil
import me.glaremasters.deluxequeues.DeluxeQueuesPlugin
import me.glaremasters.deluxequeues.queues.QueuePlayer
import me.glaremasters.deluxequeues.tasks.QueueMoveTask
import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.config.ServerInfo
import net.md_5.bungee.api.connection.ProxiedPlayer
import java.util.LinkedList
import java.util.concurrent.TimeUnit

class DeluxeQueue(val deluxeQueuesPlugin: DeluxeQueuesPlugin, val server: ServerInfo, playersRequired: Int, maxSlots: Int) {
    val queue = LinkedList<QueuePlayer?>()
    val delayLength: Int
    val playersRequired: Int
    val maxSlots: Int
    val settingsManager: SettingsManager
    val notifyMethod: String

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

    fun removePlayer(player: QueuePlayer?) {
        queue.remove(player)
    }

    fun getFromProxy(player: ProxiedPlayer): QueuePlayer? {
        return queue.stream().filter { q: QueuePlayer? -> q!!.player === player }.findFirst().orElse(null)
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
    fun getQueuePos(player: QueuePlayer?): Int {
        return queue.indexOf(player)
    }

    /**
     * Notify the player that they are in the queue
     * @param player the player to check
     */
    fun notifyPlayer(player: QueuePlayer) {
        var actionbar = settingsManager.getProperty<String>(ConfigOptions.ACTIONBAR_DESIGN)
        var message = settingsManager.getProperty<String>(ConfigOptions.TEXT_DESIGN)
        val title_top = settingsManager.getProperty<String>(ConfigOptions.TITLE_HEADER)
        var title_bottom = settingsManager.getProperty<String>(ConfigOptions.TITLE_FOOTER)
        when (notifyMethod.toLowerCase()) {
            "actionbar" -> {
                actionbar = actionbar.replace("{pos}", (getQueuePos(player) + 1).toString())
                actionbar = actionbar.replace("{total}", queue.size.toString())
                player.player.sendMessage(ChatMessageType.ACTION_BAR, TextComponent(ACFBungeeUtil.color(actionbar)))
            }
            "text" -> {
                message = message.replace("{pos}", (getQueuePos(player) + 1).toString())
                message = message.replace("{total}", queue.size.toString())
                player.player.sendMessage(TextComponent(ACFBungeeUtil.color(message)))
            }
            "title" -> {
                val title = deluxeQueuesPlugin.proxy.createTitle()
                title.title(TextComponent(ACFBungeeUtil.color(title_top)))
                title_bottom = title_bottom.replace("{pos}", (getQueuePos(player) + 1).toString())
                title_bottom = title_bottom.replace("{total}", queue.size.toString())
                title.subTitle(TextComponent(ACFBungeeUtil.color(title_bottom)))
                player.player.sendTitle(title)
            }
        }
    }

    override fun toString(): String {
        return "DeluxeQueue(deluxeQueues=" + deluxeQueuesPlugin + ", queue=" + queue + ", server=" + server + ", delayLength=" + delayLength + ", playersRequired=" + playersRequired + ", maxSlots=" + maxSlots + ", settingsManager=" + settingsManager + ", notifyMethod=" + notifyMethod + ")"
    }

    init {
        settingsManager = deluxeQueuesPlugin.getSettingsHandler().getSettingsManager()
        delayLength = settingsManager.getProperty(ConfigOptions.DELAY_LENGTH)
        this.playersRequired = playersRequired
        this.maxSlots = maxSlots
        notifyMethod = settingsManager.getProperty(ConfigOptions.INFORM_METHOD)
        deluxeQueuesPlugin.proxy.scheduler.schedule(deluxeQueuesPlugin, QueueMoveTask(this, server), 0, delayLength.toLong(), TimeUnit.SECONDS)
    }
}