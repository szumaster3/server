package content.region.kandarin.quest.fishingcompo.handlers

import content.region.kandarin.quest.fishingcompo.FishingContest
import core.game.interaction.MovementPulse
import core.game.interaction.PluginInteraction
import core.game.interaction.PluginInteractionManager
import core.game.node.Node
import core.game.node.entity.impl.PulseType
import core.game.node.entity.player.Player
import core.game.node.scenery.Scenery
import core.game.world.update.flag.context.Animation
import core.plugin.Initializable
import core.plugin.Plugin
import org.rs.consts.Quests

@Initializable
class VineInteraction : PluginInteraction() {
    override fun newInstance(arg: Any?): Plugin<Any> {
        setIds(intArrayOf(58, 2989, 2990, 2991, 2992, 2993, 2994, 2013))
        PluginInteractionManager.register(this, PluginInteractionManager.InteractionType.OBJECT)
        return this
    }

    override fun handle(
        player: Player,
        node: Node,
    ): Boolean {
        if (node is Scenery) {
            if (player.getQuestRepository().getStage(Quests.FISHING_CONTEST) in 1..99) {
                player.pulseManager.run(
                    object : MovementPulse(player, node.asScenery().location.transform(0, 0, 0)) {
                        override fun pulse(): Boolean {
                            if (player.inventory.containsItem(FishingContest.SPADE)) {
                                player.animator.animate(Animation(830))
                                player.dialogueInterpreter.sendDialogue("You find some worms.")
                                player.inventory.add(FishingContest.RED_VINE_WORM)
                            } else {
                                player.dialogueInterpreter.sendDialogue(
                                    "The ground looks promising around these vines.",
                                    "Perhaps you should dig.",
                                )
                            }
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
