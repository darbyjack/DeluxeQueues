package me.glaremasters.deluxequeues.configuration

import ch.jalu.configme.configurationdata.ConfigurationData
import ch.jalu.configme.configurationdata.ConfigurationDataBuilder
import me.glaremasters.deluxequeues.configuration.sections.ConfigOptions

/**
 * Created by Glare
 * Date: 7/13/2019
 * Time: 10:27 PM
 */
object ConfigBuilder {

    @JvmStatic
    fun buildConfig(): ConfigurationData {
        return ConfigurationDataBuilder.createConfiguration(ConfigOptions::class.java)
    }

}
