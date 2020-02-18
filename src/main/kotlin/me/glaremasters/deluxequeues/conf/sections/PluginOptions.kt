package me.glaremasters.deluxequeues.conf.sections

import ch.jalu.configme.Comment
import ch.jalu.configme.SettingsHolder
import ch.jalu.configme.configurationdata.CommentsConfiguration
import ch.jalu.configme.properties.Property
import ch.jalu.configme.properties.PropertyInitializer.newListProperty
import ch.jalu.configme.properties.PropertyInitializer.newProperty

internal object PluginOptions : SettingsHolder {

    @JvmField
    @Comment("Would you like to check for updates?")
    val UPDATE_CHECK: Property<Boolean> = newProperty("settings.update-check", true)

    @JvmField
    val LANGUAGE: Property<String> = newProperty("settings.language", "en-US")

    @JvmField
    @Comment("How many seconds should be inbetween each queue movement?")
    val DELAY_LENGTH: Property<Int> = newProperty("settings.delay-length", 2)

    @JvmField
    @Comment("List all the servers here that you would like to have a queue for.", "This is also where you specify how many players need to be on each server.", "You also set the max slots of the server here, make sure it always stays the same as the actual max.", "Synxtax: server name; amount of players to start the queue at; max slots of server", "Example: hub;50;200")
    val QUEUE_SERVERS: Property<List<String>> = newListProperty("settings.servers", "")

    @JvmField
    @Comment("What would you like the donator permission node to be?", "Donators will be moved to the front of the queue!")
    val DONATOR_PERMISSION: Property<String> = newProperty("settings.donator-permission", "deluxequeues.vip")

    @JvmField
    @Comment("What would you like the staff permission node to be?", "Staff will bypass the queue!")
    val STAFF_PERMISSION: Property<String> = newProperty("settings.staff-permission", "deluxequeues.staff")

    @JvmField
    @Comment("How would you like to inform the player that they are in the queue?", "Currently supports: ACTIONBAR, TEXT, TITLE")
    val INFORM_METHOD: Property<String> = newProperty("notify.method", "ACTIONBAR")

    @JvmField
    @Comment("How would you like the design for the ActionBar to look?")
    val ACTIONBAR_DESIGN: Property<String> = newProperty("notify.actionbar.design", "Current Position: {pos} / {total}")

    @JvmField
    @Comment("How would you like the design for the text to look?")
    val TEXT_DESIGN: Property<String> = newProperty("notify.text.design", "You are currently in position {pos} out of {total}")

    @JvmField
    @Comment("How would you like the design for the title to look?")
    val TITLE_HEADER: Property<String> = newProperty("notify.title.title", "Current in queue")

    @JvmField
    var TITLE_FOOTER: Property<String> = newProperty("notify.title.subtitle", "Position: {pos} / {total}")

    override fun registerComments(configuration: CommentsConfiguration) {
        configuration.setComment(
                "settings",
                "DeluxeQueues",
                "Creator: Glare",
                "Contributors: https://github.com/darbyjack/DeluxeQueues/graphs/contributors",
                "Issues: https://github.com/darbyjack/DeluxeQueues/issues",
                "Spigot: https://www.spigotmc.org/resources/69390/",
                "Discord: https://helpch.at/discord")
    }

}