package me.glaremasters.deluxequeues

import ch.jalu.configme.SettingsManager
import co.aikar.commands.BungeeCommandManager
import me.glaremasters.deluxequeues.base.State
import me.glaremasters.deluxequeues.cmds.CommandDeluxeQueues
import me.glaremasters.deluxequeues.conf.DeluxeQueuesConfiguration
import me.glaremasters.deluxequeues.conf.sections.PluginOptions
import me.glaremasters.deluxequeues.util.JarFileWalker
import me.glaremasters.deluxequeues.util.UpdateChecker
import java.util.Locale
import java.util.logging.Level

class DeluxeQueues internal constructor(internal val plugin: DeluxeQueuesPlugin) : State {

    private var conf = null as? SettingsManager?
    private val cmds = BungeeCommandManager(plugin)

    override fun load() {
        loadConf()
        loadCmds()

        saveLang()
        loadLang()
    }

    override fun kill() {
    }

    private fun loadConf() {
        val file = plugin.dataFolder.resolve("config.yml")

        if (!file.exists()) {
            file.parentFile.mkdirs()
            file.createNewFile()
        }

        this.conf = DeluxeQueuesConfiguration(file)
    }

    private fun saveLang()
    {
        JarFileWalker.walk("/languages")
        { path, stream ->

            if (stream == null)
            {
                return@walk // do nothing if the stream couldn't be opened
            }

            val file = plugin.dataFolder.resolve(path.toString().drop(1)).absoluteFile
            if (file.exists())
            {
                return@walk // language file was already created
            }

            file.parentFile.mkdirs()
            file.createNewFile()

            file.outputStream().use()
            {
                stream.copyTo(it)
                stream.close()
            }
        }
    }

    private fun loadCmds() {
        cmds.locales.defaultLocale = Locale.forLanguageTag(conf().getProperty(PluginOptions.LANGUAGE) ?: "en-US")

        cmds.commandReplacements.addReplacement("dq", "queue|dq|queues")

        cmds.registerCommand(CommandDeluxeQueues(plugin))
    }

    fun loadLang()
    {
        plugin.dataFolder.resolve("languages").listFiles()?.filter()
        {
            it.extension.equals("yml", true)
        }?.forEach()
        {
            val locale = Locale.forLanguageTag(it.nameWithoutExtension)

            cmds.addSupportedLanguage(locale)
            cmds.locales.loadYamlLanguageFile(it, locale)
        }
    }

    private fun checkForUpdates()
    {
        UpdateChecker.check(plugin, 987)
        {
            when (it)
            {
                is UpdateChecker.UpdateResult.UP_TO_DATE ->
                {
                    plugin.logger.info(it.message)
                }
                is UpdateChecker.UpdateResult.UNRELEASED ->
                {
                    plugin.logger.warning(it.message)
                }
                is UpdateChecker.UpdateResult.NEW_UPDATE ->
                {
                    plugin.logger.info("${it.message}: ${it.version}")
                }
                is UpdateChecker.UpdateResult.EXCEPTIONS ->
                {
                    plugin.logger.log(Level.WARNING, it.message, it.throwable)
                }
            }
        }
    }

    fun conf(): SettingsManager
    {
        return checkNotNull(conf)
    }

    fun manager(): BungeeCommandManager
    {
        return cmds
    }
}