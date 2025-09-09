package content.region.kandarin.baxtorian.barbtraining.plugin

import core.game.node.entity.player.Player
import core.game.world.update.flag.context.Animation
import core.api.*
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.task.Pulse
import shared.consts.Animations
import shared.consts.Items
import core.tools.RandomUtils

/**
 * Handles the cutting of barbarian leaping fish into roe/caviar and offcuts.
 */
class BarbarianFishCuttingPulse(val player: Player, val fish: Int) : Pulse(0) {

    override fun pulse(): Boolean {
        player.animator.animate(Animation(Animations.OFFCUTS_6702))
        player.inventory.remove(Item(fish))

        val level = player.skills.getLevel(Skills.COOKING)
        val success = rollSuccess(fish, level)

        if (success) {
            /*
             * Roe or caviar product.
             */
            val product = when (fish) {
                Items.LEAPING_TROUT_11328, Items.LEAPING_SALMON_11330 -> Items.ROE_11324
                Items.LEAPING_STURGEON_11332 -> Items.CAVIAR_11326
                else -> -1
            }
            if (product != -1) {
                player.inventory.add(Item(product))
            }

            /*
             * Fish offcuts (independent roll).
             */
            if (rollOffcuts(fish)) {
                player.inventory.add(Item(Items.FISH_OFFCUTS_11334))
            }

            /*
             * Cooking XP.
             */
            val xp = when (fish) {
                Items.LEAPING_TROUT_11328, Items.LEAPING_SALMON_11330 -> 10.0
                Items.LEAPING_STURGEON_11332 -> 15.0
                else -> 0.0
            }
            player.skills.addExperience(Skills.COOKING, xp)

            sendMessage(player, "You cut open the fish and extract some roe, but the rest of the fish is reduced to")
            sendMessage(player, "useless fragments, which you discard.")
        } else {
            sendMessage(player, "You fail to cut the fish properly and ruin it.")
        }

        return true
    }

    /**
     * Rolls for success when attempting to cut a leaping fish.
     *
     * @param fish the fish being cut
     * @param level the player cooking level
     * @return true if the cut succeeds, false if it fails
     */
    private fun rollSuccess(fish: Int, level: Int): Boolean {
        return when (fish) {
            Items.LEAPING_TROUT_11328 -> {
                val cappedLevel = level.coerceAtMost(99)
                val chance = (1.0 + (cappedLevel - 1) * (98.0 / 98.0)) / 150.0
                RandomUtils.randomDouble() < chance
            }
            Items.LEAPING_SALMON_11330,
            Items.LEAPING_STURGEON_11332 -> {
                val cappedLevel = level.coerceAtMost(80)
                val chance = cappedLevel / 80.0
                RandomUtils.randomDouble() < chance
            }
            else -> true
        }
    }

    /**
     * Rolls for obtaining fish offcuts after a successful cut.
     *
     * @param fish the fish being cut
     * @return true if offcuts are obtained, false otherwise
     */
    private fun rollOffcuts(fish: Int): Boolean {
        val roll = RandomUtils.randomDouble()
        return when (fish) {
            Items.LEAPING_TROUT_11328 -> roll < 0.5             // 1/2
            Items.LEAPING_SALMON_11330 -> roll < 0.75           // 3/4
            Items.LEAPING_STURGEON_11332 -> roll < (5.0 / 6.0)  // 5/6
            else -> false
        }
    }
}
