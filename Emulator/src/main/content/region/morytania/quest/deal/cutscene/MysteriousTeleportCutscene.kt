package content.region.morytania.quest.deal.cutscene

import content.region.morytania.quest.deal.dialogue.CaptainBrainDeathDialogueFile
import core.api.*
import core.api.ui.setMinimapState
import core.game.activity.Cutscene
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.update.flag.context.Graphics
import org.rs.consts.Music
import org.rs.consts.NPCs

class MysteriousTeleportCutscene(
    player: Player,
) : Cutscene(player) {
    override fun setup() {
        setExit(Location(2144, 5108, 1))
        if (player.settings.isRunToggled) {
            player.settings.toggleRun()
        }
        loadRegion(8527)
        addNPC(CAPTAIN, 6, 7, Direction.NORTH)
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> {
                fadeToBlack()
                setMinimapState(player, 2)
                timedUpdate(6)
            }

            1 -> {
                teleport(player, 0, 4, 1)
                playAudio(player, Music.BLISTERING_BARNACLES_498)
                moveCamera(7, 4)
                rotateCamera(7, 6)
                fadeFromBlack()
                timedUpdate(4)
            }

            2 -> dialogueUpdate(CAPTAIN, FaceAnim.SAD, "Arrr... 'Tis lookin' bleak...")
            3 -> {
                sendNPCDialogue(player, PIRATE, "Cap'n!", FaceAnim.HAPPY)
                addNPC(PIRATE, 1, 4, Direction.NORTH_WEST)
                timedUpdate(3)
            }

            4 -> {
                sendNPCDialogue(player, PIRATE, "Good news Cap'n!", FaceAnim.HAPPY)
                timedUpdate(2)
            }

            5 -> {
                move(getNPC(PIRATE)!!, 0, 6)
                move(getNPC(PIRATE)!!, 0, 7)
                timedUpdate(4)
            }

            6 -> {
                face(getNPC(PIRATE)!!, getNPC(CAPTAIN)!!)
                face(getNPC(CAPTAIN)!!, getNPC(PIRATE)!!)
                dialogueUpdate(PIRATE, FaceAnim.HAPPY, "I found us a hero down by the docks!")
            }

            7 -> dialogueUpdate(CAPTAIN, FaceAnim.FRIENDLY, "Be they heroic, brave and true?")
            8 -> dialogueUpdate(PIRATE, FaceAnim.HAPPY, "Aye! They also be gullible, tied up and unconscious!")
            9 ->
                dialogueUpdate(
                    PIRATE,
                    FaceAnim.HAPPY,
                    "They were willing to help out some random stranger" +
                        "with a good enough sob story, so I smacked them with a" +
                        "bottle and rowed them over.",
                )

            10 -> dialogueUpdate(CAPTAIN, FaceAnim.HAPPY, "Brilliant! The island's location will remain a secret!")
            11 -> dialogueUpdate(CAPTAIN, FaceAnim.HAPPY, "Bring 'em here and wake 'em up.")
            12 -> {
                getNPC(CAPTAIN)!!.location.transform(Direction.NORTH)!!
                dialogueUpdate(CAPTAIN, FaceAnim.HAPPY, "We may make it through this yet...")
                player.questRepository.setStageNonmonotonic(player.questRepository.forIndex(107), 2)
            }

            13 -> {
                endWithoutFade()
                openDialogue(player, CaptainBrainDeathDialogueFile(1))
                sendGraphics(
                    Graphics(
                        org.rs.consts.Graphics.STUN_BIRDIES_ABOVE_HEAD_80,
                        96,
                    ),
                    player.location,
                )
            }
        }
    }

    companion object {
        const val CAPTAIN = NPCs.CAPTAIN_BRAINDEATH_2827
        const val PIRATE = NPCs.PIRATE_PETE_2826
    }
}
