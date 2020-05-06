package me.glaremasters.deluxequeues.configuration

import ch.jalu.configme.SettingsManager
import ch.jalu.configme.SettingsManagerBuilder
import ch.jalu.configme.migration.PlainMigrationService
import me.glaremasters.deluxequeues.DeluxeQueues
import me.glaremasters.deluxequeues.configuration.ConfigBuilder.buildConfig
import java.io.File

/**
 * Created by Glare
 * Date: 7/13/2019
 * Time: 10:24 PM
 */
class SettingsHandler(deluxeQueues: DeluxeQueues) {

    val settingsManager: SettingsManager = SettingsManagerBuilder
            .withYamlFile(File(deluxeQueues.dataFolder, "config.yml"))
            .migrationService(PlainMigrationService())
            .configurationData(buildConfig())
            .create()

}
