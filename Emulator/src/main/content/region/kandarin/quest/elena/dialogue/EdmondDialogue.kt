package content.region.kandarin.quest.elena.dialogue

import content.region.kandarin.quest.elena.handlers.PlagueCityListener
import content.region.kandarin.quest.elena.cutscene.UndergroundCutscene
import core.api.*
import core.api.quest.finishQuest
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class EdmondDialogue(
    player: Player? = null,
) : Dialogue(player) {
    val value = 0

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (inEquipmentOrInventory(player, Items.GAS_MASK_1506) && (getQuestStage(player, Quests.PLAGUE_CITY) == 2)) {
            playerl(FaceAnim.FRIENDLY, "Hi Edmond, I've got the gas mask now.").also { stage++ }
        } else if (getQuestStage(player, Quests.PLAGUE_CITY) > 2) {
            playerl(FaceAnim.FRIENDLY, "Hello Edmond.").also { stage++ }
        } else {
            playerl(FaceAnim.FRIENDLY, "Hello old man.").also { stage++ }
        }
        return true
    }

    override fun handle(
        componentID: Int,
        buttonID: Int,
    ): Boolean {
        val bucketUses = getAttribute(player, PlagueCityListener.BUCKET_USES_ATTRIBUTE, value)
        var hasAnScroll = hasAnItem(player, Items.A_MAGIC_SCROLL_1505).container != null
        when (getQuestStage(player!!, Quests.PLAGUE_CITY)) {
            0 ->
                when (stage) {
                    1 -> npcl(FaceAnim.NEUTRAL, "Sorry, I can't stop to talk...").also { stage++ }
                    2 -> playerl(FaceAnim.HALF_ASKING, "Why, what's wrong?").also { stage++ }
                    3 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "I've got to find my daughter. I pray that she is still alive...",
                        ).also { stage++ }

                    4 -> options("What's happened to her?", "Well, good luck finding her.").also { stage++ }
                    5 ->
                        when (buttonID) {
                            1 -> playerl(FaceAnim.FRIENDLY, "What's happened to her?").also { stage = 6 }
                            2 ->
                                playerl(
                                    FaceAnim.FRIENDLY,
                                    "Well, good luck finding her.",
                                ).also { stage = END_DIALOGUE }
                        }

                    6 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "Elena's a missionary and a healer. Three weeks ago she managed to cross the Ardougne wall...",
                        ).also { stage++ }

                    7 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "No-one's allowed to cross the wall in case they spread the plague. But after hearing the screams of suffering she felt she had to help.",
                        ).also { stage++ }

                    8 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "She said she'd be gone for a few days but we've heard nothing since.",
                        ).also { stage++ }

                    9 ->
                        options(
                            "Tell me more about the plague.",
                            "Can I help find her?",
                            "I'm sorry, I have to go.",
                        ).also { stage++ }

                    10 ->
                        when (buttonID) {
                            1 -> playerl(FaceAnim.FRIENDLY, "Tell me more about the plague.").also { stage = 12 }
                            2 -> playerl(FaceAnim.FRIENDLY, "Can I help find her?").also { stage = 13 }
                            3 -> playerl(FaceAnim.FRIENDLY, "I'm sorry, I have to go.").also { stage = END_DIALOGUE }
                        }

                    12 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "The mourners can tell you more than me. They're the only ones allowed to cross the border. I do know the plague is a horrible way to go... That's why Elena felt she had to go help.",
                        ).also { stage = 9 }

                    13 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Really, would you? I've been working on a plan to get into West Ardougne, but I'm too old and tired to carry it through.",
                        ).also { stage++ }

                    14 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "If you're going into West Ardougne you'll need protection from the plague. My wife made a special gas mask for Elena with dwellberries rubbed into it.",
                        ).also { stage++ }

                    15 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Dwellberries help repel the virus! We need some more though...",
                        ).also { stage++ }

                    16 -> playerl(FaceAnim.ASKING, "Where can I find these dwellberries?").also { stage++ }
                    17 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "The only place I know of is McGrubor's Wood just north of the Rangers' Guild.",
                        ).also { stage++ }

                    18 -> playerl(FaceAnim.FRIENDLY, "Ok, I'll go and get some.").also { stage++ }
                    19 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "The foresters keep a close eye on it, but there is a back way in.",
                        ).also { stage++ }

                    20 -> {
                        end()
                        setQuestStage(player!!, Quests.PLAGUE_CITY, 1)
                    }
                }

            1 ->
                when (stage) {
                    1 -> npcl(FaceAnim.NEUTRAL, "Have you got the dwellberries yet?").also { stage++ }
                    2 ->
                        if (!inInventory(player, Items.DWELLBERRIES_2126)) {
                            playerl(FaceAnim.FRIENDLY, "Sorry, I'm afraid not.").also { stage = 3 }
                        } else {
                            playerl(FaceAnim.FRIENDLY, "Yes I've got some here.").also { stage = 6 }
                        }

                    3 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "You'll probably find them in McGrubor's Wood it's just west of Seers village.",
                        ).also { stage++ }

                    4 -> playerl(FaceAnim.NEUTRAL, "Ok, I'll go and get some.").also { stage++ }
                    5 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "The foresters keep a close eye on it, but there is a back way in.",
                        ).also { stage = END_DIALOGUE }

                    6 ->
                        npcl(FaceAnim.NEUTRAL, "Take them to my wife Alrena, she's inside.").also {
                            stage =
                                END_DIALOGUE
                        }
                }

            2 ->
                when (stage) {
                    1 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "Good stuff, now for the digging. Beneath us are the Ardougne sewers, there you'll find the access to West Ardougne.",
                        ).also { stage++ }

                    2 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "The problem is the soil is rock hard. You'll need to pour on several buckets of water to soften it up. I'll keep an eye out for the mourners.",
                        ).also { stage++ }

                    3 -> {
                        end()
                        setQuestStage(player!!, Quests.PLAGUE_CITY, 3)
                    }
                }

            3 ->
                when (stage) {
                    1 ->
                        if (getVarbit(player, PlagueCityListener.MUD_PATCH_VARBIT) == 1) {
                            playerl(FaceAnim.NEUTRAL, "I've soaked the soil with water.").also { stage = 3 }
                        } else {
                            npcl(FaceAnim.FRIENDLY, "How's it going?").also { stage = 2 }
                        }

                    2 ->
                        playerl(
                            FaceAnim.NEUTRAL,
                            "I still need to pour " + (4 - bucketUses) + " more buckets of water on the soil.",
                        ).also { stage = END_DIALOGUE }

                    3 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "That's great, it should be soft enough to dig through now.",
                        ).also { stage = END_DIALOGUE }
                }

            4 ->
                when (stage) {
                    1 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "I think it's the pipe to the south that comes up in West Ardougne.",
                        ).also { stage++ }

                    2 -> playerl(FaceAnim.NEUTRAL, "Alright I'll check it out.").also { stage++ }
                    3 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "Once you're in the city look for a man called Jethick, he's an old friend and should help you. Send him my regards, I haven't seen him since before Elena was born.",
                        ).also { stage++ }

                    4 -> playerl(FaceAnim.NEUTRAL, "Alright, thanks I will.").also { stage = END_DIALOGUE }
                }

            5 ->
                when (stage) {
                    1 ->
                        playerl(
                            FaceAnim.NEUTRAL,
                            "Edmond, I can't get through to West Ardougne! There's an iron grill blocking my way, I can't pull it off alone.",
                        ).also { stage++ }

                    2 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "If you get some rope you could tie to the grill, then we could both pull it at the same time.",
                        ).also { stage = END_DIALOGUE }
                }

            6 ->
                when (stage) {
                    1 ->
                        playerl(
                            FaceAnim.NEUTRAL,
                            "I've tied a rope to the grill over there, will you help me pull it off?",
                        ).also { stage++ }

                    2 -> npcl(FaceAnim.NEUTRAL, "Alright, let's get to it...").also { stage++ }
                    3 -> {
                        end()
                        UndergroundCutscene(player!!).start()
                    }
                }

            7 ->
                when (stage) {
                    1 -> npcl(FaceAnim.NEUTRAL, "Have you found Elena yet?").also { stage++ }
                    2 -> playerl(FaceAnim.NEUTRAL, "Not yet, it's a big city over there.").also { stage++ }
                    3 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Don't forget to look for my friend Jethick. He may be able to help.",
                        ).also { stage = END_DIALOGUE }
                }

            8 ->
                when (stage) {
                    1 -> npcl(FaceAnim.NEUTRAL, "Have you found Elena yet?").also { stage++ }
                    2 ->
                        playerl(
                            FaceAnim.NEUTRAL,
                            "Not yet, it's a big city over there. Do you have a picture of Elena?",
                        ).also { stage++ }

                    3 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "There should be a picture of Elena in the house. Please find her quickly, I hope it's not too late.",
                        ).also { stage = END_DIALOGUE }
                }

            99 ->
                when (stage) {
                    1 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "Thank you, thank you! Elena beat you back by minutes.",
                        ).also { stage++ }
                    2 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "Now I said I'd give you a reward. What can I give you as a reward I wonder?",
                        ).also { stage++ }

                    3 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "Here take this magic scroll, I have little use for it but it may help you.",
                        ).also { stage++ }

                    4 -> {
                        end()
                        finishQuest(player, Quests.PLAGUE_CITY)
                    }
                }

            100 ->
                when (stage) {
                    1 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Ah hello again, and thank you again for rescuing my daughter.",
                        ).also { stage++ }

                    2 ->
                        if (!hasAnScroll) {
                            options("Do you have any more of those scrolls?", "No problem.").also { stage++ }
                        } else {
                            playerl(FaceAnim.NEUTRAL, "No problem.").also { stage = END_DIALOGUE }
                        }

                    3 ->
                        when (buttonID) {
                            1 -> playerl(FaceAnim.NEUTRAL, "Do you have any more of those scrolls?").also { stage++ }
                            2 -> playerl(FaceAnim.NEUTRAL, "No problem.").also { stage = END_DIALOGUE }
                        }

                    4 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Here take this magic scroll, I have little use for it but it may help you.",
                        ).also {
                            addItemOrDrop(player!!, Items.A_MAGIC_SCROLL_1505)
                            stage = END_DIALOGUE
                        }
                }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.EDMOND_714)
}
