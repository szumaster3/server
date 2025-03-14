package content.region.misc.dialogue.keldagrim

import content.global.skill.crafting.casting.Gem
import core.api.removeItem
import core.api.sendMessage
import core.game.dialogue.Dialogue
import core.game.dialogue.DialogueInterpreter
import core.game.dialogue.FaceAnim
import core.game.global.action.DoorActionHandler
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import org.rs.consts.NPCs

@Initializable
class MagicDoorDialogue(
    player: Player? = null,
) : Dialogue(player) {
    private var door: Scenery? = null

    override fun open(vararg args: Any): Boolean {
        door = args[0] as Scenery
        npc(FaceAnim.OLD_CALM_TALK1, "You may not pass through this door without paying the", "trading tax.")
        return true
    }

    override fun handle(
        interfaceId: Int,
        buttonId: Int,
    ): Boolean {
        when (stage) {
            0 -> player("So how much is the tax?").also { stage++ }
            1 -> npc(FaceAnim.OLD_CALM_TALK1, "The cost is one diamond.").also { stage++ }
            2 ->
                options(
                    "Okay...",
                    "A diamond? Are you crazy?",
                    "I haven't brought my diamonds with me.",
                    "What do you do with all the diamonds you get?",
                ).also {
                    stage++
                }
            3 ->
                when (buttonId) {
                    1 -> player("Okay...").also { stage = 10 }
                    2 -> player("A diamond? Are you crazy?").also { stage = 20 }
                    3 -> player("I haven't brought my diamonds with me.").also { stage = 30 }
                    4 -> player("What do you do with all the diamonds you get?").also { stage = 40 }
                }
            10 ->
                if (!removeItem(player, Gem.DIAMOND.gem)) {
                    player("...but...")
                    stage++
                } else {
                    end()
                    DoorActionHandler.handleAutowalkDoor(player, door!!)
                    sendMessage(player, "You give the doorman a diamond.")
                }
            11 -> player("I haven't brought my diamonds with me.").also { stage = 30 }
            20 -> npc(FaceAnim.OLD_CALM_TALK1, "Not at all. Those are the rules.").also { stage = END_DIALOGUE }
            30 -> npc(FaceAnim.OLD_CALM_TALK1, "No tax, no entry.").also { stage = END_DIALOGUE }
            40 ->
                npc(
                    FaceAnim.OLD_CALM_TALK1,
                    "Ever heard of fairylights? Well how do you think we",
                    "make 'em? First we collect a pile of gems and then we",
                    "get a spider to spin 'em into a long web, we light the",
                    "jewels by imbuing each one with a little bit of magic.",
                ).also {
                    stage++
                }
            41 -> player("So you're telling me fairylights are made out of gems?").also { stage++ }
            42 ->
                npc(
                    FaceAnim.OLD_CALM_TALK1,
                    "That's right, how else could we make 'em twinkle so",
                    "beautifully?",
                ).also {
                    stage =
                        END_DIALOGUE
                }
        }
        return true
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.GATEKEEPER_3321, DialogueInterpreter.getDialogueKey(NAME))
    }

    override fun newInstance(player: Player?): Dialogue {
        return MagicDoorDialogue(player)
    }

    companion object {
        const val NAME: String = "tax door"
    }
}
