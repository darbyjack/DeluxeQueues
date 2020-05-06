package me.glaremasters.deluxequeues.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.CommandHelp
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Description
import co.aikar.commands.annotation.HelpCommand
import me.glaremasters.deluxequeues.utils.BASE_PERM

@CommandAlias("%dq")
class CommandHelp : BaseCommand() {

    @HelpCommand
    @CommandPermission(BASE_PERM + "help")
    @Description("{@@descriptions.help}")
    fun execute(help: CommandHelp) {
        help.showHelp()
    }
}
