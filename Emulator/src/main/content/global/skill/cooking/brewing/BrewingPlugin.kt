package content.global.skill.cooking.brewing

import core.api.*
import core.game.interaction.InteractionListener
import core.game.node.entity.player.Player
import core.game.node.item.Item
import org.rs.consts.Items
import org.rs.consts.Scenery

val KELDAGRIM_LOC = 11679
val PORT_PHASMATYS_LOC = 14747

val varbits = mapOf(
    PORT_PHASMATYS_LOC to BrewingVat.PORT_PHAS, KELDAGRIM_LOC to BrewingVat.KELDAGRIM
)

/**
 * Handles brewing interactions.
 *
 * @author GregF, Makar
 */
class BrewingPlugin : InteractionListener {
    private val aleIngredientsId = Brewable.getIngredients()
    private val vatsNeedingYeast = intArrayOf(Scenery.FERMENTING_VAT_7441, Scenery.FERMENTING_VAT_7444, Scenery.FERMENTING_VAT_7449, Scenery.FERMENTING_VAT_7454, Scenery.FERMENTING_VAT_7459, Scenery.FERMENTING_VAT_7464, Scenery.FERMENTING_VAT_7469, Scenery.FERMENTING_VAT_7474, Scenery.FERMENTING_VAT_7479, Scenery.FERMENTING_VAT_7484, Scenery.FERMENTING_VAT_7489, Scenery.FERMENTING_VAT_8871)
    private val drainBarrels = intArrayOf(Scenery.BARREL_7408, Scenery.BARREL_7409, Scenery.BARREL_7410)
    private val barrels = intArrayOf(Scenery.KELDA_STOUT_8870, Scenery.DWARVEN_STOUT_7411, Scenery.ASGARNIAN_ALE_7413, Scenery.GREENMANS_ALE_7415, Scenery.WIZARDS_MIND_BOMB_7417, Scenery.DRAGON_BITTER_7419, Scenery.MOONLIGHT_MEAD_7421, Scenery.AXEMAN_S_FOLLY_7423, Scenery.CHEF_S_DELIGHT_7425, Scenery.SLAYER_S_RESPITE_7427, Scenery.CIDER_7429, Scenery.MATURE_DWARVEN_STOUT_7412, Scenery.MATURE_ASGARNIAN_ALE_7414, Scenery.MATURE_GREENMANS_ALE_7416, Scenery.MATURE_WIZARDS_MIND_BOMB_7418, Scenery.MATURE_DRAGON_BITTER_7420, Scenery.MATURE_MOONLIGHT_MEAD_7422, Scenery.MATURE_AXEMAN_S_FOLLY_7424, Scenery.MATURE_CHEF_S_DELIGHT_7426, Scenery.MATURE_SLAYER_S_RESPITE_7428, Scenery.MATURE_CIDER_7430)

    override fun defineListeners() {

        /*
         * Handles adding ale ingredients to an empty fermenting vat.
         */

        onUseWith(SCENERY, aleIngredientsId, Scenery.FERMENTING_VAT_7441) { player, used, _ ->
            if (used.id == Items.APPLE_MUSH_5992) {
                sendMessage(player, "Apple mush needs to be added to an empty vat.")
                return@onUseWith false
            }
            getVat(player).addIngredient(used as Item)
            return@onUseWith true
        }

        /*
         * Handles adding 2 buckets of water to the fermenting vat.
         */

        onUseWith(SCENERY, Items.BUCKET_OF_WATER_1929, Scenery.FERMENTING_VAT_7437) { player, _, _ ->
            if (removeItem(player, Item(Items.BUCKET_OF_WATER_1929, 2))) {
                addItem(player, Items.BUCKET_1925, 2)
                sendMessage(player, "You add some water to the vat.")
                getVat(player).addWater()
            } else {
                sendMessage(player, "You need 2 buckets of water.")
            }
            return@onUseWith true
        }

        /*
         * Handles adding apple mush to the fermenting vat.
         */

        onUseWith(SCENERY, Items.APPLE_MUSH_5992, Scenery.FERMENTING_VAT_7437) { player, used, _ ->
            getVat(player).addIngredient(used as Item)
            return@onUseWith true
        }

        /*
         * Handles adding barley malt to the fermenting vat.
         */

        onUseWith(SCENERY, Items.BARLEY_MALT_6008, Scenery.FERMENTING_VAT_7438) { player, _, _ ->
            if (removeItem(player, Item(Items.BARLEY_MALT_6008, 2))) {
                sendMessage(player, "You add some barley malt to the vat.")
                getVat(player).addMalt()
            } else {
                sendMessage(player, "You need 2 barley malt.")
            }
            return@onUseWith true
        }

        /*
         * Handles adding ale yeast to vats that require yeast.
         */

        onUseWith(SCENERY, Items.ALE_YEAST_5767, *vatsNeedingYeast) { player, _, _ ->
            if (removeItem(player, Items.ALE_YEAST_5767)) {
                getVat(player).addYeast()
                sendMessage(player, "You add some yeast to the vat.")
                addItem(player, Items.EMPTY_POT_1931)
            }
            return@onUseWith true
        }

        /*
         * Handles turning the valve on the fermenting vat.
         */

        on(intArrayOf(Scenery.VALVE_7442, Scenery.VALVE_7443), SCENERY, "turn") { player, _ ->
            sendMessage(player, "You turn the valve.")
            getVat(player).turnValve()
            return@on true
        }

        /*
         * Handles draining the barrel.
         */

        on(drainBarrels, SCENERY, "Drain") { player, _ ->
            sendMessage(player, "You drain the barrel.")
            getVat(player).drainBarrel()
            return@on true
        }

        /*
         * Handles leveling the barrel using a container such as a beer glass or calquat keg.
         */

        on(barrels, SCENERY, "Level") { player, _ ->
            queueScript(player, 1) {
                val goAgain: Boolean
                if (hasAnItem(player, Items.BEER_GLASS_1919).exists()) {
                    goAgain = getVat(player).levelBarrel(Items.BEER_GLASS_1919)
                } else if (hasAnItem(player, Items.CALQUAT_KEG_5769).exists()) {
                    goAgain = getVat(player).levelBarrel(Items.CALQUAT_KEG_5769)
                } else {
                    goAgain = false
                    sendMessage(player, "You need a container to empty the barrel.")
                }
                return@queueScript if (goAgain) delayScript(player, 1) else stopExecuting(player)
            }
            return@on true
        }

        /*
         * Handles using a container (beer glass or calquat keg) on the barrel to level it.
         */

        onUseWith(SCENERY, intArrayOf(Items.BEER_GLASS_1919, Items.CALQUAT_KEG_5769), *barrels) { player, used, _ ->
            getVat(player).levelBarrel(used.id)
            return@onUseWith true
        }
    }

    private fun getVat(player: Player): FermentingVat {
        return varbits.getValue(player.location.regionId).getVat(player)
    }
}