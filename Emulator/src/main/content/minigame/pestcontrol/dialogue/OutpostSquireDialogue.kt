package content.minigame.pestcontrol.dialogue

import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.world.GameWorld.settings
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class OutpostSquireDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc("Hi, how can I help you?")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> options("Who are you?", "What is this place?", "I'm fine thanks.").also { stage++ }
            1 ->
                when (buttonId) {
                    1 -> player("Who are you?").also { stage++ }
                    2 -> player("What is this place?").also { stage = 18 }
                    3 -> player("I'm fine thanks.").also { stage = END_DIALOGUE }
                }

            2 -> npc("I'm a a Squire for the Void Knights.").also { stage++ }
            3 -> player("The who?").also { stage++ }
            4 ->
                npc(
                    "The Void Knights, they are great warriors of balance",
                    "who do Guthix's work here in " + settings!!.name + ".",
                ).also { stage++ }

            5 ->
                options(
                    "Wow, can I join?",
                    "What kind of work?",
                    "What's " + settings!!.name + "?",
                    "Uh huh, sure.",
                ).also { stage++ }

            6 ->
                when (buttonId) {
                    1 -> player("Wow, can I join?").also { stage = 11 }
                    2 -> player("What kind of work?").also { stage = 8 }
                    3 -> player("What's " + settings!!.name + "?").also { stage++ }
                    4 -> player("Uh huh, sure.").also { stage = END_DIALOGUE }
                }

            7 ->
                npc("It is the name that Guthix gave to this world, so we", "honour him with its use.").also {
                    stage = END_DIALOGUE
                }

            8 ->
                npc(
                    "Ah well you see we try to keep " + settings!!.name + " as Guthix",
                    "intended, it's very challenging. Actually we've been",
                    "having some problems recently, maybe you could help",
                    "us?",
                ).also { stage++ }

            9 ->
                options(
                    "Yeah ok, what's the problem?",
                    "What's " + settings!!.name + "?",
                    "I'd rather not, sorry.",
                ).also { stage++ }

            10 ->
                when (buttonId) {
                    1 -> player("Yeah ok, what's the problem?").also { stage = 13 }
                    2 -> player("What's " + settings!!.name + "?").also { stage = 7 }
                    3 -> player("I'd rather not sorry.").also { stage = END_DIALOGUE }
                }

            11 ->
                npc(
                    "Entry is strictly invite only, however we do need help",
                    "continuing Guthix's work.",
                ).also { stage++ }

            12 -> player("What kind of work?").also { stage = 8 }
            13 ->
                npc(
                    "Well the order has become quite diminished over the",
                    "years, it's a very long process to learn the skills of a",
                    "Void Knight. Recently there have been breaches into",
                    "our realm from somewhere else, and strange creatures",
                ).also { stage++ }

            14 ->
                npc(
                    "have been pouring through. We can't let that happen,",
                    "and we'd be very grateful if you'd help us.",
                ).also { stage++ }

            15 -> options("How can I help?", "Sorry, but I can't.").also { stage++ }
            16 ->
                when (buttonId) {
                    1 -> player("How can I help?").also { stage++ }
                    2 -> player("Sorry, but I can't.").also { stage = END_DIALOGUE }
                }

            17 ->
                npc(
                    "We send launchers from our outpost to the nearby",
                    "islands. If you go and wait in the lander there that'd",
                    "really help.",
                ).also { stage = END_DIALOGUE }

            18 ->
                npc(
                    "This is our outpost. From here we send launchers out to",
                    "the nearby islands to beat back the invaders.",
                ).also { stage++ }

            19 -> options("What invaders?", "How can I help?", "Good luck with that.").also { stage++ }
            20 ->
                when (buttonId) {
                    1 -> player("What invaders?").also { stage++ }
                    2 -> player("How can I help?").also { stage = 17 }
                    3 -> player("Good luck with that.").also { stage = END_DIALOGUE }
                }

            21 ->
                npc(
                    "Recently there have been breaches into our realm from",
                    "somewhere else, and strange creatures have been",
                    "pouring through. We can't let that happen, and we'd be",
                    "very grateful if you'd help us.",
                ).also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.SQUIRE_3791, NPCs.SQUIRE_3792, NPCs.SQUIRE_3793, NPCs.SQUIRE_3801)
    }
}
