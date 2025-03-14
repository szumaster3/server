package content.region.kandarin.handlers.barbtraining.fishing

import core.api.freeSlots
import core.api.inInventory
import core.api.sendMessage
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.update.flag.context.Animation
import org.rs.consts.Animations
import org.rs.consts.Items

class BarbFishCuttingPulse(
    val player: Player,
    val fish: Int,
) : Pulse(0) {
    fun checkRequirements(): Boolean {
        if (!(freeSlots(player) >= 2 || (freeSlots(player) >= 1 && inInventory(player, Items.FISH_OFFCUTS_11334)))) {
            sendMessage(player, "You don't have enough space in your pack to attempt cutting open the fish.")
            return false
        }
        return true
    }

    override fun pulse(): Boolean {
        player.animator.animate(Animation(Animations.CRAFT_KNIFE_5244))
        player.inventory.remove(Item(fish))
        player.inventory.add(Item(Items.FISH_OFFCUTS_11334))
        player.inventory.add(
            Item(
                when (fish) {
                    Items.LEAPING_TROUT_11328, Items.LEAPING_SALMON_11330 -> Items.ROE_11324
                    Items.LEAPING_STURGEON_11332 -> Items.CAVIAR_11326
                    else -> 0
                },
            ),
        )

        player.skills.addExperience(
            Skills.COOKING,
            when (fish) {
                Items.LEAPING_TROUT_11328, Items.LEAPING_SALMON_11330 -> 10.0
                Items.LEAPING_STURGEON_11332 -> 15.0
                else -> 0.0
            },
        )

        sendMessage(player, "You cut open the fish and extract some roe, but the rest of the fish is reduced to")
        sendMessage(player, "useless fragments, which you discard.")
        return true
    }
}
