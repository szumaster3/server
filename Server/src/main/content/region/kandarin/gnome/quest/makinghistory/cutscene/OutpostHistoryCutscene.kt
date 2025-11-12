package content.region.kandarin.gnome.quest.makinghistory.cutscene

import content.region.kandarin.gnome.quest.makinghistory.MHUtils
import core.api.*
import core.game.activity.Cutscene
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.map.Direction
import core.tools.END_DIALOGUE
import shared.consts.NPCs
import shared.consts.Quests

class OutpostHistoryCutscene(player: Player) : Cutscene(player) {

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
            4 -> dialogueUpdate(true, "the building has seen much action.")
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

            7 -> dialogueUpdate(true, "It started life as an outpost.")
            8 -> {
                moveCamera(0, 7, 400, 1)
                rotateCamera(4, 19, 400, 1)
                timedUpdate(8)
            }
            9 -> dialogueUpdate(true, "Its sole purpose to see incoming armies,")
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
                dialogueUpdate(true, "before they saw the city of Ardougne.")
            }

            12 -> {
                setQuestStage(player, Quests.MAKING_HISTORY, 1)
                setVarbit(player, MHUtils.PROGRESS, 1, true)
                end {
                    runTask(player, 18) {
                        openDialogue(player, OutpostHistoryDialogue())
                    }
                }
            }
        }
    }
}

private class OutpostHistoryDialogue : DialogueFile() {
    override fun handle(componentID: Int, buttonID: Int) {
        npc = NPC(NPCs.JORRAL_2932)
        when (stage) {
            0 -> npcl(FaceAnim.HALF_GUILTY, "If all goes well, I hope to be able to turn it into a museum as a monument to the area's history. What do you think?").also { stage++ }
            1 -> options("Ok, I'll make a stand for history!", "I don't care about some dusty building").also { stage++ }
            2 -> when (buttonID) {
                1 -> playerl(FaceAnim.HALF_GUILTY, "OK, I will make a stand for history!").also { stage++ }
                2 -> playerl(FaceAnim.HALF_GUILTY, "I don't care about some dusty building").also { stage = 4 }
            }
            3 -> npcl(FaceAnim.HAPPY, "Oh, thank you so much, you really are my saviour!").also { stage = END_DIALOGUE }
            4 -> npc("It's doomed. DOOMED!").also { stage = END_DIALOGUE }
        }
    }
}

