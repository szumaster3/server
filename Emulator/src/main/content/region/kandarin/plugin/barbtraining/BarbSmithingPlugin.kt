package content.region.kandarin.plugin.barbtraining

import core.api.*
import core.api.skill.sendSkillDialogue
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Scenery
import kotlin.math.min

class BarbSmithingPlugin : InteractionListener {
    private val bars = BarbarianWeapon.values().map { it.requiredBar }.toIntArray()

    override fun defineListeners() {

        /*
         * Handles smithing spear and hastae.
         */

        onUseWith(IntType.SCENERY, bars, Scenery.BARBARIAN_ANVIL_25349) { player, used, _ ->
            val weapon = BarbarianWeapon.product[used.id] ?: return@onUseWith true

            sendSkillDialogue(player) {
                withItems(weapon.spearId.asItem(), weapon.hastaId.asItem())
                create { id, amount ->
                    submitIndividualPulse(
                        entity = player,
                        pulse = BarbSmithingPulse(player, weapon, amount, id),
                    )
                }
                calculateMaxAmount {
                    amountInInventory(player, used.id)
                }
            }
            return@onUseWith true
        }
    }
}

private class BarbSmithingPulse(player: Player?, val weapon: BarbarianWeapon, var amount: Int, var button: Int, ) : SkillPulse<Item>(player, null) {
    val hasta = weapon.hastaId
    val spear = weapon.spearId

    override fun checkRequirements(): Boolean {
        if (getStatLevel(player, Skills.SMITHING) < weapon.requiredLevel) {
            sendMessage(player, "You need a Smithing level of ${weapon.requiredLevel} to make this.")
            return false
        }

        if (!inInventory(player, Items.HAMMER_2347)) {
            sendDialogue(player, "You need a hammer to work the metal with.")
            return false
        }

        if (!inInventory(player, weapon.requiredBar)) {
            sendDialogue(player, "You don't have the necessary material for the weapon.")
            return false
        }

        if (!inInventory(player, weapon.requiredWood)) {
            sendDialogue(player, "You don't have the necessary logs for the weapon.")
            return false
        }

        return true
    }

    override fun start() {
        super.start()
        val bar = Item(weapon.requiredBar)
        val wood = Item(weapon.requiredWood)
        val barsAmount = player.inventory.getAmount(bar)
        val woodAmount: Int = player.inventory.getAmount(wood)
        amount = min(min(barsAmount, woodAmount), amount)
    }

    override fun animate() {
        animate(player, Animations.HUMAN_ANVIL_HAMMER_SMITHING_898)
    }

    override fun reward(): Boolean {
        if (delay == 1) {
            delay = 4
            return false
        }

        var index = button
        if (player.inventory.remove(Item(weapon.requiredBar, 1)) &&
            player.inventory.remove(
                Item(
                    weapon.requiredWood,
                    1,
                ),
            )
        ) {
            sendMessage(player, "You make a ${getItemName(if (index == 0) spear else hasta)}.")
            player.inventory.add(Item(if (index == 0) spear else hasta, 1))
            rewardXP(player, Skills.SMITHING, weapon.experience)
            amount--
            return amount == 0
        }
        return true
    }

    override fun message(type: Int) {}
}

/**
 * Represents the barbarian weapons.
 */
private enum class BarbarianWeapon(val requiredWood: Int, val requiredBar: Int, val spearId: Int, val hastaId: Int, var amount: Int, val requiredLevel: Int, val experience: Double, ) {
    BRONZE(Items.LOGS_1511, Items.BRONZE_BAR_2349, Items.BRONZE_SPEAR_1237, Items.BRONZE_HASTA_11367, 1, 5, 25.00),
    IRON(Items.OAK_LOGS_1521, Items.IRON_BAR_2351, Items.IRON_SPEAR_1239, Items.IRON_HASTA_11369, 1, 20, 50.00),
    STEEL(Items.WILLOW_LOGS_1519, Items.STEEL_BAR_2353, Items.STEEL_SPEAR_1241, Items.STEEL_HASTA_11371, 1, 35, 75.00),
    MITHRIL(Items.MAPLE_LOGS_1517, Items.MITHRIL_BAR_2359, Items.MITHRIL_SPEAR_1243, Items.MITHRIL_HASTA_11373, 1, 55, 100.00),
    ADAMANT(Items.YEW_LOGS_1515, Items.ADAMANTITE_BAR_2361, Items.ADAMANT_SPEAR_1245, Items.ADAMANT_HASTA_11375, 1, 75, 125.00),
    RUNE(Items.MAGIC_LOGS_1513, Items.RUNITE_BAR_2363, Items.RUNE_SPEAR_1247, Items.RUNE_HASTA_11377, 1, 90, 150.00),
    ;

    companion object {
        /**
         * A map that associates the required bar id with the corresponding [BarbarianWeapon].
         */
        val product: Map<Int, BarbarianWeapon> = values().associateBy { it.requiredBar }

        /**
         * Returns a [BarbarianWeapon] corresponding to the given bar id.
         */
        fun getWeapon(id: Int): BarbarianWeapon? = product[id]
    }
}
