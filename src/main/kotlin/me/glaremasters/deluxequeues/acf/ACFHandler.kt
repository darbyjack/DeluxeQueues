package me.glaremasters.deluxequeues.acf

import ch.jalu.configme.SettingsManager
import co.aikar.commands.BungeeCommandManager
import me.glaremasters.deluxequeues.DeluxeQueues
import me.glaremasters.deluxequeues.commands.CommandHelp
import me.glaremasters.deluxequeues.commands.CommandLeave
import me.glaremasters.deluxequeues.commands.CommandReload
import me.glaremasters.deluxequeues.queues.QueueHandler
import java.io.File
import java.io.IOException
import java.util.*

class ACFHandler(private val deluxeQueues: DeluxeQueues, commandManager: BungeeCommandManager) {

    fun registerDependencyInjection(commandManager: BungeeCommandManager) {
        commandManager.registerDependency(QueueHandler::class.java, deluxeQueues.queueHandler)
        commandManager.registerDependency(SettingsManager::class.java, deluxeQueues.settingsHandler.settingsManager)
    }

    fun registerCommandReplacements(commandManager: BungeeCommandManager) {
        commandManager.commandReplacements.addReplacement("dq", "queue|dq|queues")
    }

    fun registerCommands(commandManager: BungeeCommandManager) {
        commandManager.registerCommand(CommandHelp())
        commandManager.registerCommand(CommandLeave())
        commandManager.registerCommand(CommandReload())
    }

    /**
     * Load all the language files for the plugin
     * @param commandManager command manager
     */
    fun registerLanguages(deluxeQueues: DeluxeQueues, commandManager: BungeeCommandManager) {
        try {
            val languageFolder = File(deluxeQueues.dataFolder, "languages")
            val files = languageFolder.listFiles() ?: return
            for (file in files) {
                if (file.isFile && file.extension == "yml") {
                    val updatedName = file.nameWithoutExtension
                    commandManager.addSupportedLanguage(Locale.forLanguageTag(updatedName))
                    commandManager.locales.loadYamlLanguageFile(File(languageFolder, file.name), Locale.forLanguageTag(updatedName))
                }
            }
            commandManager.locales.defaultLocale = Locale.forLanguageTag("en-US")
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    init {
        @Suppress("DEPRECATION")
        commandManager.enableUnstableAPI("help")
        registerLanguages(deluxeQueues, commandManager)
        registerDependencyInjection(commandManager)
        registerCommandReplacements(commandManager)
        registerCommands(commandManager)
    }
}
