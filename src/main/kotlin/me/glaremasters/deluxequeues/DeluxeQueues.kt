package me.glaremasters.deluxequeues

import ch.jalu.configme.SettingsManager
import co.aikar.commands.BungeeCommandManager
import me.glaremasters.deluxequeues.acf.ACFHandler
import me.glaremasters.deluxequeues.configuration.DeluxeQueuesConfiguration
import me.glaremasters.deluxequeues.listeners.ConnectionListener
import me.glaremasters.deluxequeues.queues.QueueHandler
import me.glaremasters.deluxequeues.updater.UpdateChecker
import net.md_5.bungee.api.plugin.Plugin
import java.io.File

class DeluxeQueues : Plugin() {
    private lateinit var commandManager: BungeeCommandManager
    private lateinit var acfHandler: ACFHandler

    private var conf = null as? SettingsManager?

    lateinit var queueHandler: QueueHandler
        private set

    override fun onEnable() {
        saveFile("config.yml")
        saveFile("languages/en-US.yml")

        loadConf()
        UpdateChecker.runCheck(this, conf())
        startQueues()

        commandManager = BungeeCommandManager(this)
        acfHandler = ACFHandler(this, commandManager)
        acfHandler.load()

        proxy.pluginManager.registerListener(this, ConnectionListener(this))
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }

    private fun loadConf() {
        val file = dataFolder.resolve("config.yml")

        if (!file.exists()) {
            file.parentFile.mkdirs()
            file.createNewFile()
        }

        this.conf = DeluxeQueuesConfiguration(file)
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
        queueHandler = QueueHandler(conf(), this)
        queueHandler.enableQueues()
    }

    fun conf(): SettingsManager {
        return checkNotNull(conf)
    }
}
