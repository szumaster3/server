package content.region.kandarin.gnome.plugin

import core.api.*
import core.api.quest.isQuestComplete
import core.game.dialogue.FaceAnim
import core.game.dialogue.SequenceDialogue.dialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import org.rs.consts.Animations
import org.rs.consts.NPCs
import org.rs.consts.Quests
import org.rs.consts.Scenery
import org.rs.consts.Graphics as Gfx

/**
 * Handles Gnome Spirit Tree transportation system.
 */
class GnomeSpiritTreePlugin : InteractionListener {
    private val spiritTrees = intArrayOf(Scenery.SPIRIT_TREE_1317, Scenery.SPIRIT_TREE_1293, Scenery.SPIRIT_TREE_1294)

    override fun defineListeners() {

        /*
         * Handles spirit tree options.
         */

        on(spiritTrees, IntType.SCENERY, "talk-to") { player, _ ->
            openSpiritTreeDialogue(player, showIntro = true)
            return@on true
        }
        on(spiritTrees, IntType.SCENERY, "teleport") { player, _ ->
            openSpiritTreeDialogue(player, showIntro = false)
            return@on true
        }

    }

    /**
     * Opens the Spirit Tree dialogue.
     */
    private fun openSpiritTreeDialogue(player: Player, showIntro: Boolean) {
        val npc = NPC(NPCs.SPIRIT_TREE_3636)
        if (!isQuestComplete(player, Quests.TREE_GNOME_VILLAGE)) {
            player.sendMessage("The tree doesn't feel like talking.")
            return
        }
        dialogue(player) {
            if (showIntro) {
                npc(npc, FaceAnim.CHILD_NEUTRAL, "If you are a friend of the gnome people, you are a friend of mine. Do you wish to travel?")
            }
            options("Where would you like to go?", "Tree Gnome Village", "Tree Gnome Stronghold", "Battlefield of Khazard", "Grand Exchange") { choice ->
                when (choice) {
                    1 -> sendTeleport(player, Location(2542, 3170, 0))
                    2 -> sendTeleport(player, Location(2461, 3444, 0))
                    3 -> sendTeleport(player, Location(2556, 3259, 0))
                    4 -> sendTeleport(player, Location(3184, 3508, 0))
                    else -> player.sendMessage("Invalid choice.")
                }
            }
        }
    }

    /**
     * Handles spirit tree pulse.
     */
    private fun sendTeleport(player: Player, location: Location) {
        submitWorldPulse(object : Pulse(1, player) {
            var count = 0
            override fun pulse(): Boolean {
                when (count) {
                    0 -> visualize(player, Animation(Animations.HUMAN_SPIRIT_TREE_TELEPORT_7082), Graphics(Gfx.GRAND_TREE_TP_A_1228))
                    3 -> teleport(player, location)
                    5 -> {
                        visualize(player, Animation(Animations.HUMAN_SPIRIT_TREE_TELEPORT_FROM_7084), Graphics(Gfx.GRAND_TREE_TP_B_1229))
                        player.face(null)
                        if (withinDistance(player, Location.create(3184, 3508, 0))) {
                            finishDiaryTask(player, DiaryType.VARROCK, 1, 5)
                        }
                        return true
                    }
                }
                count++
                return false
            }
        })
    }
}