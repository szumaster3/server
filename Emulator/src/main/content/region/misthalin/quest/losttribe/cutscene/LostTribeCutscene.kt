package content.region.misthalin.quest.losttribe.cutscene

import core.api.animate
import core.api.face
import core.api.quest.finishQuest
import core.game.activity.Cutscene
import core.game.component.Component
import core.game.dialogue.FaceAnim
import core.game.global.action.DoorActionHandler
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.emote.Emotes
import core.game.world.map.Direction
import core.game.world.map.Location
import core.tools.DARK_RED
import org.rs.consts.NPCs
import org.rs.consts.Quests

class LostTribeCutscene(
    player: Player,
) : Cutscene(player) {
    override fun setup() {
        setExit(Location.create(3207, 3217, 0))
        if (player.settings.isRunToggled) {
            player.settings.toggleRun()
        }
        loadRegion(12850)
        addNPC(DUKE, 6, 23, Direction.SOUTH)
        addNPC(MISTAG, 8, 17, Direction.NORTH)
        addNPC(URTAG, 8, 15, Direction.NORTH)
        addNPC(SIGMUND, 13, 22, Direction.NORTH_WEST)
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> {
                fadeToBlack()
                timedUpdate(6)
            }

            1 -> {
                teleport(player, 7, 17)
                Component.setUnclosable(
                    player,
                    player.dialogueInterpreter.sendPlainMessage(
                        true,
                        "$DARK_RED- The Signing of the Lumbridge-Dorgeshuun Treaty -",
                    ),
                )
                timedUpdate(2)
            }

            2 -> {
                fadeFromBlack()
                moveCamera(8, 26)
                rotateCamera(5, 18)
                timedUpdate(6)
            }

            3 -> {
                DoorActionHandler.handleDoor(player, getObject(7, 17)!!)
                timedUpdate(3)
            }

            4 -> {
                move(player, 7, 22)
                move(getNPC(MISTAG)!!, 7, 19)
                move(getNPC(URTAG)!!, 7, 18)
                timedUpdate(5)
            }

            5 -> {
                move(getNPC(MISTAG)!!, 7, 20)
                move(getNPC(URTAG)!!, 6, 21)
                timedUpdate(3)
            }

            6 -> {
                player.faceLocation(getNPC(DUKE)!!.location)
                playerDialogueUpdate(FaceAnim.FRIENDLY, "Your grace, I present Ur-tag, headman of the Dorgeshuun.")
                Emotes.BOW.play(player)
            }

            7 -> {
                move(player, 7, 24)
                timedUpdate(5)
            }

            8 -> {
                animate(getNPC(DUKE)!!, Emotes.BOW.animation)
                animate(getNPC(URTAG)!!, URTAG_BOW_ANIM)
                dialogueUpdate(
                    DUKE,
                    FaceAnim.FRIENDLY,
                    "Welcome, Ur-tag. I am sorry that your race came under suspicion.",
                )
            }

            9 -> {
                dialogueUpdate(DUKE, FaceAnim.ANGRY, "I assure you that the warmongering element has been dealt with.")
                moveCamera(9, 22)
                rotateCamera(6, 22)
            }

            10 ->
                dialogueUpdate(
                    URTAG,
                    FaceAnim.OLD_NORMAL,
                    "I apologize for the damage to your cellar. I will send workers to repair the hole.",
                )

            11 ->
                dialogueUpdate(
                    DUKE,
                    FaceAnim.FRIENDLY,
                    "No, let it stay. It can be a route of commerce between our lands.",
                )

            12 -> {
                val duke = getNPC(DUKE)!!
                face(duke, player.location)
                face(player, duke.location)
                rotateCamera(6, 22, 300, 3)
                moveCamera(11, 22, 325, 3)
                dialogueUpdate(
                    DUKE,
                    FaceAnim.FRIENDLY,
                    "${player.username}, Lumbridge is in your debt. Please accept this ring as a token of my thanks.",
                )
            }

            13 -> dialogueUpdate(DUKE, FaceAnim.FRIENDLY, "It is enchanted to save you in your hour of need.")
            14 -> {
                move(getNPC(URTAG)!!, 7, 23)
                dialogueUpdate(
                    URTAG,
                    FaceAnim.OLD_NORMAL,
                    "I too thank you. Accept the freedom of the Dorgeshuun mines.",
                )
            }

            15 -> {
                dialogueUpdate(
                    URTAG,
                    FaceAnim.OLD_NORMAL,
                    "These are strange times. I never dreamed that I would see the surface, still less that I would be on friendly terms with its people.",
                )
                moveCamera(16, 21, 300, 3)
                rotateCamera(6, 22, 300, 2)
            }

            16 -> dialogueUpdate(SIGMUND, FaceAnim.ANGRY, "Prattle on, goblin.")
            17 -> {
                animate(getNPC(SIGMUND)!!, Emotes.LAUGH.animation)
                dialogueUpdate(SIGMUND, FaceAnim.EVIL_LAUGH, "Soon you will be destroyed!")
            }

            18 -> {
                move(getNPC(SIGMUND)!!, 16, 17)
                timedUpdate(4)
            }

            19 -> {
                end {
                    finishQuest(player, Quests.THE_LOST_TRIBE)
                }
            }
        }
    }

    companion object {
        private const val DUKE = NPCs.DUKE_HORACIO_2088
        private const val MISTAG = NPCs.MISTAG_2089
        private const val SIGMUND = NPCs.SIGMUND_2090
        private const val URTAG = NPCs.UR_TAG_5858
        private const val URTAG_BOW_ANIM = 6000
    }
}
