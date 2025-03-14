package content.region.asgarnia.dialogue.burthope

import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

/**
 * Represents the Kamfreena dialogue.
 */
@Initializable
class KamfreenaDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        npc("Why hello there! I'm Kamfreena. Like the look of my pets?", "I think they're eyeing you up.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> player("That was a really bad pun.").also { stage++ }
            1 ->
                npc(
                    "Sorry...I don't get to see the rest of the guild much",
                    "stuck up here. The cyclopes don't talk much you see.",
                ).also {
                    stage++
                }
            2 -> player("Shouldn't that be cyclopses?").also { stage++ }
            3 -> npc("Nope! Cyclopes is the plural of cyclops. One cyclops,", "many cyclopes.").also { stage++ }
            4 -> player("Oh, right, thanks.").also { stage++ }
            5 ->
                options(
                    "Where are they from?",
                    "How did they get here?",
                    "Why are they here?",
                    "Bye!",
                ).also { stage++ }
            6 ->
                when (buttonId) {
                    1 -> player("Where are they from?").also { stage++ }
                    2 -> player("How did they get here?").also { stage = 8 }
                    3 -> player("Why are they here?").also { stage = 9 }
                    4 -> player("Bye!").also { stage = 20 }
                }
            7 -> npc("They're from the far east lands.").also { stage = 5 }
            8 ->
                npc(
                    "Ahh.. our guildmaster, Harrallak, went on an expedition",
                    "there. He brought them back with him.",
                ).also {
                    stage =
                        5
                }
            9 -> npc("For the warriors to train on of course! They also drop a", "rather nice blade.").also { stage++ }
            10 -> player("Oh? What would that be?").also { stage++ }
            11 -> npc("Defenders.").also { stage++ }
            12 -> player("Err what are they?").also { stage++ }
            13 -> npc("It's a blade you can defend with using your shield hand,", "like I have.").also { stage++ }
            14 -> player("Wow!").also { stage++ }
            15 ->
                npc(
                    "For every 10 tokens you collect around the guild, you",
                    "can spend one minute in with my pets. As you get",
                    "defenders you can show them to me to earn even",
                    "better ones... but remember if you lose them you'll have",
                ).also {
                    stage++
                }
            16 -> npc("to start at bronze again. I'd advise keeping a spare in", "your bank.").also { stage++ }
            17 -> player("Ok!").also { stage++ }
            18 -> npc("Oh, by the way, you need to earn 100 tokens", "before I'll let you in!").also { stage++ }
            19 -> player("Right, I'd better go play some games then!").also { stage = 5 }
            20 -> npc("See you back here soon I hope!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.KAMFREENA_4289)
    }
}
