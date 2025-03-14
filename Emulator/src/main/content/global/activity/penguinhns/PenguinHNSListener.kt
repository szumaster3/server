package content.global.activity.penguinhns

import content.data.GameAttributes
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.system.task.Pulse
import core.game.world.GameWorld.Pulser
import core.game.world.update.flag.context.Animation
import core.tools.BLUE
import org.rs.consts.Items
import org.rs.consts.NPCs

class PenguinHNSListener : InteractionListener {
    override fun defineListeners() {
        on(sceneryPenguinIDs, IntType.NPC, "spy-on") { player, node ->
            val npc = node.asNpc()
            if (!getAttribute(player, GameAttributes.ACTIVITY_PENGUINS_HNS, false)) {
                sendMessage(player, "You must speak to Larry at Ardougne Zoo before spying on penguins.")
                return@on true
            }

            if (PenguinManager.hasTagged(player, npc.location)) {
                sendMessage(player, "You've already tagged this penguin.")
            } else {
                Pulser.submit(SpyPulse(player, npc))
                playJingle(player, 345)
            }
            return@on true
        }

        on(Items.SPY_NOTEBOOK_13732, IntType.ITEM, "read") { player, _ ->
            val total = getAttribute(player, GameAttributes.ACTIVITY_PENGUINS_HNS_SCORE, 0)
            sendDialogue(player, "Total points: $BLUE$total</col>.")
            return@on true
        }
    }

    private val sceneryPenguinIDs =
        intArrayOf(
            NPCs.BARREL_8104,
            NPCs.BUSH_8105,
            NPCs.CACTUS_8107,
            NPCs.CRATE_8108,
            NPCs.ROCK_8109,
            NPCs.TOADSTOOL_8110,
        )

    class SpyPulse(
        val player: Player,
        val npc: NPC,
    ) : Pulse() {
        var stage = 0
        val totalPoints = getAttribute(player, GameAttributes.ACTIVITY_PENGUINS_HNS_SCORE, 0)
        val animationID = Animation(10355)

        override fun pulse(): Boolean {
            when (stage++) {
                0 -> {
                    lock(player, 1000)
                    animate(player, animationID)
                }

                2 -> {
                    sendMessage(player, "You manage to spy on the penguin.").also {
                        setAttribute(player, "/save:${GameAttributes.ACTIVITY_PENGUINS_HNS_SCORE}", totalPoints + 1)
                        PenguinManager.registerTag(player, npc.location)
                    }
                }

                3 -> {
                    unlock(player)
                    return true
                }
            }
            return false
        }

        override fun stop() {
            super.stop()
            unlock(player)
        }
    }
}
