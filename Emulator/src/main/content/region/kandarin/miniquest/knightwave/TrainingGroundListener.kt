package content.region.kandarin.miniquest.knightwave

import content.data.GameAttributes
import core.api.*
import core.api.quest.hasRequirement
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import org.rs.consts.NPCs
import org.rs.consts.Quests
import org.rs.consts.Scenery

class TrainingGroundListener : InteractionListener {
    override fun defineListeners() {
        on(intArrayOf(Scenery.LARGE_DOOR_25594, Scenery.LARGE_DOOR_25595), IntType.SCENERY, "open") { player, node ->
            if (!hasRequirement(player, Quests.KINGS_RANSOM)) return@on true
            if (getAttribute(player, KnightWaveAttributes.KW_COMPLETE, false) ||
                !getAttribute(player, KnightWaveAttributes.KW_BEGIN, false)
            ) {
                openDialogue(player, NPCs.SQUIRE_6169)
                return@on false
            }

            if (player.location.x >= 2752 && getAttribute(player, KnightWaveAttributes.KW_BEGIN, false)) {
                teleport(player, Location.create(2751, 3507, 2))
                removeAttributes(
                    player,
                    GameAttributes.PRAYER_LOCK,
                    KnightWaveAttributes.KW_SPAWN,
                    KnightWaveAttributes.KW_TIER,
                    KnightWaveAttributes.KW_BEGIN,
                )
                clearLogoutListener(player, KnightWaveAttributes.ACTIVITY)
                return@on true
            }

            teleport(player, Location.create(2753, 3507, 2))
            registerLogoutListener(player, KnightWaveAttributes.ACTIVITY) {
                removeAttributes(
                    player,
                    GameAttributes.PRAYER_LOCK,
                    KnightWaveAttributes.KW_SPAWN,
                    KnightWaveAttributes.KW_TIER,
                    KnightWaveAttributes.KW_BEGIN,
                )
            }
            GameWorld.Pulser.submit(
                object : Pulse(1) {
                    override fun pulse(): Boolean {
                        TrainingGroundActivity
                            .KnightWavesNPC(
                                player.getAttribute(KnightWaveAttributes.KW_TIER, 6177),
                                Location.create(2758, 3508, 2),
                                player,
                            ).init()
                        sendMessageWithDelay(player, "Remember, only melee combat is allowed in here.", 1)
                        return true
                    }
                },
            )
            return@on true
        }

        on(NPCs.MERLIN_213, IntType.NPC, "talk-to") { player, _ ->
            sendMessage(player, "Nothing interesting happens.")
            return@on true
        }
    }
}
