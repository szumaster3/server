package content.region.kandarin.quest.fishingcompo.handlers

import content.region.kandarin.quest.fishingcompo.FishingContest
import core.api.setAttribute
import core.game.interaction.MovementPulse
import core.game.interaction.NodeUsageEvent
import core.game.interaction.PluginInteraction
import core.game.interaction.PluginInteractionManager
import core.game.node.Node
import core.game.node.entity.impl.PulseType
import core.game.node.entity.player.Player
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.world.map.Location
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Items

@Initializable
class GarlicPipeInteraction : PluginInteraction() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        setIds(intArrayOf(FishingContest.GARLIC.id, 41))
        PluginInteractionManager.register(this, PluginInteractionManager.InteractionType.USEWITH)
        PluginInteractionManager.register(this, PluginInteractionManager.InteractionType.OBJECT)
        return this
    }

    override fun handle(
        player: Player,
        event: NodeUsageEvent,
    ): Boolean {
        if (event.used is Item && event.usedWith is Scenery) {
            val usedWith = event.usedWith.asScenery()
            val used = event.usedItem

            if (used.id == Items.GARLIC_1550 &&
                usedWith.id == 41 &&
                usedWith.location ==
                Location.create(
                    2638,
                    3446,
                    0,
                ) &&
                player.getQuestRepository().getStage("Fishing Contest") > 0
            ) {
                player.pulseManager.run(
                    object : MovementPulse(player, usedWith.location.transform(0, -1, 0)) {
                        override fun pulse(): Boolean {
                            player.dialogueInterpreter.sendDialogue("You stuff the garlic into the pipe.")
                            player.inventory.remove(Item(Items.GARLIC_1550))
                            setAttribute(player, "fishing_contest:garlic", true)
                            return true
                        }
                    },
                    PulseType.STANDARD,
                )
                return true
            }
        }
        return false
    }

    override fun handle(
        player: Player,
        node: Node,
    ): Boolean {
        if (node is Scenery) {
            val scenery = node.asScenery()
            if (scenery.id == 41 &&
                scenery.location ==
                Location.create(
                    2638,
                    3446,
                    0,
                ) &&
                player.getAttribute("fishing_contest:garlic", false)
            ) {
                player.pulseManager.run(
                    object : MovementPulse(player, scenery.location.transform(0, -1, 0)) {
                        override fun pulse(): Boolean {
                            player.dialogueInterpreter.sendDialogue("This is the pipe I stuffed that garlic into.")
                            return true
                        }
                    },
                    PulseType.STANDARD,
                )
                return true
            }
        }
        return false
    }

    override fun fireEvent(
        identifier: String,
        vararg args: Any,
    ): Any? {
        return null
    }
}
