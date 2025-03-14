package content.region.asgarnia.dialogue.goblinvillage

import core.game.dialogue.Dialogue
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class GoblinVillageGuardDialogue(
    player: Player? = null,
) : Dialogue(player) {
    override fun open(vararg args: Any): Boolean {
        npc = args[0] as NPC
        player("You're a long way out from the city.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 ->
                npc(
                    "I know. We guards usually stay by banks and shops,",
                    "but I got sent all the way out here to keep an eye on",
                    "the brigands loitering just south of here.",
                ).also {
                    stage++
                }
            1 -> player("Sounds more exciting than standing", "around guarding banks and shops.").also { stage++ }
            2 ->
                npc(
                    "It's not too bad. At least I don't get attacked so often",
                    "out here. Guards in the cities get killed all the time.",
                ).also {
                    stage++
                }
            3 -> player("Honestly, people these days just don't know how to behave!").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GUARD_3241)
    }
}
