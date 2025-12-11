package content.region.other.entrana.quest.zep.cutscene

import core.api.*
import core.game.activity.Cutscene
import core.game.dialogue.FaceAnim
import core.game.node.entity.impl.Projectile
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import core.game.world.map.Location
import shared.consts.Animations
import shared.consts.NPCs
import shared.consts.Quests
import shared.consts.Regions

class ExperimentCutscene(player: Player) : Cutscene(player) {

    override fun setup() {
        setExit(Location.create(2810, 3356, 0))
        if (player.settings.isRunToggled) {
            player.settings.toggleRun()
        }
        loadRegion(Regions.ENTRANA_11060)
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> {
                fadeToBlack()
                timedUpdate(6)
            }
            1 -> {
                teleport(player, 56, 27,0)
                faceLocation(player, Location(440, 25, 0))
                moveCamera(58,30, 350)
                rotateCamera(54, 18, 400)
                addNPC(NPCs.AUGUSTE_5049, 57, 27, Direction.SOUTH_WEST)
                fadeFromBlack()
                timedUpdate(6)
            }
            2 -> {
                visualize(player, Animations.BALLOON_FLY_5142, 877)
                timedUpdate(2)
            }
            3 -> {
                Projectile
                    .create(player, null,879, 45, 45, 1, 70, 0)
                    .transform(player, player.location.transform(Direction.SOUTH, player.direction.ordinal + 1), false, 70, 140).send()
                if(getQuestStage(player,Quests.ENLIGHTENED_JOURNEY) <= 4) timedUpdate(3) else runStage(13)
            }
            4 -> {
                moveCamera(63, 29, 600)
                rotateCamera(55, 23, 600, 2)
                timedUpdate(3)
            }
            5 -> {
                face(getNPC(AUGUSTE.id)!!, player, 2)
                face(player,getNPC(AUGUSTE.id)!!,  2)
                timedUpdate(2)
            }
            6  -> dialogueUpdate(NPCs.AUGUSTE_5049, FaceAnim.HAPPY, "That was perfect. My hypothesis was right!")
            7  -> playerDialogueUpdate(FaceAnim.SCARED, "Did you not see the burning?")
            8  -> dialogueUpdate(NPCs.AUGUSTE_5049, FaceAnim.HAPPY, "One more test. Then we shall proceed.")
            9 -> playerDialogueUpdate(FaceAnim.SCARED, "Burning? Fire? Hello?")
            10 -> dialogueUpdate(NPCs.AUGUSTE_5049, FaceAnim.HAPPY, "We shall meekly go! No...no...it needs to sound grander. We shall cautiously go...")
            11 -> playerDialogueUpdate(FaceAnim.SCARED, "We're doomed.")
            12 -> end {}

            13 -> {
                addNPC(NPCs.BOB_5058,58, 23, Direction.WEST)
                addNPC(NPCs.CURLY_5059,59, 24, Direction.WEST)
                addNPC(NPCs.MOE_5060,60, 23, Direction.WEST)
                addNPC(NPCs.LARRY_5061,59, 22, Direction.WEST)
                moveCamera(63, 23, 600)
                rotateCamera(63, 23, 600, 1000)
                timedUpdate(4)
            }

            14 -> {
                move(getNPC(NPCs.BOB_5058)!!, 53, 23)
                move(getNPC(NPCs.CURLY_5059)!!, 53, 24)
                move(getNPC(NPCs.MOE_5060)!!, 54, 23)
                move(getNPC(NPCs.LARRY_5061)!!, 53, 22)
                sendChat(getNPC(NPCs.BOB_5058)!!, "Destroy the foating thing!")
                timedUpdate(5)
            }

            15 -> {
                moveCamera(56,22, 600, 1000)
                rotateCamera(62,57, 600, 1000)

                timedUpdate(1)
            }
            16 -> {
                animate(player, Animations.HUMAN_SHRUG_2113)
                playerDialogueUpdate(FaceAnim.FRIENDLY, "Well, that went down like a lead balloon.")
            }
            17 -> end {

            }
        }
    }

    companion object {
        private val AUGUSTE = NPC(NPCs.AUGUSTE_5049)
    }
}
