package content.region.kandarin.quest.makinghistory.cutscene

import content.region.kandarin.quest.makinghistory.dialogue.JorralDialogueExtension
import core.api.face
import core.api.openDialogue
import core.api.runTask
import core.api.sendDialogue
import core.game.activity.Cutscene
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import org.rs.consts.NPCs

class OutpostHistoryCutscene(
    player: Player,
) : Cutscene(player) {
    override fun setup() {
        setExit(player.location.transform(0, 0, 0))
        if (player.settings.isRunToggled) {
            player.settings.toggleRun()
        }
        loadRegion(9780)
        addNPC(NPCs.JORRAL_2932, 5, 19, Direction.WEST)
    }

    override fun runStage(stage: Int) {
        when (stage) {
            0 -> {
                fadeToBlack()
                timedUpdate(6)
            }

            1 -> {
                teleport(player, 4, 19)
                face(player, getNPC(NPCs.JORRAL_2932)!!)
                sendDialogue(player, "With many occupants over the years,")
                moveCamera(0, 28, 300, 1)
                fadeFromBlack()
                timedUpdate(3)
            }

            2 -> {
                moveCamera(0, 23, 300, 1)
                timedUpdate(3)
            }

            3 -> {
                moveCamera(4, 19, 300, 1)
                rotateCamera(4, 19, 300, 1)
                timedUpdate(1)
            }

            4 -> {
                dialogueUpdate("the building has seen much action.")
            }

            5 -> {
                moveCamera(9, 24, 300, 1)
                rotateCamera(5, 16, 300, 1)
                timedUpdate(3)
            }

            6 -> {
                moveCamera(9, 24, 300, 1)
                rotateCamera(4, 30, 500, 1)
                timedUpdate(3)
            }

            7 -> {
                dialogueUpdate("It started life as an outpost.")
            }

            8 -> {
                moveCamera(0, 7, 400, 1)
                rotateCamera(4, 19, 400, 1)
                timedUpdate(8)
            }

            9 -> {
                dialogueUpdate("Its sole purpose to see incoming armies,")
            }

            10 -> {
                moveCamera(13, 31, 400, 1)
                rotateCamera(13, 31, 400, 1)
                addNPC(NPCs.HOBGOBLIN_123, 12, 30, Direction.WEST)
                addNPC(NPCs.HOBGOBLIN_122, 13, 31, Direction.SOUTH)
                addNPC(NPCs.HOBGOBLIN_123, 15, 33, Direction.EAST)
                timedUpdate(4)
            }

            11 -> {
                move(getNPC(NPCs.HOBGOBLIN_122)!!, 12, 29)
                dialogueUpdate("before they saw the city of Ardougne.")
            }

            12 -> {
                end().also {
                    runTask(player, 18) {
                        openDialogue(player, JorralDialogueExtension())
                    }
                }
            }
        }
    }
}
