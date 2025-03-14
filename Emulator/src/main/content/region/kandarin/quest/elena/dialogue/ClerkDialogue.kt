package content.region.kandarin.quest.elena.dialogue

import core.api.quest.getQuestStage
import core.api.quest.setQuestStage
import core.api.sendNPCDialogue
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs
import org.rs.consts.Quests

@Initializable
class ClerkDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npcl(
            FaceAnim.NEUTRAL,
            "Hello, welcome to the Civic Office of West Ardougne. How can I help you?",
        ).also { stage = 0 }
        return true
    }

    override fun handle(
        componentID: Int,
        buttonID: Int,
    ): Boolean {
        when (getQuestStage(player!!, Quests.PLAGUE_CITY)) {
            11 ->
                when (stage) {
                    0 -> options("Who is through that door?", "I'm just looking thanks.").also { stage++ }
                    1 ->
                        when (buttonID) {
                            1 -> playerl(FaceAnim.FRIENDLY, "Who is through that door?").also { stage = 2 }
                            2 -> playerl(FaceAnim.FRIENDLY, "I'm just looking thanks.").also { stage = END_DIALOGUE }
                        }

                    2 -> npcl(FaceAnim.FRIENDLY, "The city warder Bravek is in there.").also { stage++ }
                    3 -> playerl(FaceAnim.FRIENDLY, "Can I go in?").also { stage++ }
                    4 -> npcl(FaceAnim.FRIENDLY, "He has asked not to be disturbed.").also { stage = END_DIALOGUE }
                }

            12 ->
                when (stage) {
                    0 ->
                        options(
                            "I need permission to enter a plague house.",
                            "Who is through that door?",
                            "I'm just looking thanks.",
                        ).also { stage++ }

                    1 ->
                        when (buttonID) {
                            1 ->
                                playerl(FaceAnim.FRIENDLY, "I need permission to enter a plague house.").also {
                                    stage =
                                        2
                                }
                            2 -> playerl(FaceAnim.FRIENDLY, "Who is through that door?").also { stage = 12 }
                            3 -> playerl(FaceAnim.FRIENDLY, "I'm just looking thanks.").also { stage = END_DIALOGUE }
                        }

                    2 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Rather you than me! The mourners usually deal with that stuff, you should speak to them. Their headquarters are right near the city gate.",
                        ).also { stage++ }

                    3 ->
                        options(
                            "I'll try asking them then.",
                            "Surely you don't let them run everything for you?",
                        ).also { stage++ }

                    4 ->
                        when (buttonID) {
                            1 ->
                                playerl(
                                    FaceAnim.HALF_GUILTY,
                                    "I'll try asking them then.",
                                ).also { stage = END_DIALOGUE }
                            2 ->
                                playerl(
                                    FaceAnim.HALF_GUILTY,
                                    "Surely you don't let them run everything for you?",
                                ).also { stage = 5 }
                        }

                    5 ->
                        npcl(
                            FaceAnim.FRIENDLY,
                            "Well, they do know what they're doing here. If they did start doing something badly Bravek, the city warder, would have the power to override them. I can't see that happening though.",
                        ).also { stage++ }

                    6 ->
                        options(
                            "I'll try asking them then.",
                            "Can I speak to Bravek anyway?",
                            "This is urgent though! Someone's been kidnapped!",
                        ).also { stage++ }

                    7 ->
                        when (buttonID) {
                            1 ->
                                playerl(
                                    FaceAnim.HALF_GUILTY,
                                    "I'll try asking them then.",
                                ).also { stage = END_DIALOGUE }
                            2 -> playerl(FaceAnim.HALF_GUILTY, "Can I speak to Bravek anyway?").also { stage = 8 }
                            3 ->
                                playerl(
                                    FaceAnim.HALF_GUILTY,
                                    "This is urgent though! Someone's been kidnapped!",
                                ).also { stage = 11 }
                        }

                    8 -> npcl(FaceAnim.FRIENDLY, "He has asked not to be disturbed.").also { stage++ }
                    9 ->
                        options(
                            "This is urgent though! Someone's been kidnapped!",
                            "Okay, I'll leave him alone.",
                            "Do you know when he will be available?",
                        ).also { stage++ }

                    10 ->
                        when (buttonID) {
                            1 ->
                                playerl(FaceAnim.ANNOYED, "This is urgent though! Someone's been kidnapped!").also {
                                    stage = 11
                                }

                            2 ->
                                playerl(FaceAnim.HALF_GUILTY, "Okay, I'll leave him alone.").also {
                                    stage =
                                        END_DIALOGUE
                                }
                            3 ->
                                playerl(FaceAnim.HALF_GUILTY, "Do you know when he will be available?").also {
                                    stage =
                                        14
                                }
                        }

                    11 -> npcl(FaceAnim.HALF_GUILTY, "I'll see what I can do I suppose.").also { stage++ }
                    12 ->
                        npcl(
                            FaceAnim.HALF_GUILTY,
                            "Mr Bravek, there's a man here who really needs to speak to you.",
                        ).also { stage++ }

                    13 -> {
                        end()
                        setQuestStage(player!!, Quests.PLAGUE_CITY, 13)
                        sendNPCDialogue(
                            player!!,
                            NPCs.BRAVEK_711,
                            "I suppose they can come in then. If they keep it short.",
                        ).also { stage++ }
                    }

                    14 ->
                        npcl(FaceAnim.HALF_GUILTY, "Oh I don't know, an hour or so maybe.").also {
                            stage =
                                END_DIALOGUE
                        }
                }

            in 13..15 ->
                when (stage) {
                    0 -> npcl(FaceAnim.FRIENDLY, "Bravek will see you now but keep it short!").also { stage++ }
                    1 ->
                        playerl(FaceAnim.FRIENDLY, "Thanks, I won't take much of his time.").also {
                            stage =
                                END_DIALOGUE
                        }
                }

            in 16..100 ->
                when (stage) {
                    0 -> options("Who is through that door?", "I'm just looking thanks.").also { stage++ }
                    1 ->
                        when (buttonID) {
                            1 -> playerl(FaceAnim.FRIENDLY, "Who is through that door?").also { stage = 2 }
                            2 -> playerl(FaceAnim.FRIENDLY, "I'm just looking thanks.").also { stage = END_DIALOGUE }
                        }

                    2 -> npcl(FaceAnim.FRIENDLY, "The city warder Bravek is in there.").also { stage++ }
                    3 -> playerl(FaceAnim.FRIENDLY, "Can I go in?").also { stage++ }
                    4 -> npcl(FaceAnim.FRIENDLY, "I suppose so.").also { stage = END_DIALOGUE }
                }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.CLERK_713)
}
