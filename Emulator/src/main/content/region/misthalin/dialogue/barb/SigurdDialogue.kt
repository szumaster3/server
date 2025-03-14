package content.region.misthalin.dialogue.barb

import core.api.getStatLevel
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class SigurdDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.HALF_GUILTY, "Ha Ha! Hello!")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> options("Who are you?", "Can you teach me about canoeing?").also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> player(FaceAnim.HALF_GUILTY, "Who are you?").also { stage++ }
                    2 ->
                        npc(
                            FaceAnim.HALF_GUILTY,
                            "It's really quite simple. Just walk down to that",
                            "tree on the water bank and chop it down.",
                        ).also {
                            stage =
                                16
                        }
                }
            2 -> npc(FaceAnim.HALF_GUILTY, "I'm Sigurd the Great and Brainy.").also { stage++ }
            3 -> player(FaceAnim.HALF_GUILTY, "Why do they call you the Great and Brainy?").also { stage++ }
            4 -> npc(FaceAnim.HALF_GUILTY, "Because I invented the Log Canoe!").also { stage++ }
            5 -> player(FaceAnim.HALF_GUILTY, "Log Canoe?").also { stage++ }
            6 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Yeash! Me and my cousins were having a great",
                    "party by the river when we decided to have a",
                    " game of 'Smack The Tree'",
                ).also {
                    stage++
                }
            7 -> player(FaceAnim.HALF_GUILTY, "Smack the Tree?").also { stage++ }
            8 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "It's a game where you take it in turnsh shmacking",
                    " a tree. First one to uproot the tree winsh!",
                ).also {
                    stage++
                }
            9 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "Anyway, I won the game with a flying tackle.",
                    " The tree came loose and down the river bank I went",
                    " still holding the tree.",
                ).also {
                    stage++
                }
            10 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "I woke up a few hours later and found myself",
                    "several miles down river. And thatsh how I",
                    "invented the log canoe!",
                ).also {
                    stage++
                }
            11 ->
                player(
                    FaceAnim.HALF_GUILTY,
                    "So you invented the 'Log Canoe' by falling into a river",
                    "hugging a tree?",
                ).also {
                    stage++
                }
            12 -> npc(FaceAnim.HALF_GUILTY, "Well I refined the design from the original", "you know!").also { stage++ }
            13 ->
                npc(
                    FaceAnim.HALF_GUILTY,
                    "I cut all the branches off to make it more",
                    "comfortable. I could tell you how to if you like?",
                ).also {
                    stage++
                }
            14 -> options("Yes", "No").also { stage++ }
            15 ->
                when (buttonId) {
                    1 ->
                        npc(
                            FaceAnim.HALF_GUILTY,
                            "It's really quite simple. Just walk down to that tree",
                            "on the water bank and chop it down.",
                        ).also {
                            stage++
                        }

                    2 ->
                        npc(FaceAnim.HALF_GUILTY, "Okay, if you change your mind you know where", "to find me.").also {
                            stage =
                                END_DIALOGUE
                        }
                }
            16 -> {
                val level = getStatLevel(player, Skills.WOODCUTTING)
                when {
                    level > 57 ->
                        npcl(
                            FaceAnim.HALF_GUILTY,
                            "You look like you know your way around a tree, you can make a Waka canoe.",
                        ).also {
                            stage++
                        }
                    level in 42..57 ->
                        npcl(FaceAnim.HALF_GUILTY, "Well, you're pretty handy with an axe!").also {
                            stage =
                                21
                        }
                    level in 27..41 ->
                        npcl(
                            FaceAnim.HALF_GUILTY,
                            "You could make a Dugout canoe with your woodcutting skill, but I don't see why you would want to.",
                        ).also {
                            stage =
                                END_DIALOGUE
                        }
                    level in 12..26 ->
                        npcl(
                            FaceAnim.HALF_GUILTY,
                            "You can make a log canoe like mine! It'll get you 1 stop down the river.",
                        ).also {
                            stage =
                                23
                        }
                    else ->
                        npcl(
                            FaceAnim.HALF_GUILTY,
                            "Well, you don't look like you have the skill to make a canoe.",
                        ).also {
                            stage =
                                25
                        }
                }
                npc(FaceAnim.HALF_GUILTY, "Then take your axe to it and shape it how you", "like!").also { stage++ }
            }
            17 -> npc(FaceAnim.HALF_GUILTY, "What's a Waka?").also { stage++ }
            18 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "I've only ever seen Hari using them. People say he's found a way to canoe the river underground and into the Wilderness.",
                ).also {
                    stage++
                }
            19 -> npcl(FaceAnim.HALF_GUILTY, "Hari hangs around up near Edgeville.").also { stage++ }
            20 -> npcl(FaceAnim.HALF_GUILTY, "He's a nice bloke.").also { stage = END_DIALOGUE }
            21 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "You could make Stable Dugout canoes, like that snooty fella Tarquin.",
                ).also {
                    stage++
                }
            22 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "He reckons his canoes are better than mine. He's never said it to my face though.",
                ).also {
                    stage =
                        END_DIALOGUE
                }
            23 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "There's some snooty fella down near the Champions Guild who reckons his canoes are better than mine.",
                ).also {
                    stage++
                }
            24 -> npcl(FaceAnim.HALF_GUILTY, "He's never said it to my face though.").also { stage = END_DIALOGUE }
            25 -> npcl(FaceAnim.HALF_GUILTY, "You need to have at least level 12 woodcutting.").also { stage++ }
            26 ->
                npcl(
                    FaceAnim.HALF_GUILTY,
                    "Once you are able to make a canoe it makes travel along the river much quicker!",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return SigurdDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SIGURD_3329)
    }
}
