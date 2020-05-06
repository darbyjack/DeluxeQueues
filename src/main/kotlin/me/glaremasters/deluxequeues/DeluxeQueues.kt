package me.glaremasters.deluxequeues

import co.aikar.commands.BungeeCommandManager
import me.glaremasters.deluxequeues.acf.ACFHandler
import me.glaremasters.deluxequeues.configuration.SettingsHandler
import me.glaremasters.deluxequeues.listeners.ConnectionListener
import me.glaremasters.deluxequeues.queues.QueueHandler
import me.glaremasters.deluxequeues.updater.UpdateChecker
import net.md_5.bungee.api.plugin.Plugin
import java.io.File

class DeluxeQueues : Plugin() {
    private lateinit var commandManager: BungeeCommandManager
    private lateinit var acfHandler: ACFHandler

    lateinit var settingsHandler: SettingsHandler
        private set

    lateinit var queueHandler: QueueHandler
        private set

    override fun onEnable() {
        saveFile("config.yml")
        saveFile("languages/en-US.yml")

        settingsHandler = SettingsHandler(this)
        UpdateChecker.runCheck(this, settingsHandler.settingsManager)
        startQueues()
        commandManager = BungeeCommandManager(this)
        acfHandler = ACFHandler(this, commandManager)
        proxy.pluginManager.registerListener(this, ConnectionListener(this))
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }

    /**
     * Create a file to be used in the plugin
     * @param name the name of the file
     */
    private fun saveFile(name: String) {
        if (!dataFolder.exists()) {
            dataFolder.mkdir()
        }

        val languageFolder = File(dataFolder, "languages")
        if (!languageFolder.exists()) {
            languageFolder.mkdirs()
        }
        val file = File(dataFolder, name)
        if (!file.exists()) {
            getResourceAsStream(name).use {
                file.outputStream().use { out ->
                    it.copyTo(out)
                }
            }
        }
    }

    fun startQueues() {
        queueHandler = QueueHandler(settingsHandler.settingsManager, this)
        queueHandler.enableQueues()
    }
}
