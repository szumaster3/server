package content.global.activity.ttrail.challenge

import content.global.activity.ttrail.ClueLevel
import content.global.activity.ttrail.ClueScrollPlugin
import core.api.addItem
import core.api.interaction.getNPCName
import core.api.removeItem
import core.api.sendInputDialogue
import core.api.sendItemDialogue
import core.api.ui.setInterfaceText
import core.game.interaction.Option
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.world.map.zone.ZoneBorders
import org.rs.consts.Components
import org.rs.consts.Items

/**
 * Representing a challenge clue scroll.
 *
 * @param question  the question associated with the clue.
 * @param npc       the NPC related to the clue.
 * @param answer    the answer to the clue.
 * @param name      the name of the clue scroll.
 * @param clueId    the unique identifier of the clue.
 * @param level     the difficulty level of the clue.
 * @param borders   the zone borders where the clue is located.
 */
abstract class ChallengeClueScroll(
    name: String?,
    clueId: Int,
    level: ClueLevel?,
    val question: String?,
    val npc: Int?,
    val answer: Int?,
    vararg borders: ZoneBorders,
) : ClueScrollPlugin(name, clueId, level, Components.TRAIL_MAP09_345, *borders) {

    /**
     * Shows clue text to the player.
     *
     * @param player The player reading the clue.
     */
    override fun read(player: Player) {
        repeat(8) { index ->
            setInterfaceText(player, "", interfaceId, index + 1)
        }

        super.read(player)
        val npc = getNPCName(npc!!).lowercase()
        setInterfaceText(player, "<br><br><br><br><br> Speak to $npc.", interfaceId, 1)
    }

    /**
     * Handles player interaction with the NPC for this clue.
     */
    override fun interact(
        e: Entity,
        target: Node,
        option: Option,
    ): Boolean {
        val player = e as? Player ?: return false
        val npc = target as? core.game.node.entity.npc.NPC ?: return false

        if (npc.id != this.npc) return false

        sendInputDialogue(player, true, "Your answer:") { value: Any ->
            val answer = (value as? Int) ?: return@sendInputDialogue

            if (answer == this.answer) {
                reward(player)
            }
        }

        return true
    }

    companion object {
        /**
         * Finds a challenge clue scroll in player's inventory for a given NPC.
         */
        fun getClueForNpc(player: Player, npc: core.game.node.entity.npc.NPC): ChallengeClueScroll? {
            return player.inventory.toArray()
                .filterNotNull()
                .mapNotNull { ClueScrollPlugin.getClueScrolls()[it.id] }
                .filterIsInstance<ChallengeClueScroll>()
                .firstOrNull { it.npc == npc.id }
        }
    }
}
