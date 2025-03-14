package content.global.activity.ttrail

import core.api.interaction.getNPCName
import core.api.ui.setInterfaceText
import core.game.interaction.Option
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.player.Player
import core.game.world.map.zone.ZoneBorders

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
) : ClueScrollPlugin(name, clueId, level, 345, *borders) {
    override fun read(player: Player) {
        repeat(8) { index ->
            setInterfaceText(player, "", interfaceId, index + 1)
        }

        super.read(player)
        val npc = getNPCName(npc!!).lowercase()
        setInterfaceText(player, "<br><br><br><br><br> Speak to $npc.", interfaceId, 1)
    }

    override fun interact(
        e: Entity,
        target: Node,
        option: Option,
    ): Boolean {
        return super.interact(e, target, option)
    }
}
