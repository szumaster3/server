package core.game.node.entity.combat

import core.api.playGlobalAudio
import core.game.container.Container
import core.game.container.ContainerType
import core.game.event.SelfDeathEvent
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.impl.Animator
import core.game.node.entity.impl.PulseType
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.prayer.PrayerType
import core.game.node.item.Item
import core.game.system.task.NodeTask
import core.game.world.GameWorld
import core.game.world.GameWorld.ticks

/**
 * Handles an entity death task.
 *
 * @author Emperor
 */
class DeathTask private constructor() : NodeTask(1) {

    override fun start(node: Node, vararg n: Node) {
        val e = node as Entity
        e.walkingQueue.reset()
        e.setAttribute("state:death", true)
        e.setAttribute("tick:death", GameWorld.ticks)
        e.lock(50)
        e.face(null)
        val killer = if (n.size > 0) n[0] as Entity else e
        if (e is NPC) {
            killer.removeAttribute("combat-time")
            val audio = e.asNpc().getAudio(2)
            if (audio != null) {
                playGlobalAudio(e.getLocation(), audio.id)
            }
        }
        e.graphics(Animator.RESET_G)
        e.visualize(e.properties.deathAnimation, e.properties.deathGfx)
        e.animator.forceAnimation(e.properties.deathAnimation)
        e.commenceDeath(killer)
        e.impactHandler.disabledTicks = 50
    }

    override fun exec(node: Node, vararg n: Node): Boolean {
        val e = node as Entity
        var ticks = e.properties.deathAnimation.duration
        if (ticks < 1 || ticks > 30) {
            ticks = 6
        }
        return e.getAttribute("tick:death", -1) <= GameWorld.ticks - ticks
    }

    override fun stop(node: Node, vararg n: Node) {
        val e = node as Entity
        val killer = if (n.size > 0) n[0] as Entity else e
        e.removeAttribute("state:death")
        e.removeAttribute("tick:death")
        val spawn = if (e.properties.isSafeZone) e.properties.safeRespawn else e.properties.spawnLocation
        e.animator.forceAnimation(Animator.RESET_A)
        e.properties.teleportLocation = spawn
        e.unlock()
        e.finalizeDeath(killer)
        e.impactHandler.npcImpactLog.clear() // check if this needs to be before finalize
        e.impactHandler.playerImpactLog.clear() // check if this needs to be before finalize
        e.impactHandler.disabledTicks = 4
        e.dispatch(SelfDeathEvent(killer))
    }

    override fun removeFor(s: String, node: Node, vararg n: Node): Boolean {
        return false
    }

    companion object {
        /**
         * The death task singleton.
         */
        val singleton: DeathTask = DeathTask()

        /**
         * Gets the player's death containers.
         *
         * @param player The player.
         * @return The containers, index 0 = kept items, index 1 = lost items.
         */
        @JvmStatic
        fun getContainers(player: Player): Array<Container?> {
            val containers = arrayOfNulls<Container>(2)
            val wornItems = Container(42, ContainerType.ALWAYS_STACK)
            wornItems.addAll(player.inventory)
            wornItems.addAll(player.equipment)
            var count = 3
            if (player.skullManager.isSkulled) {
                count -= 3
            }
            if (player.prayer[PrayerType.PROTECT_ITEMS]) {
                count += 1
            }
            val keptItems = Container(count, ContainerType.NEVER_STACK)
            containers[0] = keptItems

                for (i in 0 until count) {
                    for (j in 0..41) {
                        var item = wornItems[j]
                        if (item != null) {
                            var x = 0
                            while (x < count) {
                                var kept = keptItems[x]
                                if (kept == null || kept != null && kept.definition.getAlchemyValue(true) <= item!!.definition.getAlchemyValue(
                                        true
                                    )
                                ) {
                                    keptItems.replace(Item(item!!.id, 1, item.charge), x)
                                    x++
                                    while (x < count) {
                                        val newKept = keptItems[x]
                                        keptItems.replace(kept, x++)
                                        kept = newKept
                                    }
                                    if (kept != null) {
                                        wornItems.add(kept, false)
                                    }
                                    item = wornItems[j]
                                    wornItems.replace(Item(item.id, item.amount - 1, item.charge), j)
                                    break
                                }
                                x++
                            }
                        }
                    }
                }

            containers[1] = Container(42, ContainerType.DEFAULT)
            containers[1]!!.addAll(wornItems)
            return containers
        }

        /**
         * Starts the death task for an entity.
         *
         * @param entity The entity.
         * @param killer The entity's killer.
         */
        @Suppress("deprecation")
        fun startDeath(entity: Entity, killer: Entity?) {
            var killer = killer
            if (!isDead(entity)) {
                if (killer == null) {
                    killer = entity
                }
                val pulse = DeathTask().schedule(entity, killer)
                entity.pulseManager.run(pulse, PulseType.STRONG)
            }
        }

        /**
         * Checks if the entity is dead.
         *
         * @param e The entity.
         * @return `True` if so.
         */
        @JvmStatic
        fun isDead(e: Entity): Boolean {
            return if (e is NPC) e.respawnTick > ticks || e.getAttribute(
                "state:death",
                false
            )
            else e.getAttribute("state:death", false)
        }
    }
}
