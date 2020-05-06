package me.glaremasters.deluxequeues.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.*
import me.glaremasters.deluxequeues.messages.Messages
import me.glaremasters.deluxequeues.queues.QueueHandler
import me.glaremasters.deluxequeues.utils.BASE_PERM
import net.md_5.bungee.api.connection.ProxiedPlayer

@CommandAlias("%dq")
class CommandLeave : BaseCommand() {

    @Dependency
    private lateinit var queueHandler: QueueHandler

    @Subcommand("leave")
    @Description("{@@descriptions.command-leave}")
    @CommandPermission(BASE_PERM + "leave")
    fun execute(player: ProxiedPlayer) {
        queueHandler.clearPlayer(player)
        currentCommandIssuer.sendInfo(Messages.QUEUES__LEFT)
    }
}
