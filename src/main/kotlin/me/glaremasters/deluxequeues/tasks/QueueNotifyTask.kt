package me.glaremasters.deluxequeues.tasks

import me.glaremasters.deluxequeues.queues.DeluxeQueue

class QueueNotifyTask(private val queue: DeluxeQueue) : Runnable {
	
	override fun run() {
		queue.players.forEach(queue::notifyPlayer)
	}
	
}