package content.region.asgarnia.falador.quest.rd.plugin

import content.region.asgarnia.falador.quest.rd.RecruitmentDrive
import content.region.asgarnia.falador.quest.rd.cutscene.FailCutscene
import core.api.*
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

@Initializable
class HynnTerprettDialogue(player: Player? = null) : Dialogue(player) {
    companion object {
        const val ATTRIBUTE_RANDOM_RIDDLE = "rd:randomriddle"
        const val ATTRIBUTE_CORRECT_ANSWER = "rd:recentlycorrect"
    }

    override fun open(vararg args: Any?): Boolean {
        val player = player ?: return false

        when {
            getAttribute(player, ATTRIBUTE_CORRECT_ANSWER, false) -> stage = 100
            getAttribute(player, RecruitmentDrive.stageFail, false) -> stage = 200
            getAttribute(player, RecruitmentDrive.stagePass, false) -> stage = 300
            else -> {
                stage = 0
                if (getAttribute(player, ATTRIBUTE_RANDOM_RIDDLE, 5) == 5) {
                    setAttribute(player, ATTRIBUTE_RANDOM_RIDDLE, (0..4).random())
                }
            }
        }
        return handle(0, 0)
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        val player = player ?: return false
        when (stage) {
            0 -> {
                npc("Greetings, ${player.username}. I am here to test your wits with a simple riddle.")
                stage = getAttribute(player, ATTRIBUTE_RANDOM_RIDDLE, 0) + 10
            }
            10 -> npc("Here is my riddle: I estimate there to be one million inhabitants in the world of Gielinor; creatures and people both. What number would you get if you multiply the number of fingers on everything's left hand, to the nearest million?").also { stage++ }
            11 -> sendInputDialogue(player, true, "Enter the amount:") { value: Any ->
                if (value == 5000000) {
                    setAttribute(player, ATTRIBUTE_CORRECT_ANSWER, true)
                } else {
                    setAttribute(player, RecruitmentDrive.stageFail, true)
                }
                openDialogue(player, HynnTerprettDialogue(), NPC(NPCs.MS_HYNN_TERPRETT_2289))
            }
            12 -> npc("Here is my riddle: Which of the following statements is true?").also { stage++ }
            13 -> showTopics(
                Topic("The number of false statements here is one.", 220),
                Topic("The number of false statements here is two.", 220),
                Topic("The number of false statements here is three.", 120),
                Topic("The number of false statements here is four.", 220)
            )
            14 -> npc("Here is my riddle: I have both a husband and daughter. My husband is four times older than my daughter. In twenty years time, he will be twice as old as my daughter. How old is my daughter now?").also { stage++ }
            15 -> sendInputDialogue(player, true, "Enter the amount:") { value: Any ->
                if (value == 10) {
                    setAttribute(player, ATTRIBUTE_CORRECT_ANSWER, true)
                } else {
                    setAttribute(player, RecruitmentDrive.stageFail, true)
                }
                openDialogue(player, HynnTerprettDialogue(), NPC(NPCs.MS_HYNN_TERPRETT_2289))
            }
            16 -> npc("Here is my riddle: Imagine that you have been captured by an enemy. You are to be killed, but in a moment of mercy, the enemy has allowed you to pick your own demise. Your first choice is to be drowned in a lake of acid. Your second choice is to be burned on a fire. Your third choice is to be thrown to a pack of wolves that have not been fed in over a month. Your final choice of fate is to be thrown from the walls of a castle, many hundreds of feet high. Which fate would you be wise to choose?").also { stage++ }
            17 -> {
                showTopics(
                    Topic("The lake of acid.", 220),
                    Topic("The large fire.", 220),
                    Topic("The wolves.", 120),
                    Topic("The castle walls.", 220)
                )
            }
            18 -> npc("Here is my riddle: I dropped four identical stones, into four identical buckets, each containing an identical amount of water. The first bucket's water was at 32 degrees Fahrenheit, the second was at 33 degrees, the third at 34 and the fourth was at 35 degrees.").also { stage++ }
            19 -> {
                showTopics(
                    Topic("Bucket A (32 degrees)", 120),
                    Topic("Bucket B (33 degrees)", 220),
                    Topic("Bucket C (34 degrees)", 220),
                    Topic("Bucket D (35 degrees)", 220),
                )
            }
            100, 120 -> {
                removeAttribute(player, ATTRIBUTE_CORRECT_ANSWER)
                if (!getAttribute(player, RecruitmentDrive.stageFail, false)) {
                    setAttribute(player, RecruitmentDrive.stagePass, true)
                    removeAttribute(player, ATTRIBUTE_RANDOM_RIDDLE)
                }
                npc("Excellent work, ${player.username}", "Please step through the portal to meet your next challenge.").also { stage = END_DIALOGUE }
            }
            200, 220 -> {
                removeAttribute(player, ATTRIBUTE_RANDOM_RIDDLE)
                setAttribute(player, RecruitmentDrive.stagePass, false)
                setAttribute(player, RecruitmentDrive.stageFail, false)
                npc("No... I am very sorry.", "Apparently you are not up to the challenge.", "I will return you where you came from, better luck in the future.")
                runTask(player, 3) { FailCutscene(player).start() }
                end()
            }
            300 -> {
                npc(FaceAnim.NEUTRAL, "You certainly have the wits to be a Temple Knight.", "Pass on through the portal to find your next challenge.").also { stage = END_DIALOGUE }
            }
        }

        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.MS_HYNN_TERPRETT_2289)

}
