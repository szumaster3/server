package content.global.skill.gather.fishing

import core.api.*
import core.game.interaction.Clocks
import core.game.interaction.IntType
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.tools.RandomFunction
import shared.consts.Animations
import shared.consts.Items
import shared.consts.NPCs
import content.region.kandarin.baxtorian.BarbarianTraining
import core.game.interaction.InteractionListener
import core.game.world.update.flag.context.Animation
import core.tools.RandomUtils
import shared.consts.Scenery

class BarbarianFishing : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles start of Heavy rod fishing technique barbarian training.
         */

        on(Scenery.BARBARIAN_BED_25268, IntType.SCENERY, "search") { player, _ ->
            if (getAttribute(player, BarbarianTraining.FISHING_BASE, false)) {
                if (!inInventory(player, Items.BARBARIAN_ROD_11323) && freeSlots(player) > 0) {
                    sendMessage(player, "You find a heavy fishing rod under the bed and take it.")
                    addItem(player, Items.BARBARIAN_ROD_11323, 1)
                } else {
                    sendMessage(player, "You don't find anything that interests you.")
                }
            } else {
                sendMessage(player, "You don't find anything that interests you.")
            }
            return@on true
        }

        /*
         * Handles barbarian fishing spots using a barbarian rod.
         */

        onUseWith(IntType.ITEM, Items.BARBARIAN_ROD_11323, NPCs.FISHING_SPOT_1176) { player, _, _ ->

            if (!clockReady(player, Clocks.SKILLING)) return@onUseWith true

            val fishingLevel = getStatLevel(player,  Skills.FISHING)
            val agilityLevel = getStatLevel(player,  Skills.AGILITY)
            val strengthLevel = getStatLevel(player, Skills.STRENGTH)
            val hasBait = anyInInventory(
                player,
                Items.FISHING_BAIT_313,
                Items.FEATHER_314,
                Items.ROE_11324,
                Items.FISH_OFFCUTS_11334,
                Items.CAVIAR_11326
            )

            when {
                fishingLevel < 48 -> {
                    sendMessage(player, "You need a Fishing level of at least 48 to fish here.")
                    return@onUseWith true
                }

                agilityLevel < 15 || strengthLevel < 15 -> {
                    val stat = if (agilityLevel < 15 && strengthLevel < 15) "agility and strength"
                    else if (agilityLevel < 15) "agility" else "strength"
                    sendMessage(player, "You need a $stat level of at least 15 to fish here.")
                    return@onUseWith true
                }

                !hasBait -> {
                    sendMessage(player, "You don't have any bait to fish with.")
                    return@onUseWith true
                }

                player.inventory.isFull -> {
                    sendMessage(player, "You don't have enough space in your inventory.")
                    return@onUseWith true
                }
            }

            var remaining = player.inventory.freeSlots()
            val anim = Animation(Animations.ROD_FISHING_622)

            queueScript(player, 0, QueueStrength.WEAK) { stage ->
                if (remaining <= 0) return@queueScript stopExecuting(player)

                when (stage) {
                    0 -> {
                        player.animate(anim)
                        delayClock(player, Clocks.SKILLING, 2)
                        delayScript(player, 2)
                    }

                    else -> {
                        val fish = getRandomFish(player)
                        val success = rollSuccess(player, fish.id)

                        if (success) {
                            removeItem(player, Items.FISH_OFFCUTS_11334) || removeItem(player, Items.FEATHER_314)

                            addItem(player, fish.id)

                            val fishXP = when (fish.id) {
                                Items.LEAPING_TROUT_11328 -> 50.0
                                Items.LEAPING_SALMON_11330 -> 70.0
                                Items.LEAPING_STURGEON_11332 -> 80.0
                                else -> 0.0
                            }
                            val stragiXP = when (fish.id) {
                                Items.LEAPING_TROUT_11328 -> 5.0
                                Items.LEAPING_SALMON_11330 -> 6.0
                                Items.LEAPING_STURGEON_11332 -> 7.0
                                else -> 0.0
                            }

                            rewardXP(player, Skills.FISHING, fishXP)
                            rewardXP(player, Skills.AGILITY, stragiXP)
                            rewardXP(player, Skills.STRENGTH, stragiXP)

                            sendMessage(player, "You catch a ${fish.name.lowercase()}.")

                            // Check for completion.
                            if (!getAttribute(player, BarbarianTraining.FISHING_BASE, false)) {
                                sendDialogueLines(
                                    player,
                                    "You feel you have learned more of barbarian ways. Otto might wish",
                                    "to talk to you more."
                                )
                                setAttribute(player, BarbarianTraining.FISHING_BASE, true)
                            }

                        } else {
                            sendMessage(player, "You fail to catch any fish.")
                        }

                        remaining--
                        if (remaining > 0 && !player.inventory.isFull) {
                            setCurrentScriptState(player, 0)
                            delayScript(player, 2)
                        } else {
                            stopExecuting(player)
                        }
                    }
                }
            }

            return@onUseWith true
        }

        /*
         * Handles cutting barbarian leaping fish
         * into roe/caviar and offcuts.
         */

        onUseWith(IntType.ITEM, Items.KNIFE_946, Items.LEAPING_TROUT_11328, Items.LEAPING_SALMON_11330, Items.LEAPING_STURGEON_11332) { player, _, fishItem ->
            if (!clockReady(player, Clocks.SKILLING)) return@onUseWith true

            val fishId = fishItem.id
            val level = player.skills.getLevel(Skills.COOKING)

            if (level < 1) {
                sendDialogue(player, "You need a Cooking level to attempt cutting this fish.")
                return@onUseWith true
            }

            val slots = freeSlots(player)
            val hasOffcuts = inInventory(player, Items.FISH_OFFCUTS_11334)
            if (slots < 2 && (slots < 1 || !hasOffcuts)) {
                sendMessage(player, "You don't have enough space in your pack to attempt cutting open the fish.")
                return@onUseWith false
            }

            val maxAmount = amountInInventory(player, fishId)
            var remaining = maxAmount
            val anim = Animation(Animations.OFFCUTS_6702)

            queueScript(player, 0, QueueStrength.WEAK) { stage ->
                if (remaining <= 0 || amountInInventory(player, fishId) <= 0) return@queueScript stopExecuting(player)

                when (stage) {
                    0 -> {
                        player.animate(anim)
                        delayClock(player, Clocks.SKILLING, 2)
                        delayScript(player, 2)
                    }

                    else -> {
                        removeItem(player, fishId)

                        val success = rollSuccess(fishId, level)
                        if (success) {
                            val product = when (fishId) {
                                Items.LEAPING_TROUT_11328, Items.LEAPING_SALMON_11330 -> Items.ROE_11324
                                Items.LEAPING_STURGEON_11332 -> Items.CAVIAR_11326
                                else -> -1
                            }
                            if (product != -1) addItem(player, product)

                            // Roll offcuts.
                            if (rollOffcuts(fishId)) addItem(player, Items.FISH_OFFCUTS_11334)

                            val xp = when (fishId) {
                                Items.LEAPING_TROUT_11328, Items.LEAPING_SALMON_11330 -> 10.0
                                Items.LEAPING_STURGEON_11332 -> 15.0
                                else -> 0.0
                            }

                            rewardXP(player, Skills.COOKING, xp)

                            sendMessage(
                                player, "You cut open the fish and extract some roe, but the rest is discarded."
                            )
                        } else {
                            sendMessage(player, "You fail to cut the fish properly and ruin it.")
                        }

                        remaining--
                        if (remaining > 0 && amountInInventory(player, fishId) > 0) {
                            setCurrentScriptState(player, 0)
                            delayScript(player, 2)
                        } else {
                            stopExecuting(player)
                        }
                    }
                }
            }

            return@onUseWith true
        }
    }

    private fun rollSuccess(fish: Int, level: Int): Boolean {
        return when (fish) {
            Items.LEAPING_TROUT_11328 -> RandomUtils.randomDouble() < ((level.coerceAtMost(99)) / 150.0)
            Items.LEAPING_SALMON_11330, Items.LEAPING_STURGEON_11332 -> RandomUtils.randomDouble() < (level.coerceAtMost(
                80
            ) / 80.0)

            else -> true
        }
    }

    private fun rollOffcuts(fish: Int): Boolean {
        val roll = RandomUtils.randomDouble()
        return when (fish) {
            Items.LEAPING_TROUT_11328 -> roll < 0.5
            Items.LEAPING_SALMON_11330 -> roll < 0.75
            Items.LEAPING_STURGEON_11332 -> roll < (5.0 / 6.0)
            else -> false
        }
    }

    private fun rollSuccess(player: Player, fish: Int): Boolean {
        val level = 1 + player.skills.getLevel(Skills.FISHING) + player.familiarManager.getBoost(Skills.FISHING)
        val hostRatio = Math.random() * fish
        val clientRatio = Math.random() * (level * 3.0 - fish)
        return hostRatio < clientRatio
    }

    private fun getRandomFish(player: Player): Item {
        val fishArray = arrayOf(Items.LEAPING_TROUT_11328, Items.LEAPING_SALMON_11330, Items.LEAPING_STURGEON_11332)
        val fishing = player.skills.getLevel(Skills.FISHING)
        val strength = player.skills.getLevel(Skills.STRENGTH)
        val agility = player.skills.getLevel(Skills.AGILITY)

        var possibleIndex = 0
        if (fishing >= 58 && (strength >= 30 && agility >= 30)) possibleIndex++
        if (fishing >= 70 && (strength >= 45 && agility >= 45)) possibleIndex++
        return Item(fishArray[RandomFunction.random(possibleIndex + 1)])
    }
}
