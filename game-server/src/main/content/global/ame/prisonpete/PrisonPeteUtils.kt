package content.global.ame.prisonpete

import content.data.RandomEvent
import core.api.*
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.system.timer.impl.AntiMacro
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.map.zone.ZoneBorders
import core.game.world.update.flag.context.Graphics
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Sounds

object PrisonPeteUtils {
    val PRISON_LOCATION: Location = Location.create(2086, 4462, 0)

    const val GET_REWARD = "/save:prisonpete:reward"
    const val POP_KEY = "/save:prisonpete:pop-animals"
    const val POP_KEY_FALSE = "/save:prisonpete:pop-incorrect"
    const val POP_KEY_VALUE = "/save:prisonpete:value"

    val ANIMAL_ID = intArrayOf(3119, 3120, 3121, 3122)

    val PRISON_ZONE = ZoneBorders(2075, 4458, 2096, 4474)

    fun cleanup(player: Player) {
        player.properties.teleportLocation = getAttribute(player, RandomEvent.save(), null)
        clearLogoutListener(player, RandomEvent.logout())
        removeAttributes(
            player,
            POP_KEY,
            GET_REWARD,
            RandomEvent.save(),
            RandomEvent.logout(),
            POP_KEY_FALSE,
            POP_KEY_VALUE,
        )
        sendMessage(player, "Welcome back to ${GameWorld.settings!!.name}.")
        if (anyInInventory(player, Items.PRISON_KEY_6966)) {
            removeAll(player, Items.PRISON_KEY_6966)
        }
    }

    fun bringKey(player: Player) {
        queueScript(player, 1, QueueStrength.SOFT) { stage: Int ->
            when (stage) {
                0 -> {
                    forceWalk(player, Location.create(2084, 4461, 0), "smart")
                    return@queueScript keepRunning(player)
                }

                1 -> {
                    face(player, findNPC(NPCs.PRISON_PETE_3118)!!.location)
                    openDialogue(player, PrisonPeteDialogue(dialOpt = 2))
                    return@queueScript stopExecuting(player)
                }

                else -> return@queueScript stopExecuting(player)
            }
        }
    }

    fun teleport(player: Player) {
        queueScript(player, 1, QueueStrength.SOFT) { stage: Int ->
            when (stage) {
                0 -> {
                    setAttribute(player, RandomEvent.save(), player.location)
                    visualize(player, 714, Graphics(308, 85, 50))
                    playAudio(player, Sounds.TP_ALL_200)
                    return@queueScript delayScript(player, 3)
                }

                1 -> {
                    registerLogoutListener(player, RandomEvent.logout()) { p ->
                        p.location = getAttribute(p, RandomEvent.save(), player.location)
                    }
                    teleport(player, PRISON_LOCATION)
                    return@queueScript delayScript(player, 1)
                }

                2 -> {
                    setAttribute(player, POP_KEY, 0)
                    AntiMacro.terminateEventNpc(player)
                    resetAnimator(player)
                    return@queueScript stopExecuting(player)
                }

                else -> return@queueScript stopExecuting(player)
            }
        }
    }

    fun reward(player: Player) {
        AntiMacro.rollEventLoot(player).forEach { addItemOrDrop(player, it.id, it.amount) }
    }
}
