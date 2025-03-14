package content.region.asgarnia.dialogue.burthope

import core.api.getStatLevel
import core.api.openDialogue
import core.game.dialogue.Dialogue
import core.game.global.Skillcape.purchase
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

/**
 * Represents the Ajjat dialogue.
 */
@Initializable
class AjjatDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc(
            "Greetings, fellow warrior. I am Ajjat, former Black Knight",
            "and now training officer here in the Warriors' Guild.",
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
                    "Can you tell me about skillcapes, please?",
                    "Black knight? Why are you here?",
                    "What's the dummy room all about?",
                    "May I claim my tokens please?",
                    "Bye!",
                ).also {
                    stage++
                }
            1 ->
                when (buttonId) {
                    1 -> player("Can you tell me about skillcapes, please?").also { stage++ }
                    2 -> player("Black knight? Why are you here?").also { stage = 4 }
                    3 -> player("What's the dummy room all about?").also { stage = 6 }
                    4 -> {
                        end()
                        openDialogue(player, "wg:claim-tokens", npc.id)
                    }
                    5 -> player("Bye!").also { stage = 12 }
                }
            2 ->
                npc(
                    "Skillcapes, also knows as Capes of Accomplishment, are",
                    "reserved for the elite of the elite. Only a person who has",
                    "truly mastered a skill can buy one, even then a",
                    "Skillcape can only be bought from one who is recognised as",
                ).also {
                    stage++
                }
            3 ->
                npc(
                    "the highest skilled in the land at any particular skill. I",
                    "have the privilege of being the person that controls",
                    "access to the Skillcape of Attack. Is there anything else I",
                    "can help you with?",
                ).also {
                    stage =
                        13
                }
            4 ->
                npc(
                    "Indeed I was however, their... methods did not match",
                    "with my ideals, so I left. Harrallak, recognizing my talent as",
                    "a warrior, took me in and offered me a job here.",
                ).also {
                    stage++
                }
            5 -> player("Hmm...well, if Harrallak trusts you, I guess I can.").also { stage = 0 }
            6 ->
                npc(
                    "Ahh yes, the dummies. Another ingenious invention of the",
                    "noble dwarf, Gamfred. They're mechanical, you see, and",
                    "pop up out of the floor. You have to hit them with the",
                    "correct attack mode before they disappear again.",
                ).also {
                    stage++
                }
            7 -> player("Do, how do I tell which one is which?").also { stage++ }
            8 ->
                npc(
                    "here are two different ways. One indication is their",
                    "colour, the other is the pose and weapons they are",
                    "holding, for instance, the one holding daggers you will",
                    "to hit with a piercing attack.",
                ).also {
                    stage++
                }
            9 ->
                npc(
                    "In the room, you will find a poster on the wall to",
                    " help you recognize each different dummy.",
                ).also {
                    stage++
                }
            10 -> player("That sounds ingenious!").also { stage++ }
            11 ->
                npc(
                    "Indeed, you may find that you need several weapons to",
                    "be successfully all of the time, but keep trying",
                    "The weapons Shop upstairs may help you there.",
                ).also {
                    stage =
                        0
                }
            12 -> npc("Farewell, warrior. Stay away from the dark side.").also { stage = END_DIALOGUE }
            13 -> {
                if (getStatLevel(player, Skills.ATTACK) == 99) {
                    player("I'd like to buy a skillcape of Attack.").also { stage++ }
                } else {
                    end()
                }
            }
            14 -> npc("Okay, it will cost you 99000 coins", "is that ok?").also { stage++ }
            15 -> options("Yes.", "No.").also { stage++ }
            16 ->
                when (buttonId) {
                    1 -> player("Yes.").also { stage++ }
                    2 -> end()
                }
            17 ->
                if (purchase(player, Skills.ATTACK)) {
                    npc("There you go! Enjoy.").also { stage = END_DIALOGUE }
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.AJJAT_4288)
    }
}
