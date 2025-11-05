package content.region.misthalin.varrock.dialogue

import core.api.openInterface
import core.api.openNpcShop
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Components
import shared.consts.NPCs

/**
 * Represents the Asyff dialogue.
 */
@Initializable
class AsyffDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        npc(FaceAnim.FRIENDLY, "Now you look like someone who goes to", "a lot of fancy dress parties.")
        return true
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> player(FaceAnim.ASKING, "Err...what are you saying exactly?").also { stage++ }
            1 -> npc(FaceAnim.FRIENDLY, "I'm just saying that perhaps you would", "like to peruse my selection of garments.").also { stage++ }
            2 -> npc(FaceAnim.FRIENDLY, "Or, if that doesn't interest you, then maybe you have", "something else to offer? I'm always on the look out", "for interesting or unusual new materials.").also { stage++ }
            3 -> options("Okay, let's see what you've got then.", "Can you make clothing suitable for hunting in?", "I think I might just leave the perusing for now thanks.", "What sort of unusual materials did you have in mind?").also { stage++ }
            4 -> when (buttonId) {
                1 -> player("Okay, let's see what you've got then.").also { stage = 5 }
                2 -> player("Can you make clothing suitable for hunting?").also { stage = 6 }
                3 -> player("I think I might just leave the perusing for now, thanks.").also { stage = END_DIALOGUE }
                4 -> player("What sort of unusual materials did you have in mind?").also { stage = 7 }
            }
            5 -> {
                end()
                openNpcShop(player, NPCs.FANCY_DRESS_SHOP_OWNER_554)
            }
            6 -> {
                end()
                openInterface(player, Components.CUSTOM_FUR_CLOTHING_477)
            }
            7 -> npc("Well, some more colourful feathers might be useful.", "For some surreal reason, all I normally seem to get offered", "are large quantities of rather beaten up chicken feathers.").also { stage++ }
            8 -> npc("People must have some very strange pastimes around", "these parts, that's all I can say.").also { stage++ }
            9 -> player("Ok, let's see what you've got then.").also { stage = 5 }
        }
        return true
    }

    override fun newInstance(player: Player?): Dialogue = AsyffDialogue(player)

    override fun getIds(): IntArray = intArrayOf(NPCs.FANCY_DRESS_SHOP_OWNER_554)
}
