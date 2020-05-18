package me.glaremasters.deluxequeues.commands

import ch.jalu.configme.SettingsManager
import co.aikar.commands.BaseCommand
import co.aikar.commands.CommandHelp
import co.aikar.commands.CommandIssuer
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Dependency
import co.aikar.commands.annotation.Description
import co.aikar.commands.annotation.HelpCommand
import co.aikar.commands.annotation.Subcommand
import me.glaremasters.deluxequeues.DeluxeQueues
import me.glaremasters.deluxequeues.configuration.sections.ConfigOptions
import me.glaremasters.deluxequeues.messages.Messages
import me.glaremasters.deluxequeues.queues.QueueHandler
import me.glaremasters.deluxequeues.utils.ADMIN_PERM
import me.glaremasters.deluxequeues.utils.BASE_PERM
import net.md_5.bungee.api.connection.ProxiedPlayer

@CommandAlias("%dq")
class CommandDeluxeQueues : BaseCommand() {
    @Dependency private lateinit var queueHandler: QueueHandler
    @Dependency private lateinit var settingsManager: SettingsManager
    @Dependency private lateinit var deluxeQueues: DeluxeQueues

    @Subcommand("reload")
    @Description("{@@descriptions.reload}")
    @CommandPermission(ADMIN_PERM)
    fun execute(issuer: CommandIssuer) {
        settingsManager.reload()

        // Update the notify methods for each queue
        queueHandler.getQueues().forEach { queue ->
            queue.notifyMethod = settingsManager.getProperty(ConfigOptions.INFORM_METHOD)
        }
        
        issuer.sendInfo(Messages.RELOAD__SUCCESS)
    }

    @Subcommand("leave")
    @Description("{@@descriptions.command-leave}")
    @CommandPermission(BASE_PERM + "leave")
    fun leave(player: ProxiedPlayer) {
        queueHandler.clearPlayer(player)
        currentCommandIssuer.sendInfo(Messages.QUEUES__LEFT)
    }

    @HelpCommand
    @CommandPermission(BASE_PERM + "help")
    @Description("{@@descriptions.help}")
    fun help(help: CommandHelp) {
        help.showHelp()
    }
}