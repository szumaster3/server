package content.region.misthalin.handlers.rc_guild.dialogue

import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

/**
 * Wizard Acantha dialogue.
 */
@Initializable
class WizardAcanthaDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc("You look slightly more intelligent than the rest of", "these goons. Will you help me?")

        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> options("Yes, I'll help.", "No.", "Help with what?").also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> player("Yes, I'll help.").also { stage = 23 }
                    2 -> player("No.").also { stage++ }
                    3 -> player("Help with what?").also { stage = 3 }
                    4 -> player("How can you be sure about which orb is good?").also { stage = 19 }
                }

            2 -> npc("Humph. The youth today have no sense of adventure.").also { stage = END_DIALOGUE }
            3 ->
                npc(
                    "Don't you keep up with the Runecrafting Theory Journal?",
                    "We've been making major discoveries! The youth of today",
                    "are so lazy. Shall I tell you about it?",
                ).also {
                    stage++
                }

            4 -> player("Sure. What am I supposed to do?").also { stage++ }
            5 ->
                npc(
                    "When we began formally studying the rune altars, we",
                    "saw a spike in the energy fields surrounding altars",
                    "when there is an increase in rune production.",
                ).also {
                    stage++
                }

            6 ->
                npc(
                    "When an essence is used on the altar, energy is",
                    "transferred and bound into the essence, creating",
                    "a rune.",
                ).also {
                    stage++
                }

            7 ->
                npc(
                    "As with all transfers of energy, some is lost in the",
                    "process. This lost energy, manifested as green orbs,",
                    "needs to be returned to the altar to keep them well",
                    "maintained.",
                ).also {
                    stage++
                }

            8 ->
                npc(
                    "When the green energy escapes during the transfer,",
                    "yellow energy particles in the air gather around the",
                    "pure green energy. They form into opposing orbs and",
                    "must be kept away as they are impure energy forms.",
                ).also {
                    stage++
                }

            9 ->
                npc(
                    "I need you to put the green orbs back in the altar. I",
                    "will give you two wands to move the orbs around and a",
                    "third wand to create barriers to prevent those pesky",
                    "yellow orbs from getting in.",
                ).also {
                    stage++
                }

            10 ->
                npc(
                    "To further this cause, we've set up a project to repair",
                    "the altars. I wanted to just call it The Orb Project, but",
                    "the buffoon wanted it to sound grander, so now it's",
                    "The Great Orb Project. As if it needed an adjective.",
                ).also {
                    stage++
                }

            11 ->
                options(
                    "Okay, okay. Green orbs into the altar, yellow orbs away.",
                    "Right, yellow orbs into altar, green orbs away.",
                    "So, what do I get for all this hard work?",
                    "How can you be sure about which orb is good?",
                ).also {
                    stage++
                }

            12 ->
                when (buttonId) {
                    1 -> player("Okay, okay. Green orbs into the altar, yellow orbs away.").also { stage = 16 }
                    2 -> player("Right, yellow orbs into altar, green orbs away.").also { stage = 17 }
                    3 -> player("So, what do I get for all this hard work?").also { stage = 13 }
                    4 -> player("How can you be sure about which orb is good?").also { stage = 19 }
                }

            13 ->
                npc(
                    "Aaaah, a true mercenary. Although we cannot agree",
                    "scientifically, Vief and I have decided to reward those",
                    "who help us. If you get more green orbs into the altar",
                    "than the yellow team gets yellow orbs, you win the",
                ).also {
                    stage++
                }

            14 ->
                npc(
                    "altar. You will receive rune essence each round you",
                    "win. The team who winds the most altars will receive",
                    "tokens which you can give to Wizard Elriss in exchange",
                    "for a reward. I don't know the exact details, so it's best",
                ).also {
                    stage++
                }

            15 -> npc(FaceAnim.LAUGH, "to speak to her.").also { stage = END_DIALOGUE }
            16 ->
                npc(
                    "Well, at least you aren't as thick as that Wizard Vief.",
                    "Make sure to stop his apprentices putting yellow orbs into altars.",
                    "Use your wands intelligently and you'll come out victorious.",
                ).also {
                    stage = END_DIALOGUE
                }

            17 -> npc("Read my lips, whippersnapper! NO YELLOW ORBS", "IN THE ALTAR.").also { stage++ }
            18 ->
                player("Eek! Okay, okay. Green orbs into the altar,", "yellow orbs away.").also {
                    stage = END_DIALOGUE
                }

            19 ->
                npc(
                    "Do you think I got to where I am today on false",
                    "calculations? I researched it, that's how I know!",
                    "There was a time when my opinion was respected and",
                    "accepted without question.",
                ).also {
                    stage++
                }

            20 ->
                npc(
                    "Age steals that from you. I will not explain my",
                    "findings to fools. If you want to see how I reached my",
                    "conclusions, you should keep up with the scientific",
                    "wizarding community.",
                ).also {
                    stage++
                }

            21 ->
                npc(
                    "I have no time for laziness! Wizard Vief, in",
                    "an attempt to progress his career, has decided",
                    "to disagree with my  findings and cast doubt upon my",
                    "ability as a wizard.",
                ).also {
                    stage++
                }

            22 ->
                npc(
                    "What better way to attract attention to one's self",
                    "than disagree with those more knowledgeable?",
                    "Pay no attention to that man, he's a buffoon.",
                ).also {
                    stage = END_DIALOGUE
                }

            23 ->
                npc(
                    FaceAnim.ANNOYED,
                    "You need your head right hand and two free",
                    "inventory slots, or I can't give you the necessary",
                    "equipment.",
                ).also {
                    stage = END_DIALOGUE
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.WIZARD_ACANTHA_8031)
    }
}
