package me.glaremasters.deluxequeues.conf

import ch.jalu.configme.SettingsManagerImpl
import ch.jalu.configme.configurationdata.ConfigurationDataBuilder
import ch.jalu.configme.migration.PlainMigrationService
import ch.jalu.configme.resource.YamlFileResource
import me.glaremasters.deluxequeues.conf.sections.PluginOptions
import java.io.File

internal class DeluxeQueuesConfiguration(file: File) : SettingsManagerImpl(YamlFileResource(file), ConfigurationDataBuilder.createConfiguration(SECTIONS), PlainMigrationService()) {

    private companion object {
        private val SECTIONS = listOf(
                PluginOptions::class.java
        )
    }
}