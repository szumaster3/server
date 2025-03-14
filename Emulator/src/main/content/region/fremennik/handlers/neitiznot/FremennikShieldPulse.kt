package content.region.fremennik.handlers.neitiznot

import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs.consts.Animations
import org.rs.consts.Items

class FremennikShieldPulse(
    player: Player?,
    node: Item,
    var amount: Int,
) : SkillPulse<Item>(player, null) {
    val splitAnimation = Animations.HUMAN_SPLIT_LOGS_5755
    var ticks = 0

    override fun checkRequirements(): Boolean {
        if (!anyInInventory(
                player,
                Items.HAMMER_2347,
                Items.ARCTIC_PINE_LOGS_10810,
                Items.ROPE_954,
                Items.BRONZE_NAILS_4819,
            )
        ) {
            sendMessage(player, "You don't have required items in your inventory.")
            return false
        }
        if (amountInInventory(player, Items.ARCTIC_PINE_LOGS_10810) < 2) {
            sendMessage(player, "You need at least 2 arctic pine logs to do this.")
            return false
        }
        if (!inInventory(player, Items.ROPE_954)) {
            sendMessage(player, "You will need a rope in order to do this.")
            return false
        }
        if (!inInventory(player, Items.HAMMER_2347) && inInventory(player, Items.BRONZE_NAILS_4819)) {
            sendMessage(player, "You need a hammer to force the nails in with.")
            return false
        }
        if (!inInventory(player, Items.BRONZE_NAILS_4819)) {
            sendMessage(player, "You need bronze nails for this.")
            return false
        }
        return true
    }

    override fun animate() {
        animate(player, splitAnimation)
    }

    override fun reward(): Boolean {
        if (ticks == 1) {
            delay = 3
            return false
        }

        if (player.inventory.remove(
                Item(Items.ARCTIC_PINE_LOGS_10810, 2),
                Item(Items.ROPE_954, 1),
                Item(Items.BRONZE_NAILS_4819, 1),
            )
        ) {
            rewardXP(player, Skills.CRAFTING, 34.0)
            addItem(player, Items.FREMENNIK_ROUND_SHIELD_10826, 1)
            sendMessage(player, "You make a Fremennik round shield.")
            amount--
            return amount == 0
        }

        return true
    }

    override fun message(type: Int) {}
}
