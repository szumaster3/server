package content.region.kandarin.ardougne.west.quest.elena.cutscene

import core.api.*
import core.api.quest.setQuestStage
import core.game.activity.Cutscene
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import core.game.world.map.Location
import org.rs.consts.Animations
import org.rs.consts.NPCs
import org.rs.consts.Quests

class UndergroundCutscene(
    player: Player,
) : Cutscene(player) {
    override fun setup() {
        setExit(Location.create(2514, 9740, 0))
        if (player.settings.isRunToggled) {
            player.settings.toggleRun()
        }
        loadRegion(10136)
        addNPC(EDMOND, 18, 13, Direction.SOUTH)
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> {
                fadeToBlack()
                timedUpdate(5)
            }

            1 -> {
                teleport(player, 18, 12)
                timedUpdate(1)
            }

            2 -> {
                fadeFromBlack()
                moveCamera(21, 16)
                rotateCamera(18, 13)
                timedUpdate(4)
            }

            3 -> {
                animate(getNPC(EDMOND)!!, ROPE_PULL)
                animate(player, ROPE_PULL)
                animateScenery(player, getScenery(base.transform(18, 11, 0))!!, 3189)
                animateScenery(player, getScenery(base.transform(18, 12, 0))!!, 3188)
                animateScenery(player, getScenery(base.transform(18, 13, 0))!!, 3188)
                sendChat(player, "1... 2... 3... Pull!")
                timedUpdate(6)
            }

            4 -> {
                setVarbit(player, 1787, 6, true)
                face(player, getNPC(EDMOND)!!.location)
                timedUpdate(2)
            }

            5 -> {
                dialogueUpdate(
                    EDMOND,
                    FaceAnim.FRIENDLY,
                    "Once you're in the city look for a man called Jethick, he's an old friend and should help you. Send",
                )
                sendChat(getNPC(EDMOND)!!, "Once you're in the city")
            }

            6 -> {
                sendChat(getNPC(EDMOND)!!, "look for a man called")
                timedUpdate(4)
            }

            7 -> {
                sendChat(getNPC(EDMOND)!!, "Jethick, he's an old friend")
                timedUpdate(4)
            }

            8 -> {
                sendChat(getNPC(EDMOND)!!, "and should help you. Send")
                timedUpdate(4)
            }

            9 -> {
                dialogueUpdate(
                    EDMOND,
                    FaceAnim.FRIENDLY,
                    "him my regards, I Haven't seen him since before Elena was born.",
                )
                sendChat(getNPC(EDMOND)!!, "him my regards, I haven't")
            }

            10 -> {
                sendChat(getNPC(EDMOND)!!, "seen him since before")
                timedUpdate(4)
            }

            11 -> {
                sendChat(getNPC(EDMOND)!!, "Elena was born.")
                timedUpdate(4)
            }

            12 -> {
                sendChat(player, "Alright, thanks I will.")
                timedUpdate(3)
            }

            13 -> {
                sendPlayerDialogue(player, "Alright, thanks I will.")
                timedUpdate(4)
            }

            14 -> {
                end {
                    setQuestStage(player, Quests.PLAGUE_CITY, 7)
                }
            }
        }
    }

    companion object {
        private const val EDMOND = NPCs.EDMOND_714
        private const val ROPE_PULL = Animations.PC_SEWERS_ROPE_PULL_3187
    }
}
