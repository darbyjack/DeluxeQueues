package me.glaremasters.deluxequeues.configuration

import ch.jalu.configme.SettingsManagerImpl
import ch.jalu.configme.configurationdata.ConfigurationDataBuilder
import ch.jalu.configme.migration.PlainMigrationService
import ch.jalu.configme.resource.YamlFileResource
import me.glaremasters.deluxequeues.configuration.sections.ConfigOptions
import java.io.File

internal class DeluxeQueuesConfiguration(file: File) : SettingsManagerImpl(YamlFileResource(file), ConfigurationDataBuilder.createConfiguration(SECTIONS), PlainMigrationService()) {

    private companion object {
        private val SECTIONS = listOf(
                ConfigOptions::class.java
        )
    }
}