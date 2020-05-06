package me.glaremasters.deluxequeues.configuration.sections

import ch.jalu.configme.Comment
import ch.jalu.configme.SettingsHolder
import ch.jalu.configme.configurationdata.CommentsConfiguration
import ch.jalu.configme.properties.Property
import ch.jalu.configme.properties.PropertyInitializer

/**
 * Created by Glare
 * Date: 7/13/2019
 * Time: 10:37 PM
 */
class ConfigOptions : SettingsHolder {

    override fun registerComments(configuration: CommentsConfiguration) {
        val pluginHeader = arrayOf(
                "DeluxeQueues",
                "Creator: Glare",
                "Contributors: https://github.com/darbyjack/DeluxeQueues/graphs/contributors",
                "Issues: https://github.com/darbyjack/DeluxeQueues/issues",
                "Spigot: https://www.spigotmc.org/resources/69390/",
                "Discord: https://helpch.at/discord"
        )
        configuration.setComment("settings", *pluginHeader)
    }

    companion object {
        @JvmField
        @Comment("Would you like to check for updates?")
        val UPDATE_CHECK: Property<Boolean> = PropertyInitializer.newProperty("settings.update-check", true)

        @Comment("How many seconds should be in between each queue movement?")
        val DELAY_LENGTH: Property<Int> = PropertyInitializer.newProperty("settings.delay-length", 2)

        @Comment(
                "List all the servers here that you would like to have a queue for.",
                "This is also where you specify how many players need to be on each server.",
                "You also set the max slots of the server here, make sure it always stays the same as the actual max.",
                "Syntax: server name; amount of players to start the queue at; max slots of server",
                "Example: hub;50;200"
        )
        val QUEUE_SERVERS: Property<List<String>> = PropertyInitializer.newListProperty("settings.servers", "")

        @Comment(
                "What would you like the donator permission node to be?",
                "Donators will be moved to the front of the queue!"
        )
        val DONATOR_PERMISSION: Property<String> = PropertyInitializer.newProperty("settings.donator-permission", "deluxequeues.vip")

        @Comment(
                "What would you like the staff permission node to be?",
                "Staff will bypass the queue!"
        )
        val STAFF_PERMISSION: Property<String> = PropertyInitializer.newProperty("settings.staff-permission", "deluxequeues.staff")

        @Comment(
                "How would you like to inform the player that they are in the queue?",
                "Currently supports: ACTIONBAR, TEXT, TITLE"
        )
        val INFORM_METHOD: Property<String> = PropertyInitializer.newProperty("notify.method", "ACTIONBAR")

        @Comment("How would you like the design for the ActionBar to look?")
        val ACTIONBAR_DESIGN: Property<String> = PropertyInitializer.newProperty("notify.actionbar.design", "Current Position: {pos} / {total}")

        @Comment("How would you like the design for the text to look?")
        val TEXT_DESIGN: Property<String> = PropertyInitializer.newProperty("notify.text.design", "You are currently in position {pos} out of {total}")

        @Comment("How would you like the design for the title to look?")
        val TITLE_HEADER: Property<String> = PropertyInitializer.newProperty("notify.title.title", "Current in queue")
        val TITLE_FOOTER: Property<String> = PropertyInitializer.newProperty("notify.title.subtitle", "Position: {pos} / {total}")

    }
}
