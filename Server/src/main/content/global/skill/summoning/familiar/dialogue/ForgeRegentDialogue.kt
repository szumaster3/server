package content.global.skill.summoning.familiar.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.NPCs

/**
 * Represents the Forge Regent familiar dialogue.
 */
@Initializable
class ForgeRegentDialogue : Dialogue {
    private var branch: Int = -1

    override fun newInstance(player: Player?) = ForgeRegentDialogue(player)

    constructor()
    constructor(player: Player?) : super(player)

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        branch = (0..3).random()
        stage = 0
        when (branch) {
            0 -> npc(FaceAnim.FAMILIAR_NEUTRAL, "Crackley spit crack sizzle?", "(Can we go Smithing?)")
            1 -> npc(FaceAnim.FAMILIAR_NEUTRAL, "Hiss.", "(I'm happy.)")
            2 -> npc(FaceAnim.FAMILIAR_NEUTRAL, "Sizzle!", "(I like logs.)")
            3 -> npc(FaceAnim.FAMILIAR_NEUTRAL, "Sizzle...", "(I'm bored.)")
        }
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (branch) {
            0 -> when (stage) {
                0 -> { playerl(FaceAnim.FRIENDLY, "Maybe."); stage++ }
                1 -> { npc(FaceAnim.FAMILIAR_NEUTRAL, "Hiss?", "(Can we go smelt something?)"); stage++ }
                2 -> { playerl(FaceAnim.FRIENDLY, "Maybe."); stage++ }
                3 -> { npc(FaceAnim.FAMILIAR_NEUTRAL, "Flicker crackle sizzle?", "(Can we go mine something to smelt?)"); stage++ }
                4 -> { playerl(FaceAnim.FRIENDLY, "Maybe."); stage++ }
                5 -> { npc(FaceAnim.FAMILIAR_NEUTRAL, "Sizzle flicker!", "(Yay! I like doing that!)"); stage = END_DIALOGUE }
            }

            1 -> when (stage) {
                0 -> { playerl(FaceAnim.FRIENDLY, "Good."); stage++ }
                1 -> { npc(FaceAnim.FAMILIAR_NEUTRAL, "Crackle.", "(Now I'm sad.)"); stage++ }
                2 -> { playerl(FaceAnim.HALF_ASKING, "Oh dear, why?"); stage++ }
                3 -> { npc(FaceAnim.FAMILIAR_NEUTRAL, "Hiss-hiss.", "(Happy again.)"); stage++ }
                4 -> { playerl(FaceAnim.FRIENDLY, "Glad to hear it."); stage++ }
                5 -> { npc(FaceAnim.FAMILIAR_NEUTRAL, "Crackley-crick.", "(Sad now.)"); stage++ }
                6 -> { playerl(FaceAnim.FRIENDLY, "Um."); stage++ }
                7 -> { npc(FaceAnim.FAMILIAR_NEUTRAL, "Hiss.", "(Happy.)"); stage++ }
                8 -> { playerl(FaceAnim.FRIENDLY, "Right..."); stage++ }
                9 -> { npc(FaceAnim.FAMILIAR_NEUTRAL, "Crackle.", "(Sad.)"); stage++ }
                10 -> { playerl(FaceAnim.FRIENDLY, "You're very strange."); stage++ }
                11 -> { npc(FaceAnim.FAMILIAR_NEUTRAL, "Sizzle hiss?", "(What makes you say that?)"); stage++ }
                12 -> { playerl(FaceAnim.FRIENDLY, "Oh...nothing in particular."); stage = END_DIALOGUE }
            }

            2 -> when (stage) {
                0 -> { playerl(FaceAnim.FRIENDLY, "They are useful for making planks."); stage++ }
                1 -> { npc(FaceAnim.FAMILIAR_NEUTRAL, "Sizzley crack hiss spit.", "(No, I just like walking on them. They burst into flames.)"); stage++ }
                2 -> { playerl(FaceAnim.FRIENDLY, "It's a good job I can use you as a firelighter really!"); stage = END_DIALOGUE }
            }

            3 -> when (stage) {
                0 -> { playerl(FaceAnim.HALF_ASKING, "Are you not enjoying what we're doing?"); stage++ }
                1 -> { npc(FaceAnim.FAMILIAR_NEUTRAL, "Crackley crickle sizzle.", "(Oh yes, but I'm still bored.)"); stage++ }
                2 -> { playerl(FaceAnim.FRIENDLY, "Oh, I see."); stage++ }
                3 -> { npc(FaceAnim.FAMILIAR_NEUTRAL, "Sizzle hiss?", "(What's that over there?)"); stage++ }
                4 -> { playerl(FaceAnim.HALF_ASKING, "I don't know. Should we go and look?"); stage++ }
                5 -> { npc(FaceAnim.FAMILIAR_NEUTRAL, "Hiss crackle spit sizzle crack?", "(Nah, that's old news - I'm bored of it now.)"); stage++ }
                6 -> { npc(FaceAnim.FAMILIAR_NEUTRAL, "Crackle crickle spit hiss?", "(Oooooh ooooh oooooh, what's that over there?)"); stage++ }
                7 -> { playerl(FaceAnim.HALF_ASKING, "But...wha...where now?"); stage++ }
                8 -> { npc(FaceAnim.FAMILIAR_NEUTRAL, "Sizzle crack crickle.", "(Oh no matter, it no longer interests me.)"); stage++ }
                9 -> { playerl(FaceAnim.FRIENDLY, "You're hard work."); stage = END_DIALOGUE }
            }
        }
        return true
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.FORGE_REGENT_7335, NPCs.FORGE_REGENT_7336)
}