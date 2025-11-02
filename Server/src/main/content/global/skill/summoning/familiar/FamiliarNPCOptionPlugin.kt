package content.global.skill.summoning.familiar

import core.cache.def.impl.NPCDefinition.setOptionHandler
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.plugin.Initializable
import core.plugin.Plugin

/**
 * Handles interaction options for summoning familiars.
 */
@Initializable
class FamiliarNPCOptionPlugin : OptionHandler() {

    /**
     * Registers this plugin as the handler for familiar options.
     */
    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        setOptionHandler("pick-up", this)
        setOptionHandler("interact-with", this)
        setOptionHandler("interact", this)
        setOptionHandler("store", this)
        setOptionHandler("withdraw", this)
        return this
    }

    /**
     * Handles an interaction with a familiar NPC.
     */
    override fun handle(player: Player, node: Node, option: String): Boolean {
        if (node !is Familiar) {
            return false
        }
        val familiar = node
        if (familiar.getOwner() !== player) {
            player.packetDispatch.sendMessage("This is not your familiar.")
            return true
        }
        when (option) {
            "pick-up" -> {
                player.faceLocation(familiar.getFaceLocation(player.location))
                player.familiarManager.pickup()
            }
            "interact-with" -> player.dialogueInterpreter.open(343823)
            "interact" -> player.dialogueInterpreter.open(node.getId(), node)
            "store", "withdraw" -> {
                if (!familiar.isBurdenBeast) {
                    player.packetDispatch.sendMessage("This is not a beast of burden.")
                    return false
                }
                (familiar as BurdenBeast).openInterface()
            }
        }
        return true
    }
}
