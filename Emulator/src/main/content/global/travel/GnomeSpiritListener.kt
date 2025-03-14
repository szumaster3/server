package content.global.travel

import core.api.*
import core.api.quest.isQuestComplete
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.tools.END_DIALOGUE
import org.rs.consts.Animations
import org.rs.consts.NPCs
import org.rs.consts.Quests
import org.rs.consts.Scenery

class GnomeSpiritTreeListener : InteractionListener {
    private val spiritTrees = intArrayOf(Scenery.SPIRIT_TREE_1317, Scenery.SPIRIT_TREE_1293, Scenery.SPIRIT_TREE_1294)

    override fun defineListeners() {
        on(spiritTrees, IntType.SCENERY, "talk-to") { player, _ ->
            openDialogue(player, GnomeSpiritTreeDialogue(), NPC(NPCs.SPIRIT_TREE_3636))
            return@on true
        }
        on(spiritTrees, IntType.SCENERY, "teleport") { player, _ ->
            openDialogue(player, GnomeSpiritTreeTeleportDialogue(), NPC(NPCs.SPIRIT_TREE_3636))
            return@on true
        }
    }
}

class GnomeSpiritTreeTeleportDialogue : DialogueFile() {
    private val destination =
        arrayOf(
            Location(2542, 3170, 0),
            Location(2461, 3444, 0),
            Location(2556, 3259, 0),
            Location(3184, 3508, 0),
        )

    private val animationId =
        arrayOf(
            Animation(Animations.HUMAN_SPIRIT_TREE_TELEPORT_7082),
            Animation(Animations.HUMAN_SPIRIT_TREE_TELEPORT_FROM_7084),
        )
    private val graphicsIds =
        arrayOf(
            Graphics(org.rs.consts.Graphics.GRAND_TREE_TP_A_1228),
            Graphics(org.rs.consts.Graphics.GRAND_TREE_TP_B_1229),
        )

    fun hasQuestCompleted(player: Player): Boolean {
        if (!isQuestComplete(player, Quests.TREE_GNOME_VILLAGE)) {
            sendDialogue(player, "The tree doesn't feel like talking.")
            stage = END_DIALOGUE
            return false
        }
        return true
    }

    private fun sendTeleport(
        player: Player,
        location: Location,
    ) {
        end()
        submitWorldPulse(
            object : Pulse(1, player) {
                var count = 0

                override fun pulse(): Boolean {
                    when (count) {
                        0 -> visualize(player, animationId[0], graphicsIds[0])
                        3 -> teleport(player, location)
                        5 -> {
                            visualize(player, animationId[1], graphicsIds[1])
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
            },
        )
    }

    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        if (!GnomeSpiritTreeTeleportDialogue().hasQuestCompleted(player!!)) {
            stage = END_DIALOGUE
            return
        }
        when (stage) {
            0 -> {
                setTitle(player!!, 4)
                sendDialogueOptions(
                    player!!,
                    "Where would you like to go?",
                    "Tree Gnome Village",
                    "Tree Gnome Stronghold",
                    "Battlefield of Khazard",
                    "Grand Exchange",
                ).also { stage++ }
            }

            1 ->
                when (buttonID) {
                    1 -> sendTeleport(player!!, destination[0])
                    2 -> sendTeleport(player!!, destination[1])
                    3 -> sendTeleport(player!!, destination[2])
                    4 -> sendTeleport(player!!, destination[3])
                }
        }
    }
}

class GnomeSpiritTreeDialogue : DialogueFile() {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ) {
        if (!GnomeSpiritTreeTeleportDialogue().hasQuestCompleted(player!!)) {
            stage = END_DIALOGUE
            return
        }
        when (stage) {
            0 ->
                npcl(
                    FaceAnim.CHILD_NEUTRAL,
                    "If you are a friend of the gnome people, you are a friend of mine, Do you wish to travel?",
                ).also { stage++ }

            1 -> {
                openDialogue(player!!, GnomeSpiritTreeTeleportDialogue())
            }
        }
    }
}
