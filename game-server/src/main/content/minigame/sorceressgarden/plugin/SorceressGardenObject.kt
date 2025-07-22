package content.minigame.sorceressgarden.plugin

import core.api.getStatLevel
import core.api.sendItemDialogue
import core.cache.def.impl.SceneryDefinition
import core.game.global.action.DoorActionHandler.handleAutowalkDoor
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.scenery.Scenery
import core.plugin.Plugin
import org.rs.consts.Items

/**
 * Handles the sorceress garden gates.
 * @author Vexia
 */
class SorceressGardenObject : OptionHandler() {

    override fun handle(player: Player, node: Node, option: String): Boolean {
        val def = SorceressGardenPlugin.SeasonDefinitions.forGateId((node as Scenery).id)
        if (def != null) {
            if (getStatLevel(player, Skills.THIEVING) < def.level) {
                sendItemDialogue(player, Items.HIGHWAYMAN_MASK_10692, "You need Thieving level of " + def.level + " to pick the lock of this gate.")
                return true
            }
            handleAutowalkDoor(player, node)
        }
        return true
    }

    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        SceneryDefinition.forId(21709).handlers["option:open"] = this
        SceneryDefinition.forId(21753).handlers["option:open"] = this
        SceneryDefinition.forId(21731).handlers["option:open"] = this
        SceneryDefinition.forId(21687).handlers["option:open"] = this
        return this
    }
}
