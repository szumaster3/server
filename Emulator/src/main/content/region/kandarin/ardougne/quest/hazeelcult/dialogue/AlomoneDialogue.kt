package content.region.kandarin.ardougne.quest.hazeelcult.dialogue

import core.api.getAttribute
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class AlomoneDialogue(player: Player? = null) : Dialogue(player) {


    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        val questStage = getQuestStage(player, Quests.HAZEEL_CULT)
        val hasMahjarrat = getAttribute(player, "hazeelcult:mahjarrat", true)
        val hasCarnillean = getAttribute(player, "hazeelcult:carnillean", true)

        when {
            questStage >= 2 -> npcl(FaceAnim.FRIENDLY, "How did YOU get in here?")
            hasMahjarrat && !hasCarnillean -> playerl(FaceAnim.FRIENDLY, "Hello, Alomone.")
            else -> playerl(FaceAnim.FRIENDLY, "Hello again.")
        }
        return true
    }

    override fun handle(
        componentID: Int,
        buttonID: Int,
    ): Boolean {
        when (getQuestStage(player!!, Quests.HAZEEL_CULT)) {
            2 -> when (stage) {
                1 -> playerl(FaceAnim.FRIENDLY, "I've come for the Carnillean family armour. Hand it over, or face the consequences.").also { stage++ }
                2 -> npcl(FaceAnim.FRIENDLY, "I thought I made it clear to the butler you could not be allowed to interfere with our mission. The incompetent fool must be going soft.").also { stage++ }
                3 -> playerl(FaceAnim.FRIENDLY, "So, the butler's part of your sordid little cult, huh? Why is it ALWAYS butler? I should have known...").also { stage++ }
                4 -> npcl(FaceAnim.ANNOYED, "Well, you won't live long enough to tell anyone! Prepare to DIE!!!").also { stage++ }
                5 -> {
                    end()
                    setQuestStage(player, Quests.HAZEEL_CULT, 3)
                    npc!!.attack(player!!)
                }
            }

            100 -> when (stage) {
                1 -> npcl(FaceAnim.NEUTRAL, "Welcome, adventurer. Know that as a friend to Hazeel, you are always welcome here.").also { stage = END_DIALOGUE }
                2 -> npcl(FaceAnim.ANNOYED, "You have crossed my path too many times intruder. Leave or face my wrath.").also { stage++ }
                3 -> playerl(FaceAnim.FRIENDLY, "Yeah, whatever.").also { stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.ALOMONE_891)
}
