package me.glaremasters.deluxequeues.acf

import ch.jalu.configme.SettingsManager
import co.aikar.commands.BungeeCommandManager
import me.glaremasters.deluxequeues.DeluxeQueues
import me.glaremasters.deluxequeues.commands.CommandDeluxeQueues
import me.glaremasters.deluxequeues.queues.QueueHandler
import java.io.File
import java.io.IOException
import java.util.*

class ACFHandler(private val deluxeQueues: DeluxeQueues, private val commandManager: BungeeCommandManager) {


    fun load() {
        commandManager.enableUnstableAPI("help")

        loadLang()
        loadDI()

        commandManager.commandReplacements.addReplacement("dq", "queue|dq|queues")

        loadCommands()
    }

    private fun loadDI() {
        commandManager.registerDependency(QueueHandler::class.java, deluxeQueues.queueHandler)
        commandManager.registerDependency(SettingsManager::class.java, deluxeQueues.conf())
    }

    private fun loadCommands() {
        commandManager.registerCommand(CommandDeluxeQueues())
    }

    fun loadLang() {
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
}
