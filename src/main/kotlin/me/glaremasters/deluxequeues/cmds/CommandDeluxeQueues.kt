package me.glaremasters.deluxequeues.cmds

import co.aikar.commands.BaseCommand
import co.aikar.commands.CommandHelp
import co.aikar.commands.CommandIssuer
import co.aikar.commands.annotation.*
import me.glaremasters.deluxequeues.DeluxeQueuesPlugin
import me.glaremasters.deluxequeues.base.Addon
import me.glaremasters.deluxequeues.exte.ADMIN_PERM
import me.glaremasters.deluxequeues.exte.BASE_PERM
import me.glaremasters.deluxequeues.messages.Messages
import net.md_5.bungee.api.connection.ProxiedPlayer

@CommandAlias("%dq")
internal class CommandDeluxeQueues(override val plugin: DeluxeQueuesPlugin) : BaseCommand(), Addon {

    @Subcommand("reload")
    @Description("{@@descriptions.reload}")
    @CommandPermission(ADMIN_PERM)
    fun reload(issuer: CommandIssuer) {
        queues.conf().reload()
        plugin.startQueues()
        issuer.sendInfo(Messages.RELOAD__SUCCESS)
    }

    @Subcommand("leave")
    @Description("{@@descriptions.command-leave}")
    @CommandPermission(BASE_PERM + "leave")
    fun leave(player: ProxiedPlayer) {
        plugin.queueHandler.clearPlayer(player)
        currentCommandIssuer.sendInfo(Messages.QUEUES__LEFT)
    }

    @HelpCommand
    @CommandPermission(BASE_PERM + "help")
    @Description("{@@descriptions.help}")
    fun help(help: CommandHelp) {
        help.showHelp()
    }
}