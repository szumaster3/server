package content.global.skill.summoning.familiar.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

/**
 * The type Forge regent dialogue.
 */
@Initializable
class ForgeRegentDialogue : Dialogue {
    override fun newInstance(player: Player): Dialogue {
        return ForgeRegentDialogue(player)
    }

    /**
     * Instantiates a new Forge regent dialogue.
     */
    constructor()

    /**
     * Instantiates a new Forge regent dialogue.
     *
     * @param player the player
     */
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        when ((Math.random() * 4).toInt()) {
            0 -> {
                npc(FaceAnim.CHILD_NORMAL, "Crackley spit crack sizzle?", "(Can we go Smithing?)")
                stage = 0
            }

            1 -> {
                npc(FaceAnim.CHILD_NORMAL, "Hiss.", "(I'm happy.)")
                stage = 6
            }

            2 -> {
                npc(FaceAnim.CHILD_NORMAL, "Sizzle!", "(I like logs.)")
                stage = 19
            }

            3 -> {
                npc(FaceAnim.CHILD_NORMAL, "Sizzle...", "(I'm bored.)")
                stage = 22
            }
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> {
                playerl(FaceAnim.FRIENDLY, "Maybe.")
                stage++
            }

            1 -> {
                npc(FaceAnim.CHILD_NORMAL, "Hiss?", "(Can we go smelt something?)")
                stage++
            }

            2 -> {
                playerl(FaceAnim.FRIENDLY, "Maybe.")
                stage++
            }

            3 -> {
                npc(FaceAnim.CHILD_NORMAL, "Flicker crackle sizzle?", "(Can we go mine something to smelt?)")
                stage++
            }

            4 -> {
                playerl(FaceAnim.FRIENDLY, "Maybe.")
                stage++
            }

            5 -> {
                npc(FaceAnim.CHILD_NORMAL, "Sizzle flicker!", "(Yay! I like doing that!)")
                stage = END_DIALOGUE
            }

            6 -> {
                playerl(FaceAnim.FRIENDLY, "Good.")
                stage++
            }

            7 -> {
                npc(FaceAnim.CHILD_NORMAL, "Crackle.", "(Now I'm sad.)")
                stage++
            }

            8 -> {
                playerl(FaceAnim.HALF_ASKING, "Oh dear, why?")
                stage++
            }

            9 -> {
                npc(FaceAnim.CHILD_NORMAL, "Hiss-hiss.", "(Happy again.)")
                stage++
            }

            10 -> {
                playerl(FaceAnim.FRIENDLY, "Glad to hear it.")
                stage++
            }

            11 -> {
                npc(FaceAnim.CHILD_NORMAL, "Crackley-crick.", "(Sad now.)")
                stage++
            }

            12 -> {
                playerl(FaceAnim.FRIENDLY, "Um.")
                stage++
            }

            13 -> {
                npc(FaceAnim.CHILD_NORMAL, "Hiss.", "(Happy.)")
                stage++
            }

            14 -> {
                playerl(FaceAnim.FRIENDLY, "Right...")
                stage++
            }

            15 -> {
                npc(FaceAnim.CHILD_NORMAL, "Crackle.", "(Sad.)")
                stage++
            }

            16 -> {
                playerl(FaceAnim.FRIENDLY, "You're very strange.")
                stage++
            }

            17 -> {
                npc(FaceAnim.CHILD_NORMAL, "Sizzle hiss?", "(What makes you say that?)")
                stage++
            }

            18 -> {
                playerl(FaceAnim.FRIENDLY, "Oh...nothing in particular.")
                stage = END_DIALOGUE
            }

            19 -> {
                playerl(FaceAnim.FRIENDLY, "They are useful for making planks.")
                stage++
            }

            20 -> {
                npc(
                    FaceAnim.CHILD_NORMAL,
                    "Sizzley crack hiss spit.",
                    "(No, I just like walking on them. They burst into flames.)"
                )
                stage++
            }

            21 -> {
                playerl(FaceAnim.FRIENDLY, "It's a good job I can use you as a firelighter really!")
                stage = END_DIALOGUE
            }

            22 -> {
                playerl(FaceAnim.HALF_ASKING, "Are you not enjoying what we're doing?")
                stage++
            }

            23 -> {
                npc(FaceAnim.CHILD_NORMAL, "Crackley crickle sizzle.", "(Oh yes, but I'm still bored.)")
                stage++
            }

            24 -> {
                playerl(FaceAnim.FRIENDLY, "Oh, I see.")
                stage++
            }

            25 -> {
                npc(FaceAnim.CHILD_NORMAL, "Sizzle hiss?", "(What's that over there?)")
                stage++
            }

            26 -> {
                playerl(FaceAnim.HALF_ASKING, "I don't know. Should we go and look?")
                stage++
            }

            27 -> {
                npc(
                    FaceAnim.CHILD_NORMAL,
                    "Hiss crackle spit sizzle crack?",
                    "(Nah, that's old news - I'm bored of it now.)"
                )
                stage++
            }

            28 -> {
                npc(
                    FaceAnim.CHILD_NORMAL,
                    "Crackle crickle spit hiss?",
                    "(Oooooh ooooh oooooh, what's that over there?)"
                )
                stage++
            }

            29 -> {
                playerl(FaceAnim.HALF_ASKING, "But...wha...where now?")
                stage++
            }

            30 -> {
                npc(FaceAnim.CHILD_NORMAL, "Sizzle crack crickle.", "(Oh no matter, it no longer interests me.)")
                stage++
            }

            31 -> {
                playerl(FaceAnim.FRIENDLY, "You're hard work.")
                stage = END_DIALOGUE
            }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.FORGE_REGENT_7335, NPCs.FORGE_REGENT_7336)
    }
}
