package content.minigame.vinesweeper.dialogue

import content.minigame.vinesweeper.handlers.VinesweeperListener
import core.api.interaction.openNpcShop
import core.api.openInterface
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.dialogue.Topic
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.Components
import org.rs.consts.NPCs

@Initializable
class ToolLeprechaunDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        val faceExpressions =
            if (npc.id == NPCs.TECLYN_2861) {
                FaceAnim.HAPPY
            } else {
                FaceAnim.OLD_HAPPY
            }
        npc(
            faceExpressions,
            "Ah, 'tis a foine day to be sure! Can I help ye with tool",
            "storage, or a trip to Winkin's Farm, or what?",
        )
        stage = 1
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        val faceExpressions =
            if (npc.id == NPCs.TECLYN_2861) {
                FaceAnim.HAPPY
            } else {
                FaceAnim.OLD_HAPPY
            }
        when (stage) {
            1 ->
                showTopics(
                    Topic(FaceAnim.NEUTRAL, "Yes, please.", 10),
                    Topic("What tools can you store?", 2, true),
                    Topic(FaceAnim.NEUTRAL, "No thanks, I'll keep hold of my stuff.", 20),
                    Topic("Can you take me to Winkin's Farm?", 30),
                )

            2 -> playerl(FaceAnim.THINKING, "What can you store?").also { stage++ }
            3 ->
                npcl(
                    faceExpressions,
                    "We'll hold onto yer rake, yer seed dibber, yer spade, yer secateurs, yer waterin' can and yer trowel - but mind it's not one of them fancy trowels only archaeologists use!",
                ).also {
                    stage++
                }

            4 ->
                npcl(
                    faceExpressions,
                    "We'll take a few buckets off yer hands too, and even yer compost and supercompost! There's room in our shed for plenty of compost, so bring it on.",
                ).also {
                    stage++
                }

            5 ->
                npcl(
                    faceExpressions,
                    "Also if ye hands us yer farming produce, we might be able to change it into banknotes.",
                ).also {
                    stage++
                }

            6 -> npcl(faceExpressions, "So... do ye want to be using the store?").also { stage++ }
            7 ->
                showTopics(
                    Topic(FaceAnim.NEUTRAL, "Yes, please.", 10),
                    Topic("What do you do with the tools you're storing?", 11, true),
                    Topic(FaceAnim.NEUTRAL, "No thanks, I'll keep hold of my stuff.", 20),
                    Topic("Can you take me to Winkin's Farm?", 30),
                )

            10 -> {
                end()
                openInterface(player, Components.FARMING_TOOLS_125)
            }

            11 ->
                playerl(
                    FaceAnim.THINKING,
                    "What do you do with the tools you're storing? They can't possibly all fit in your pockets!",
                ).also {
                    stage++
                }

            12 ->
                npcl(
                    faceExpressions,
                    "We leprechauns have a shed where we keep 'em. It's a magic shed, so ye can get yer items back from any of us leprechauns whenever ye want. Saves ye havin' to carry loads of stuff around the country!",
                ).also {
                    stage++
                }

            13 -> npcl(faceExpressions, "So... do ye want to be using the store?").also { stage = 1 }
            20 ->
                npcl(
                    faceExpressions,
                    "Ye must be dafter than ye look if ye likes luggin' yer tools everywhere ye goes!",
                ).also {
                    stage =
                        END_DIALOGUE
                }

            30 -> {
                end()
                VinesweeperListener.Companion.VinesweeperTeleport.teleport(npc!!, player!!)
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.TOOL_LEPRECHAUN_3021, NPCs.TECLYN_2861)
    }
}

@Initializable
class ToolLeprechaunGothDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(
            FaceAnim.HAPPY,
            "Ah, 'tis a foine day to be sure! Can I help ye with tool",
            "storage, or a trip to Winkin's Farm, or what?",
        )
        stage = 1
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            1 ->
                showTopics(
                    Topic(FaceAnim.FRIENDLY, "Yes, please.", 10),
                    Topic("What tools can you store?", 2, true),
                    Topic(FaceAnim.FRIENDLY, "No thanks, I'll keep hold of my stuff.", 20),
                    Topic(FaceAnim.HALF_ASKING, "Can you take me to Winkin's Farm?", 30),
                )

            2 -> playerl(FaceAnim.HALF_ASKING, "What can you store?").also { stage++ }
            3 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "We'll hold onto yer rake, yer seed dibber, yer spade, yer secateurs, yer waterin' can and yer trowel - but mind it's not one of them fancy trowels only archaeologists use!",
                ).also { stage++ }

            4 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "We'll take a few buckets off yer hands too, and even yer compost and supercompost! There's room in our shed for plenty of compost, so bring it on.",
                ).also { stage++ }

            5 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Also if ye hands us yer farming produce, we might be able to change it into banknotes.",
                ).also { stage++ }

            6 -> npcl(FaceAnim.FRIENDLY, "So... do ye want to be using the store?").also { stage++ }
            7 ->
                showTopics(
                    Topic(FaceAnim.FRIENDLY, "Yes, please.", 10),
                    Topic("What do you do with the tools you're storing?", 11, true),
                    Topic(FaceAnim.FRIENDLY, "No thanks, I'll keep hold of my stuff.", 20),
                    Topic("Can you take me to Winkin's Farm?", 30),
                )

            10 -> {
                end()
                openInterface(player, Components.FARMING_TOOLS_125)
            }

            11 ->
                playerl(
                    FaceAnim.HALF_THINKING,
                    "What do you do with the tools you're storing? They can't possibly all fit in your pockets!",
                ).also { stage++ }

            12 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "We leprechauns have a shed where we keep 'em. It's a magic shed, so ye can get yer items back from any of us leprechauns whenever ye want. Saves ye havin' to carry loads of stuff around the country!",
                ).also { stage++ }

            13 -> npcl(FaceAnim.ASKING, "So... do ye want to be using the store?").also { stage = 1 }
            20 ->
                npcl(
                    FaceAnim.FRIENDLY,
                    "Ye must be dafter than ye look if ye likes luggin' yer tools everywhere ye goes!",
                ).also { stage = END_DIALOGUE }

            30 -> {
                end()
                VinesweeperListener.Companion.VinesweeperTeleport.teleport(npc!!, player!!)
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GOTH_LEPRECHAUN_8000)
    }
}

@Initializable
class ToolLeprechaunOnVacationDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.OLD_HAPPY, "Aye, top o' th' mornin' to ya!", "Are ye wantin' help with th' tool store?")
        stage = 2
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            2 ->
                showTopics(
                    Topic(FaceAnim.NEUTRAL, "Yes please.", 10),
                    Topic(FaceAnim.THINKING, "Why are you sunbathing up a mountain?", 3),
                    Topic(FaceAnim.NEUTRAL, "No thanks, I'll keep hold of my stuff.", 20),
                    Topic("Would you like to trade?", 12),
                )

            3 ->
                npcl(
                    FaceAnim.OLD_HAPPY,
                    "We tool leprechauns work hard, that we do. An'nary a penny do we get in return. So ye cannae begrudge me mah holiday an' a wee drink or twelve!",
                ).also { stage++ }

            4 ->
                playerl(
                    FaceAnim.THINKING,
                    "Yes, very nice, but why are you sunbathing up a mountain? Surely a beach would be more appropriate?",
                ).also { stage++ }

            5 ->
                npcl(
                    FaceAnim.OLD_HAPPY,
                    "Ahh, but I likes th' ruggedy mountain, ye see. Also, I ha' a terrible allergy to sand.",
                ).also { stage++ }

            6 -> playerl(FaceAnim.NEUTRAL, "Fair enough, I suppose.").also { stage++ }
            7 -> npcl(FaceAnim.OLD_HAPPY, "So were ye wantin' help with th' tool store?").also { stage++ }
            8 ->
                showTopics(
                    Topic(FaceAnim.NEUTRAL, "Yes, please.", 10),
                    Topic(FaceAnim.NEUTRAL, "No thanks, I'll keep hold of my stuff.", 20),
                )

            10 -> {
                end()
                openInterface(player, Components.FARMING_TOOLS_125)
            }

            12 -> npcl(FaceAnim.OLD_HAPPY, "Sure, have a look.").also { stage++ }
            13 -> {
                end()
                openNpcShop(player, NPCs.TOOL_LEPRECHAUN_4965)
            }

            20 ->
                npcl(
                    FaceAnim.OLD_NORMAL,
                    "Ye must be dafter than ye look if ye likes luggin' yer tools everywhere ye goes!",
                ).also {
                    stage = END_DIALOGUE
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.TOOL_LEPRECHAUN_4965)
    }
}
