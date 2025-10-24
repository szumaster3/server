package content.global.skill.summoning.familiar

import content.global.skill.summoning.objectpets.Pets
import core.cache.def.impl.ItemDefinition.Companion.forId
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.plugin.Initializable
import core.plugin.Plugin
import shared.consts.Items

/**
 * The familiar item option plugin.
 */
@Initializable
class FamiliarItemOptionPlugin : OptionHandler() {
    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        for (p in Pets.values()) {
            val def = forId(p.babyItemId) ?: continue
            def.handlers["option:drop"] = this
            def.handlers["option:release"] = this
            if (p.grownItemId > -1) {
                forId(p.grownItemId).handlers["option:drop"] = this
                forId(p.grownItemId).handlers["option:release"] = this
            }
            if (p.overgrownItemId > -1) {
                forId(p.overgrownItemId).handlers["option:drop"] = this
                forId(p.overgrownItemId).handlers["option:release"] = this
            }
        }
        return this
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        when (option) {
            "drop" -> {
                player.familiarManager.summon((node as Item), true)
                return true
            }

            "release" -> {
                if (node.id == Items.CLOCKWORK_CAT_7771) {
                    player.familiarManager.summon((node as Item), true)
                    return true
                }
                if (player.inventory.remove((node as Item))) {
                    player.dialogueInterpreter.sendDialogues(player, null, "Run along; I'm setting you free.")
                }
                return true
            }
        }
        return true
    }

    override fun isWalk(): Boolean {
        return false
    }
}
