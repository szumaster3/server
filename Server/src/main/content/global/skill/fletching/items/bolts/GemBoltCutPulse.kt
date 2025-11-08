package content.global.skill.fletching.items.bolts

import core.api.*
import core.game.interaction.Clocks
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import shared.consts.Items
import shared.consts.Sounds

class GemBoltCutPulse(player: Player?, node: Item?, private val gem: GemBolt, private var amount: Int) : SkillPulse<Item?>(player, node) {

    /**
     * Represents the ticks passed.
     */
    private var ticks = 0

    /**
     * Represents the cut animations.
     */
    private val animation = gem.animation

    override fun checkRequirements(): Boolean {
        if (getStatLevel(player, Skills.FLETCHING) < gem.level) {
            sendDialogue(player, "You need a fletching level of " + gem.level + " or above to do that.")
            return false
        }

        if(!hasSpaceFor(player, Item(gem.tip))){
            sendDialogue(player, "You do not have enough inventory space.")
            return false
        }

        return player.inventory.containsItem(Item(gem.gem))
    }

    override fun animate() {
        if (ticks % 5 == 0) {
            player.animate(animation)
            playAudio(player, Sounds.CHISEL_2586)
        }
    }

    override fun reward(): Boolean {
        if (!clockReady(player, Clocks.SKILLING)) return false
        delayClock(player, Clocks.SKILLING, 5)

        val reward =
            when (gem.gem) {
                Items.OYSTER_PEARLS_413, Items.ONYX_6573 -> Item(gem.tip, 24)
                Items.OYSTER_PEARL_411 -> Item(gem.tip, 6)
                else -> Item(gem.tip, 12)
            }

        if (player.inventory.remove(Item(gem.gem))) {
            player.inventory.add(reward)
            player.skills.addExperience(Skills.FLETCHING, gem.experience, true)
            player?.sendMessage("You use your chisel to fetch small bolt tips.")
        }
        amount--
        return amount <= 0
    }
}
