package content.region.kandarin.seers.dialogue

import content.region.kandarin.seers.quest.scorpcatcher.dialogue.SeerMirrorDialogue
import core.api.isQuestInProgress
import core.api.openDialogue
import core.api.sendItemDialogue
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.Diary
import core.game.node.entity.player.link.diary.DiaryType
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs
import shared.consts.Quests

/**
 * Represents the Seer's dialogue.
 *
 * # Relations
 * - [Scorpion Catcher quest][content.region.kandarin.quest.scorpcatcher.ScorpionCatcher]
 * - [Seers Achievement Diary][content.region.kandarin.handlers.seers.SeersVillageAchievementDiary]
 */
@Initializable
class SeersDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        options("Talk about something else.", "Talk about achievement diary.")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {

            /*
             * Diary dialogue.
             */

            0 -> when (buttonId) {
                1 -> npc("Many greetings.").also { stage++ }
                2 -> if (Diary.canReplaceReward(player, DiaryType.SEERS_VILLAGE, 0)) {
                    player("I seem to have lost my seers' headband...").also { stage = 80 }
                } else if (Diary.hasClaimedLevelRewards(player, DiaryType.SEERS_VILLAGE, 0)) {
                    player("Can you remind me what my headband does?").also { stage = 90 }
                } else if (Diary.canClaimLevelRewards(player, DiaryType.SEERS_VILLAGE, 0)) {
                    player("Hi. I've completed the Easy tasks in my Achievement", "Diary.").also { stage = 200 }
                } else {
                    player("Hi! Can you help me out with the Achievement Diary", "tasks?").also { stage = 101 }
                }
            }

            /*
             * Scorpion catcher dialogue.
             */

            1 -> {
                if (isQuestInProgress(player, Quests.SCORPION_CATCHER, 10, 99)) {
                    end()
                    openDialogue(player, SeerMirrorDialogue())
                } else {
                    options("Many greetings.", "I seek knowledge and power!").also { stage++ }
                }
            }
            2 -> when (buttonId) {
                1 -> player("Many greetings.").also { stage = 10 }
                2 -> player("I seek knowledge and power!").also { stage = 20 }
            }
            10 -> npc("Remember, whenever you set out to do something,", "something else must be done first.").also { stage = 999 }
            20 -> npc("Knowledge comes from experience, power", "comes from battleaxes.").also { stage = END_DIALOGUE }
            80 -> {
                Diary.grantReplacement(player, DiaryType.SEERS_VILLAGE, 0)
                npc("Here's your replacement. Please be more careful.").also { stage = END_DIALOGUE }
            }
            90 -> npc("Your headband marks you as an honorary seer.", "Geoffrey - who works in the field to the", "south - will give you free flax every day.").also { stage = 203 }
            100 -> npc("I certainly do - we have a set of tasks spanning Seers'", "Village, Catherby, Hemenster and the Sinclair Mansion.", "Just complete the tasks listed in the Achievement Diary", "and they will be ticked off automatically.").also { stage = 999 }
            101 -> npc("I'm afraid not. It is important that adventurers", "complete the tasks unaided. That way, only the truly", "worthy collect the spoils.").also { stage = 999 }
            200 -> npc("Well done, adventurer. You are clearly a " + (if (player.isMale) "man" else "woman") + "of", "great wisdom. I have a gift for you.").also { stage++ }

            201 -> {
                if (!Diary.flagRewarded(player, DiaryType.SEERS_VILLAGE, 0)) return true
                sendItemDialogue(player, Diary.getRewards(DiaryType.SEERS_VILLAGE, 0)[0], "The seer hands you a strange-looking headband and a rusty lamp.").also { stage++ }
            }

            202 -> npc("You are now an honorary seer and Geoffrey - who", "works in the field to the south - will give you free flax", "every day. Don't call him 'Geoffrey' though: he prefers", "to be known as 'Flax'.").also { stage++ }
            203 -> player(FaceAnim.NOD_NO, "Is that it?").also { stage++ }
            204 -> npc("No, no. As well as Flax giving you 30 flax per day,", "you'll be able to see dimly in the dark and your", "defence against magic attacks will be boosted.").also { stage++ }
            205 -> npc("Stankers will allow 140 pieces of coal in his coal truck,", "and you'll get an extra log when cutting normal trees.").also { stage++ }
            206 -> player(FaceAnim.SUSPICIOUS, "Flax? What sort of name is that for a person?").also { stage++ }
            207 -> npc(FaceAnim.NOD_NO, "I know, I know. The poor boy is a simple soul - he just", "really loves picking flax. A little too much, I fear.").also { stage = 999 }
            999 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = SeersDialogue(player)
    override fun getIds(): IntArray = intArrayOf(NPCs.SEER_388)
}
