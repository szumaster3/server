package content.region.kandarin.quest.elena.dialogue

import core.api.*
import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class BravekDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun handle(
        componentID: Int,
        buttonID: Int,
    ): Boolean {
        when (getQuestStage(player!!, Quests.PLAGUE_CITY)) {
            13 ->
                when (stage) {
                    0 -> npcl(FaceAnim.NEUTRAL, "My head hurts! I'll speak to you another day...").also { stage++ }
                    1 -> options("This is really important though!", "Ok, goodbye.").also { stage++ }
                    2 ->
                        when (buttonID) {
                            1 -> playerl(FaceAnim.FRIENDLY, "This is really important though!").also { stage = 3 }
                            2 -> player("Ok, goodbye.").also { stage = END_DIALOGUE }
                        }

                    3 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "I can't possibly speak to you with my head spinning like this... I went a bit heavy on the drink last night.",
                        ).also { stage++ }

                    4 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Curse my herbalist, she made the best hangover cures. Darn inconvenient of her catching the plague.",
                        ).also { stage++ }

                    5 ->
                        options(
                            "Okay, goodbye.",
                            "You shouldn't drink so much then!",
                            "Do you know what's in the cure?",
                        ).also { stage++ }

                    6 ->
                        when (buttonID) {
                            1 -> playerl(FaceAnim.FRIENDLY, "Okay, goodbye.").also { stage = END_DIALOGUE }
                            2 -> playerl(FaceAnim.FRIENDLY, "You shouldn't drink so much then!").also { stage = 7 }
                            3 -> playerl(FaceAnim.FRIENDLY, "Do you know what's in the cure?").also { stage = 20 }
                        }

                    7 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Well positions of responsibility are hard, I need something to take my mind off things... Especially with the problems this place has..",
                        ).also { stage++ }

                    8 -> playerl(FaceAnim.FRIENDLY, "I don't think drink is the solution.").also { stage++ }
                    9 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Uurgh! My head still hurts too much to think straight. Oh for one of Trudi's hangover cures!",
                        ).also { stage++ }

                    10 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "I'll see what I can do I suppose. Mr. Bravek, there's someone here who really needs to speak to you.",
                        ).also { stage++ }

                    11 ->
                        sendNPCDialogue(
                            player!!,
                            NPCs.BRAVEK_711,
                            "I suppose they can come in then. If they keep it short.",
                        ).also { stage = END_DIALOGUE }

                    20 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Hmmm let me think... Ouch! Thinking isn't clever. Ah here, she did scribble it down for me.",
                        ).also { stage++ }

                    21 -> {
                        if (freeSlots(player!!) == 0) {
                            end()
                            sendItemDialogue(
                                player!!,
                                Items.A_SCRUFFY_NOTE_1508,
                                "Bravek waves a tatty piece of paper at you, but you don't have room to take it.",
                            ).also { stage = END_DIALOGUE }
                        } else {
                            end()
                            sendItemDialogue(
                                player!!,
                                Items.A_SCRUFFY_NOTE_1508,
                                "Bravek hands you a tatty piece of paper.",
                            ).also { stage++ }
                            addItem(player!!, Items.A_SCRUFFY_NOTE_1508)
                            setQuestStage(player!!, Quests.PLAGUE_CITY, 14)
                        }
                    }
                }

            14 ->
                when (stage) {
                    0 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "Uurgh! My head still hurts too much to think straight. Oh for one of Trudi's hangover cures!",
                        ).also { stage++ }

                    1 -> {
                        if (removeItem(player!!, Items.HANGOVER_CURE_1504)) {
                            playerl(FaceAnim.FRIENDLY, "Try this.")
                            setAttribute(player, "/save:elena:cure", true)
                            stage++
                        } else if (getAttribute(player, "elena:cure", false)) {
                            npcl(
                                FaceAnim.NEUTRAL,
                                "Ooh that's much better! Thanks, that's the clearest my head has felt in a month. Ah now, what was it you wanted me to do for you?",
                            ).also { stage++ }
                            stage = 5
                        } else {
                            end()
                        }
                    }

                    2 -> {
                        sendItemDialogue(player!!, Items.HANGOVER_CURE_1504, "You give Bravek the hangover cure.")
                        animate(npc!!, Animations.HUMAN_DRINK_KEG_OF_BEER_1330)
                        sendChat(npc!!, "Grruurgh!", 1)
                        stage++
                    }

                    3 -> sendDialogue(player!!, "Bravek gulps down the foul-looking liquid.").also { stage++ }
                    4 ->
                        npcl(
                            FaceAnim.NEUTRAL,
                            "Ooh that's much better! Thanks, that's the clearest my head has felt in a month. Ah now, what was it you wanted me to do for you?",
                        ).also { stage++ }

                    5 ->
                        playerl(
                            FaceAnim.FRIENDLY,
                            "I need to rescue a kidnap victim named Elena. She's being held in a plague house, I need permission to enter.",
                        ).also { stage++ }

                    6 ->
                        options(
                            "Ok, I'll go speak to them.",
                            "Is that all anyone says around here?",
                            "They won't listen to me!",
                        ).also { stage++ }

                    7 ->
                        when (buttonID) {
                            1 -> playerl(FaceAnim.FRIENDLY, "Ok, I'll go speak to them.").also { stage = 6 }
                            2 -> playerl(FaceAnim.FRIENDLY, "Is that all anyone says around here?").also { stage = 11 }
                            3 ->
                                playerl(
                                    FaceAnim.FRIENDLY,
                                    "They won't listen to me! They say I'm not properly equipped to go in the house, though I do have a very effective gasmask.",
                                ).also { stage++ }
                        }

                    8 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Hmmm, well I guess they're not taking the issue of a kidnapping seriously enough. They do go a bit far sometimes.",
                        ).also { stage++ }

                    9 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "I've heard of Elena, she has helped us a lot... Ok, I'll give you this warrant to enter the house.",
                        ).also { stage = 17 }

                    11 -> npcl(FaceAnim.FRIENDLY, "Well, they know best about plague issues.").also { stage++ }
                    12 ->
                        playerl(
                            FaceAnim.FRIENDLY,
                            "Don't you want to take an interest in it at all?",
                        ).also { stage++ }
                    13 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Nope, I don't wish to take a deep interest in plagues. That stuff is too scary for me!",
                        ).also { stage++ }

                    14 -> playerl(FaceAnim.FRIENDLY, "I can see why people say you're a weak leader.").also { stage++ }
                    15 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Bah, people always criticise their leaders but delegating is the only way to lead. I delegate all plague issues to the mourners.",
                        ).also { stage++ }

                    16 -> playerl(FaceAnim.FRIENDLY, "This whole city is a plague issue!").also { stage = 6 }
                    17 -> {
                        if (freeSlots(player!!) == 0) {
                            end()
                            sendItemDialogue(
                                player!!,
                                Items.WARRANT_1503,
                                "Bravek waves a warrant at you, but you don't have room to take it.",
                            )
                        } else {
                            end()
                            sendItemDialogue(player!!, Items.WARRANT_1503, "Bravek hands you a warrant.")
                            addItem(player!!, Items.WARRANT_1503)
                            setQuestStage(player!!, Quests.PLAGUE_CITY, 16)
                        }
                    }
                }

            in 16..100 ->
                when (stage) {
                    0 -> npcl(FaceAnim.NEUTRAL, "Thanks again for the hangover cure.").also { stage++ }
                    1 -> playerl(FaceAnim.NEUTRAL, "Not a problem, happy to help out.").also { stage++ }
                    2 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "I'm just having a little drop of whisky, then I'll feel really good.",
                        ).also { stage = END_DIALOGUE }
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.BRAVEK_711)
    }
}
