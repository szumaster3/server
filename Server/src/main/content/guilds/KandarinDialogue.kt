package content.guilds

import content.guilds.Guilds.Companion.inGuild
import core.api.sendOptions
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE

/**
 * Represents the Kandarin dialogue.
 */
@Initializable
class KandarinDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc("Ah... you've wandered far, adventurer.")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        val guild = Guilds.Kandarin
        when (stage) {
            0 -> npc("The forests of Kandarin hold more than trees and towns.").also { stage++ }
            1 -> player(FaceAnim.HALF_ASKING, "What secrets lie there?").also { stage++ }
            2 -> npc("Some call them old druidic paths, some the hidden trade routes of Ardougne's spies. Few realize there are watchers who tread them silently.").also { stage++ }
            3 -> player(FaceAnim.HALF_ASKING, "Watchers? Who are they?").also { stage++ }
            4 -> npc("A secret brotherhood, known to some as the brotherhood. We protect the balance of Kandarin, unseen, unnoticed, but always present.").also { stage++ }
            5 -> player(FaceAnim.HALF_ASKING, "Can I join?").also { stage++ }
            6 -> if(inGuild(player))
                npc("You have already sworn loyalty elsewhere. We cannot accept your allegiance.").also { stage = END_DIALOGUE }
            else if(!guild.checkRequirements(player))
                npc("You are not yet ready to walk the path of the brotherhood. Return when your mind and skills are sharper.").also { stage = END_DIALOGUE }
            else
                npc("Only those who respect nature, discretion, and subtlety. Not for glory, not for coins.").also { stage++ }
            9 -> sendOptions(player, "Do you wish to join?", "Yes.", "No, thanks.").also { stage++ }
            10 -> when(buttonId) {
                1 -> player("Yes, I wish to join.").also { stage++ }
                2 -> player("Not yet.").also { stage = END_DIALOGUE }
            }
            11 -> npc("Welcome.").also { stage++ }
            12 -> end()
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = AsgarniaDialogue(player)

    override fun getIds(): IntArray = intArrayOf(-1)
}
