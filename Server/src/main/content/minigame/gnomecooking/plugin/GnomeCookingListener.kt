package content.minigame.gnomecooking.plugin

import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.update.flag.context.Animation
import shared.consts.Animations
import shared.consts.Components
import shared.consts.Items
import shared.consts.Scenery

class GnomeCookingListener : InteractionListener {

    private val cookLoc = intArrayOf(Scenery.GNOME_COOKER_17131, Scenery.RANGE_2728)

    private object XP {
        const val BATTA = 40.0
        const val BATTA_TOAD = 82.0
        const val BOWL = 30.0
        const val CRUNCHY = 0.0
        const val CRUNCHY_SPECIAL = 30.0
        const val GARNISH_CRUNCHY = 64.0
        const val GARNISH_BATTA = 88.0
    }

    private val battaCookMap = mapOf(
        Items.RAW_BATTA_2250       to Items.HALF_BAKED_BATTA_2249,
        Items.HALF_MADE_BATTA_9478 to Items.UNFINISHED_BATTA_9479,
        Items.HALF_MADE_BATTA_9480 to Items.UNFINISHED_BATTA_9481,
        Items.HALF_MADE_BATTA_9483 to Items.UNFINISHED_BATTA_9484,
        Items.HALF_MADE_BATTA_9485 to Items.UNFINISHED_BATTA_9486,
        Items.HALF_MADE_BATTA_9482 to Items.TOAD_BATTA_2255
    )

    private val bowlCookMap = mapOf(
        Items.RAW_GNOMEBOWL_2178  to Items.HALF_BAKED_BOWL_2177,
        Items.HALF_MADE_BOWL_9558 to Items.UNFINISHED_BOWL_9560,
        Items.HALF_MADE_BOWL_9559 to Items.TANGLED_TOADS_LEGS_2187,
        Items.HALF_MADE_BOWL_9561 to Items.UNFINISHED_BOWL_9562,
        Items.HALF_MADE_BOWL_9563 to Items.UNFINISHED_BOWL_9564
    )

    private val crunchyCookMap = mapOf(
        Items.HALF_MADE_CRUNCHY_9577 to Items.UNFINISHED_CRUNCHY_9578,
        Items.HALF_MADE_CRUNCHY_9579 to Items.UNFINISHED_CRUNCHY_9580,
        Items.HALF_MADE_CRUNCHY_9581 to Items.UNFINISHED_CRUNCHY_9582,
        Items.HALF_MADE_CRUNCHY_9583 to Items.UNFINISHED_CRUNCHY_9584,
        Items.RAW_CRUNCHIES_2202     to Items.HALF_BAKED_CRUNCHY_2201
    )

    private val equaBattasMap = mapOf(
        Items.UNFINISHED_BATTA_9486 to Items.WORM_BATTA_2253,
        Items.UNFINISHED_BATTA_9484 to Items.VEGETABLE_BATTA_2281,
        Items.UNFINISHED_BATTA_9479 to Items.CHEESE_PLUSTOM_BATTA_2259
    )

    private val crunchyRecipeMap = mapOf(
        Pair(Items.CHOCOLATE_DUST_1975, Items.UNFINISHED_CRUNCHY_9578) to Items.CHOCCHIP_CRUNCHIES_2209,
        Pair(Items.GNOME_SPICE_2169,    Items.UNFINISHED_CRUNCHY_9584) to Items.WORM_CRUNCHIES_2205,
        Pair(Items.GNOME_SPICE_2169,    Items.UNFINISHED_CRUNCHY_9580) to Items.SPICY_CRUNCHIES_2213,
        Pair(Items.EQUA_LEAVES_2128,    Items.UNFINISHED_CRUNCHY_9582) to Items.TOAD_CRUNCHIES_2217
    )

    private val bowlRecipeMap = mapOf(
        Pair(Items.EQUA_LEAVES_2128, Items.UNFINISHED_BOWL_9562) to Items.VEG_BALL_2195,
        Pair(Items.EQUA_LEAVES_2128, Items.UNFINISHED_BOWL_9564) to Items.WORM_HOLE_2191
    )

    private val cocktailMap = mapOf(
        Items.MIXED_SATURDAY_9572 to Items.MIXED_SATURDAY_9573,
        Items.MIXED_DRAGON_9576   to Items.DRUNK_DRAGON_2092
    )

    private val doughFillRecipeMap = mapOf(
        Items.BATTA_TIN_2164        to Items.RAW_BATTA_2250,
        Items.GNOMEBOWL_MOULD_2166  to Items.RAW_GNOMEBOWL_2178,
        Items.CRUNCHY_TRAY_2165     to Items.RAW_CRUNCHIES_2202
    )

    private val chocolateBombBases = listOf(Items.POT_OF_CREAM_2130, Items.CHOCOLATE_DUST_1975)
    private val chocolateBombRecipe = Items.UNFINISHED_BOWL_9560 to Items.CHOCOLATE_BOMB_2185


    override fun defineListeners() {
        on(Items.ALUFT_ALOFT_BOX_9477, IntType.ITEM, "check") { player, _, ->
            val jobId = player.getAttribute("$GC_BASE_ATTRIBUTE:$GC_JOB_ORDINAL", -1)
            if (jobId == -1) {
                sendDialogue(player, "You do not currently have a job.")
                return@on true
            }
            val job = GnomeCookingJob.values()[jobId]
            val item = player.getAttribute("$GC_BASE_ATTRIBUTE:$GC_NEEDED_ITEM", Item(0))
            val npcName = NPC(job.npc_id).name.lowercase()
            val itemName = item.name.lowercase()
            sendDialogueLines(
                player,
                "I need to deliver a $itemName to $npcName,",
                "who is ${job.tip}"
            )
            return@on true
        }

        on(Items.HALF_BAKED_BATTA_2249, IntType.ITEM, "prepare") { player, _ ->
            openInterface(player, Components.GNOME_RESTAURANT_BATTAS_434)
            return@on true
        }

        on(Items.HALF_BAKED_BOWL_2177, IntType.ITEM, "prepare") { player, _ ->
            openInterface(player, Components.GNOME_RESTAURANT_BOWL_435)
            return@on true
        }

        on(Items.COCKTAIL_SHAKER_2025, IntType.ITEM, "mix-cocktail") { player, _ ->
            openInterface(player, Components.GNOME_RESTAURANT_COCKTAIL_436)
            return@on true
        }

        on(Items.HALF_BAKED_CRUNCHY_2201, IntType.ITEM, "prepare") { player, _ ->
            openInterface(player, Components.GNOME_RESTAURANT_CRUNCHY_437)
            return@on true
        }

        cookLoc.forEach { loc ->
            battaCookMap.forEach { (raw, product) ->
                onUseWith(IntType.SCENERY, loc, raw) { player, _, _ ->
                    val xp = if (product == Items.TOAD_BATTA_2255) XP.BATTA_TOAD else XP.BATTA
                    cookItem(player, raw, product, xp)
                    return@onUseWith true
                }
            }
        }

        cookLoc.forEach { loc ->
            bowlCookMap.forEach { (raw, product) ->
                onUseWith(IntType.SCENERY, loc, raw) { player, _, _ ->
                    cookItem(player, raw, product, XP.BOWL, addMould = product > 2180)
                    return@onUseWith true
                }
            }
        }

        cookLoc.forEach { loc ->
            crunchyCookMap.forEach { (raw, product) ->
                onUseWith(IntType.SCENERY, loc, raw) { player, _, _ ->
                    val xp = if (raw == 2202) XP.CRUNCHY_SPECIAL else XP.CRUNCHY
                    cookItem(player, raw, product, xp, addForm = raw != 2202)
                    return@onUseWith true
                }
            }
        }

        equaBattasMap.forEach { (unfinished, finished) ->
            onUseWith(IntType.ITEM, Items.EQUA_LEAVES_2128, unfinished) { player, used, with ->
                if (removeBoth(player, used.asItem(), with.asItem())) {
                    addItem(player, finished)
                    rewardXP(player, Skills.COOKING, XP.GARNISH_BATTA)
                }
                return@onUseWith true
            }
        }

        onUseWith(IntType.ITEM, Items.GNOME_SPICE_2169, Items.UNFINISHED_BATTA_9481) { player, used, with ->
            if (removeBoth(player, used.asItem(), with.asItem())) {
                addItem(player, Items.FRUIT_BATTA_2277)
                rewardXP(player, Skills.COOKING, XP.GARNISH_BATTA)
            }
            return@onUseWith true
        }


        crunchyRecipeMap.forEach { (pair, product) ->
            val (garnish, base) = pair
            onUseWith(IntType.ITEM, garnish, base) { player, _, _ ->
                if (removeBoth(player, Item(garnish), Item(base))) {
                    addItem(player, product)
                    rewardXP(player, Skills.COOKING, XP.GARNISH_CRUNCHY)
                }
                return@onUseWith true
            }
        }

        bowlRecipeMap.forEach { (pair, product) ->
            val (garnish, base) = pair
            onUseWith(IntType.ITEM, garnish, base) { player, _, _ ->
                if (removeBoth(player, Item(garnish), Item(base))) addItem(player, product)
                return@onUseWith true
            }
        }

        cookLoc.forEach { loc ->
            cocktailMap.forEach { (raw, cooked) ->
                onUseWith(IntType.SCENERY, loc, raw) { player, _, _ ->
                    cookItem(player, raw, cooked, 0.0)
                    return@onUseWith true
                }
            }
        }

        chocolateBombBases.forEach { base ->
            onUseWith(IntType.ITEM, base, chocolateBombRecipe.first) { player, _, _ ->
                val inv = player.inventory
                val requiredCream = Item(Items.POT_OF_CREAM_2130, 2)
                val requiredChocDust = Item(Items.CHOCOLATE_DUST_1975)
                if (!inv.containsItem(requiredCream) || !inv.containsItem(requiredChocDust)) {
                    sendDialogue(player, "You don't have enough ingredients to finish that.")
                    return@onUseWith true
                }
                inv.remove(requiredCream)
                inv.remove(requiredChocDust)
                inv.remove(Item(chocolateBombRecipe.first))
                inv.add(Item(chocolateBombRecipe.second))
                return@onUseWith true
            }
        }

        doughFillRecipeMap.forEach { (mould, product) ->
            onUseWith(IntType.ITEM, Items.GIANNE_DOUGH_2171, mould) { player, used, with ->
                val inv = player.inventory
                if (inv.remove(used.asItem()) && inv.remove(with.asItem())) {
                    inv.add(Item(product))
                }
                return@onUseWith true
            }
        }
    }

    private fun cookItem(player: Player, rawId: Int, productId: Int, xp: Double, addMould: Boolean = false, addForm: Boolean = false) {
        player.lock()
        player.animate(Animation(Animations.HUMAN_MAKE_PIZZA_883))
        queueScript(player, 1, QueueStrength.SOFT) { stage ->
            val inv = player.inventory
            when (stage) {
                0 -> {
                    if (inv.remove(Item(rawId))) {
                        inv.add(Item(productId))
                        if (addMould) inv.add(Item(Items.GNOMEBOWL_MOULD_2166))
                        if (addForm) inv.add(Item(2165))
                        if (xp > 0.0) rewardXP(player, Skills.COOKING, xp)
                        player.unlock()
                    }
                    keepRunning(player)
                }
                else -> {
                    player.unlock()
                    stopExecuting(player)
                }
            }
        }
    }

    private fun removeBoth(player: Player, a: Item, b: Item): Boolean {
        val inv = player.inventory
        return inv.remove(a) && inv.remove(b)
    }
}
