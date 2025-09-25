package content.region.misthalin.varrock.quest.firedup.dialogue

import content.minigame.allfiredup.plugin.BeaconState
import core.api.getQuestStage
import core.api.openDialogue
import core.api.setQuestStage
import core.api.setVarbit
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.game.world.GameWorld
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs
import shared.consts.Quests
import shared.consts.Vars

/**
 * Represents the Squire Fyre (All fired up) dialogue.
 */
@Initializable
class SquireFyreDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        val questStage = getQuestStage(player, Quests.ALL_FIRED_UP)
        if(!GameWorld.settings!!.isMembers) {
            player("Hello there. Could I ask you about the-").also { stage = 7 }
        } else if(questStage == 100) {
            npcl(FaceAnim.FRIENDLY, "Hello again! And what might I be able to do for you today?").also { stage = 14 }
        } else if(questStage == 40) {
            player(FaceAnim.FRIENDLY, "Hi there. I'm helping Blaze and King Roald test the", "beacon network. Can you see it from here? Blaze said", "you have pretty sharp eyes.")
        } else {
            playerl(FaceAnim.HALF_THINKING, "So, I'm curious. What are you doing up here and what's that interesting contraption?").also { stage = 10 }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> npc("Of course I can see it. I haven't spent my entire life", "practising my seeing skills for nothing! I'm happy to", "report that the fire near Blaze is burning brightly.").also { stage++ }
            1 -> player("Terrific! Blaze has asked me to light this fire as well, so", "he can see how things look from his vantage point.").also { stage++ }
            2 -> npc("Be my guest!").also {
                stage++
                setQuestStage(player, Quests.ALL_FIRED_UP, 50)
                setVarbit(player, Vars.VARBIT_BEACON_RIVER_SALVE_5146, BeaconState.DYING.ordinal)
            }
            3 -> options("How do I light the beacon?", "I suppose you don't have any logs I could have?", "Okay, thanks.").also { stage++ }
            4 -> when (buttonId) {
                1 -> player("How do I light the beacon?").also { stage++ }
                2 -> player("I suppose you don't have any logs I could have?").also { stage = 6 }
                3 -> player("Okay, thanks").also { stage = END_DIALOGUE }
            }
            5 -> npc("Put in 20 logs of the same kind, and then light it.").also { stage = END_DIALOGUE }
            6 -> npc("No, I do not.").also { stage = END_DIALOGUE }

            7 -> npcl(FaceAnim.HALF_GUILTY, "Not at the moment, I'm afraid! It's not nearly safe enough to talk right now.").also { stage++ }
            8 -> playerl(FaceAnim.FRIENDLY, "But you don't even know what I was going to ask!").also { stage++ }
            9 -> npcl(FaceAnim.NEUTRAL, "Oh, I have enough of an idea to know that it's not safe enough to talk about it on a free world.").also { stage = END_DIALOGUE }

            10 -> npcl(FaceAnim.FRIENDLY, "Well, it's all part of King Roald's new security initiative.").also { stage++ }
            11 -> playerl(FaceAnim.HALF_ASKING, "Security initiative? That sounds intriguing. What's going on?").also { stage++ }
            12 -> npcl(FaceAnim.HALF_GUILTY, "I'm afraid that I can't discuss it in detail at present, but if you're that interested, have a chat with the king. Last I heard, we needed some extra hands on deck.").also { stage++ }
            13 -> player("Thanks, I just might.").also { stage = END_DIALOGUE }
            14 -> {
                end()
                openDialogue(player, SquireFyreDialogueFile())
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.SQUIRE_FYRE_8042, NPCs.SQUIRE_FYRE_8043)
}
