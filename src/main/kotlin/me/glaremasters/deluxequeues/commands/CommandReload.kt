package me.glaremasters.deluxequeues.commands

import ch.jalu.configme.SettingsManager
import co.aikar.commands.BaseCommand
import co.aikar.commands.CommandIssuer
import co.aikar.commands.annotation.*
import me.glaremasters.deluxequeues.DeluxeQueues
import me.glaremasters.deluxequeues.messages.Messages
import me.glaremasters.deluxequeues.utils.ADMIN_PERM

@CommandAlias("%dq")
class CommandReload : BaseCommand() {
    @Dependency
    private lateinit var settingsManager: SettingsManager

    @Dependency
    private lateinit var deluxeQueues: DeluxeQueues

    @Subcommand("reload")
    @Description("{@@descriptions.reload}")
    @CommandPermission(ADMIN_PERM)
    fun execute(issuer: CommandIssuer) {
        settingsManager.reload()
        deluxeQueues.startQueues()
        issuer.sendInfo(Messages.RELOAD__SUCCESS)
    }
}
