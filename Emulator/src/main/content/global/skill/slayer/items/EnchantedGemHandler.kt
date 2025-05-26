package content.global.skill.slayer.items

import content.global.skill.slayer.SlayerManager
import core.api.sendMessage
import core.cache.def.impl.ItemDefinition
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Items

/**
 * Handles interaction with Enchanted gem item.
 */
@Initializable
class EnchantedGemHandler : OptionHandler() {

    override fun handle(player: Player, node: Node, option: String): Boolean {
        return if (!SlayerManager.getInstance(player).hasStarted()) {
            sendMessage(player, "You try to activate the gem...")
            true
        } else {
            player.dialogueInterpreter.open(77777)
            true
        }
    }

    override fun isWalk(): Boolean = false

    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        ItemDefinition.forId(Items.ENCHANTED_GEM_4155).handlers["option:activate"] = this
        return this
    }
}
