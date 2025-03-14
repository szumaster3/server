package content.region.misthalin.dialogue

import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class AeonisigRaispherDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(
            "Please only talk to the King if it's important. He has a",
            "heavy burden to bear with the running of his Kingdom.",
        )
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                options(
                    "Who are you?",
                    "What do you do here?",
                    "Where did you come from?",
                    "How did you come to be an advisor to King Roald?",
                    "What is happening in the kingdom?",
                ).also { stage++ }

            1 ->
                when (buttonId) {
                    1 -> player("Who are you?").also { stage = 2 }
                    2 -> player("What do you do here?").also { stage = 4 }
                    3 -> player("Where did you come from?").also { stage = 7 }
                    4 -> player("How did you come to be an advisor to King Roald?").also { stage = 9 }
                    5 -> player("What is happening in the kingdom?").also { stage = 13 }
                }

            2 ->
                npc(
                    "Apologies! Allow me to introduce myself. My",
                    "name is Aeonisig Raispher, special advisor",
                    "to King Roald on spiritual matters.",
                ).also { stage++ }

            3 ->
                npc(
                    "It means that some decisions the King has to make might",
                    "have unforeseen repercussions on the nation's spiritual",
                    "sensibilities. My duty is to ensure that Saradomin",
                    "ideals are not stomped underfoot.",
                ).also { stage = END_DIALOGUE }

            4 ->
                npc(
                    "My main function is to ensure that King Roald is apprised",
                    "of all options, especially those that favour the",
                    "righteous followers of Saradomin.",
                ).also { stage++ }

            5 ->
                player(
                    "But surely the King should be able to make",
                    "his own decisions on what's best for Misthalin?",
                ).also { stage++ }

            6 ->
                npc(
                    "What an interesting perspective you have! Totally",
                    "unworkable of course, but interesting nonetheless.",
                ).also { stage = END_DIALOGUE }

            7 ->
                npc(
                    "I took my religious and combat training in several parts",
                    "of the known world. I've also fought despicable beasts",
                    "in the wilderness in Saradomin's name. Needless to say I",
                    "have great experienc in the ways of the world and am an",
                ).also { stage++ }

            8 -> npc("invaluable aid to his ordship's decision making process.").also { stage = END_DIALOGUE }
            9 ->
                npc(
                    "The King of Misthalin, like any great leader, always",
                    "requires the best advice and the best advisors. He very",
                    "often summons occasional advisors to help him in",
                    "certain situations, but it was felt by the Church",
                ).also { stage++ }

            10 ->
                npc(
                    "of Saradomin that a full time advisor on religious matters",
                    "was needed to ensure fair treatement",
                    "of Saradomin's followers.",
                ).also { stage++ }

            11 ->
                player(
                    "How come he doesn't have an advisor for",
                    "any other religious denominations?",
                ).also { stage++ }

            12 ->
                npc(
                    "Because I simply won't stand for it, that's why!",
                    "Now, enough of your impertinent questions.",
                    "I have work to do!",
                ).also { stage = END_DIALOGUE }

            13 -> npc("Nothing out of the usual.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.AEONISIG_RAISPHER_4710)
    }
}
