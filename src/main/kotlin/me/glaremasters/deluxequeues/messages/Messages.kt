package me.glaremasters.deluxequeues.messages

import co.aikar.locales.MessageKey
import co.aikar.locales.MessageKeyProvider

enum class Messages : MessageKeyProvider {
    QUEUES__LEFT, RELOAD__SUCCESS;

    /**
     * Message keys that grab from the config to send messages
     */
    val key: MessageKey = MessageKey.of(name.toLowerCase().replace("__", ".").replace("_", "-"))

    /**
     * Get the message get from the config
     * @return message key
     */
    override fun getMessageKey(): MessageKey {
        return key
    }
}
