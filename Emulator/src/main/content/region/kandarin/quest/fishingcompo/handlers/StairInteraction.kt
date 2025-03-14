package content.region.kandarin.quest.fishingcompo.handlers

import core.game.interaction.MovementPulse
import core.game.interaction.PluginInteraction
import core.game.interaction.PluginInteractionManager
import core.game.node.Node
import core.game.node.entity.impl.PulseType
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.world.repository.Repository.findNPC
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Quests

@Initializable
class StairInteraction : PluginInteraction() {
    override fun handle(
        player: Player,
        node: Node,
    ): Boolean {
        if (!player.getQuestRepository().isComplete(Quests.FISHING_CONTEST)) {
            val `object` = node.asScenery()
            when (`object`.id) {
                57 -> {
                    handleStairs(player, 232, `object`)
                    return true
                }

                55 -> {
                    handleStairs(player, 3679, `object`)
                    return true
                }
            }
        }
        return false
    }

    private fun handleStairs(
        player: Player,
        npc_id: Int,
        scenery: Scenery,
    ) {
        player.pulseManager.run(
            object : MovementPulse(player, scenery.location.transform(0, 2, 0)) {
                override fun pulse(): Boolean {
                    val n = findNPC(npc_id)
                    if (n == null) {
                        player.sendMessage("Are you in a world without NPCs? What did you do?")
                        return true
                    }
                    player.dialogueInterpreter.open(npc_id, n)
                    return true
                }
            },
            PulseType.STANDARD,
        )
    }

    override fun newInstance(arg: Any?): Plugin<Any> {
        setIds(intArrayOf(57, 55))
        PluginInteractionManager.register(this, PluginInteractionManager.InteractionType.OBJECT)
        return this
    }

    override fun fireEvent(
        identifier: String,
        vararg args: Any,
    ): Any? {
        return null
    }
}
