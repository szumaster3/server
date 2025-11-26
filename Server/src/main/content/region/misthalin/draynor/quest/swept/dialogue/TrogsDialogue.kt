package content.region.misthalin.draynor.quest.swept.dialogue

import content.data.GameAttributes
import content.global.plugins.interfaces.DiangoReclaimableInterface.Companion.getEligibleItems
import core.api.getAttribute
import core.api.inEquipment
import core.game.dialogue.Dialogue
import core.game.dialogue.FaceAnim
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.worldevents.WorldEvent
import core.game.worldevents.WorldEvents.get
import core.plugin.Initializable
import core.tools.END_DIALOGUE
import shared.consts.Items
import shared.consts.NPCs
import java.util.*

/**
 * Represents the Trogs dialogue.
 */
@Initializable
class TrogsDialogue(player: Player? = null) : Dialogue(player) {

    override fun open(vararg args: Any?): Boolean {
        npc = args[0] as NPC
        if (!inEquipment(player, Items.CATSPEAK_AMULET_4677, 1)) {
            npc(FaceAnim.CHILD_NEUTRAL, "Meow?")
            return true
        }

        if (isHalloween) {
            val eligibleItems = getEligibleItems(player)
            if (hasCostumeItems(player, eligibleItems)) {
                npc(FaceAnim.CHILD_NEUTRAL, "Rather nice costume, that. Are you trick-or-treating this year, then?")
            } else if (hasAnyCostumeItem(player, eligibleItems)) {
                npc(FaceAnim.CHILD_NEUTRAL, "Oh, I see you're wearing one costume item! Aren't you planning on trick-or-treating this year?")
            } else {
                npc(FaceAnim.CHILD_NEUTRAL, "No costume, eh? Aren't you planning on trick-or-treating this year?")
            }

            stage = END_DIALOGUE
            return true
        }

        if (getAttribute(player, GameAttributes.MINI_PURPLE_CAT_COMPLETE, false)) {
            player("So, how do you like being purple?")
            stage = 10
            return true
        }

        player("You're purple!")
        stage = 0
        return true
    }

    private val isHalloween: Boolean
        get() = Optional.ofNullable(get("halloween"))
            .map { event: WorldEvent -> "halloween" == event.name }
            .orElse(false)

    private fun hasCostumeItems(player: Player, eligibleItems: Array<Item?>?): Boolean {
        for (item in eligibleItems!!) {
            if (player.inventory.contains(item!!.id, item.amount)) {
                return true
            }
        }
        return false
    }

    private fun hasAnyCostumeItem(player: Player, eligibleItems: Array<Item?>?): Boolean {
        for (item in eligibleItems!!) {
            if (player.inventory.containsAtLeastOneItem(item)) {
                return true
            }
        }
        return false
    }

    override fun handle(interfaceId: Int, buttonId: Int): Boolean {
        when (stage) {
            0 -> npc(FaceAnim.CHILD_NEUTRAL, "Yes. Well spotted.").also { stage++ }
            1 -> player(FaceAnim.HALF_ASKING, "Are you naturally, you know, that colour? It's a rather vibrant hue.").also { stage++ }
            2 -> npc(FaceAnim.CHILD_NEUTRAL, "Oh, no, I used to be more of a tortoiseshell until Wendy there started playing around.").also { stage++ }
            3 -> player("Playing around?").also { stage++ }
            4 -> npc(FaceAnim.CHILD_NEUTRAL, "Yes. I'm sure she'll tell you about it if you ask.").also { stage = END_DIALOGUE }
            10 -> npc(FaceAnim.CHILD_NEUTRAL, "Well, I felt rather conspicuous at first, but now I've grown to rather like the colour.").also { stage++ }
            11 -> player("That's lucky.").also { stage++ }
            12 -> npc(FaceAnim.CHILD_NEUTRAL, "Quite. As far as I know, Wendy doesn't know how to reverse the process.").also { stage = END_DIALOGUE }
        }
        return true
    }

    override fun newInstance(player: Player): Dialogue {
        return TrogsDialogue(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.TROGS_8202)
    }
}
