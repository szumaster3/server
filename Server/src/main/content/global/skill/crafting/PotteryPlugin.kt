package content.global.skill.crafting

import core.api.*
import core.game.interaction.Clocks
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.world.map.Location
import core.tools.StringUtils
import shared.consts.Animations
import shared.consts.Items
import shared.consts.Quests
import shared.consts.Scenery
import java.util.*

class PotteryPlugin : InteractionListener {

    override fun defineListeners() {

        /*
         * Handles crafting on pottery wheel.
         */

        onUseWith(IntType.ITEM, Items.SOFT_CLAY_1761, *CraftingObject.POTTERY_WHEELS) { player, _, _ ->
            val items = CraftingDefinition.Pottery.values().map { it.unfinished }.toTypedArray()
            sendSkillDialogue(player) {
                withItems(*items)

                create { itemId, amount ->
                    val pottery = CraftingDefinition.Pottery.forId(itemId) ?: run {
                        sendMessage(player, "Invalid pottery selection.")
                        return@create
                    }

                    if (getStatLevel(player, Skills.CRAFTING) < pottery.level) {
                        sendMessage(player, "You need a crafting level of ${pottery.level} to make this.")
                        return@create
                    }

                    if (!inInventory(player, Items.SOFT_CLAY_1761)) {
                        sendMessage(player, "You have run out of clay.")
                        return@create
                    }

                    var remaining = amount

                    queueScript(player, 0, QueueStrength.WEAK) {
                        if (remaining <= 0 || !clockReady(player, Clocks.SKILLING)) return@queueScript stopExecuting(player)
                        if (!inInventory(player, Items.SOFT_CLAY_1761)) return@queueScript stopExecuting(player)

                        animate(player, 883)
                        delayClock(player, Clocks.SKILLING, 5)
                        delayScript(player, 5)

                        if (removeItem(player, Items.SOFT_CLAY_1761)) {
                            addItem(player, pottery.unfinished.id)
                            rewardXP(player, Skills.CRAFTING, pottery.exp)
                            val article = if (StringUtils.isPlusN(pottery.unfinished.name)) "an" else "a"
                            sendMessage(player, "You make the clay into $article ${pottery.unfinished.name.lowercase(Locale.getDefault())}.")

                            // Varrock & Lumbridge diary.
                            when (pottery) {
                                CraftingDefinition.Pottery.BOWL -> if (withinDistance(player, Location(3086, 3410, 0))) {
                                    setAttribute(player, "/save:diary:varrock:spun-bowl", true)
                                }
                                CraftingDefinition.Pottery.POT -> if (withinDistance(player, Location(3086, 3410, 0))) {
                                    finishDiaryTask(player, DiaryType.LUMBRIDGE, 0, 7)
                                }
                                else -> {}
                            }

                            remaining--
                        }

                        if (remaining > 0 && inInventory(player, Items.SOFT_CLAY_1761)) {
                            setCurrentScriptState(player, 0)
                            delayScript(player, 5)
                        } else stopExecuting(player)
                    }
                }

                calculateMaxAmount { amountInInventory(player, Items.SOFT_CLAY_1761) }
            }

            return@onUseWith true
        }

        /*
         * Handles firing in ovens.
         */

        fun firePottery(player: Player, pottery: CraftingDefinition.Pottery, oven: Node): Boolean {
            if (oven.id == Scenery.POTTERY_OVEN_4308 && !isQuestComplete(player, Quests.THE_FREMENNIK_TRIALS)) {
                sendMessage(player, "Only Fremenniks may use this ${oven.name.lowercase(Locale.getDefault())}.")
                return false
            }

            if (getStatLevel(player, Skills.CRAFTING) < pottery.level) {
                sendMessage(player, "You need a crafting level of ${pottery.level} to do this.")
                return false
            }
            if (!inInventory(player, pottery.unfinished.id)) {
                sendMessage(player, "You don't have any ${pottery.unfinished.name.lowercase(Locale.getDefault())} to fire.")
                return false
            }

            sendSkillDialogue(player) {
                withItems(pottery.unfinished)
                create { _, amount ->
                    var remaining = amount
                    queueScript(player, 0, QueueStrength.WEAK) { stage ->
                        if (remaining <= 0) return@queueScript stopExecuting(player)

                        when (stage) {
                            0 -> {
                                animate(player, Animations.HUMAN_FURNACE_SMELT_3243)
                                playAudio(player, 2588)
                                sendMessage(player, "You put ${pottery.unfinished.name.lowercase(Locale.getDefault())} in the oven.")
                                delayScript(player, 5)
                            }
                            1 -> {
                                sendMessage(player, "The ${pottery.product.name.lowercase(Locale.getDefault())} hardens in the oven.")
                                delayScript(player, 5)
                            }
                            else -> {
                                delayClock(player, Clocks.SKILLING, 5)
                                if (removeItem(player, pottery.unfinished)) {
                                    addItem(player, pottery.product.id)
                                    rewardXP(player, Skills.CRAFTING, pottery.fireExp)
                                    sendMessage(player, "You remove ${pottery.product.name.lowercase(Locale.getDefault())} from the oven.")

                                    // Varrock diary.
                                    when (pottery) {
                                        CraftingDefinition.Pottery.BOWL -> if (withinDistance(player, Location(3085, 3408, 0)) &&
                                            getAttribute(player, "diary:varrock:spun-bowl", false)) {
                                            finishDiaryTask(player, DiaryType.VARROCK, 0, 9)
                                        }
                                        CraftingDefinition.Pottery.POT -> if (withinDistance(player, Location(3085, 3408, 0))) {
                                            finishDiaryTask(player, DiaryType.LUMBRIDGE, 0, 8)
                                        }
                                        else -> {}
                                    }
                                    remaining--
                                }

                                if (remaining > 0) {
                                    setCurrentScriptState(player, 0)
                                    delayScript(player, 5)
                                } else stopExecuting(player)
                            }
                        }
                    }
                }
                calculateMaxAmount { player.inventory.getAmount(pottery.unfinished) }
            }
            return true
        }

        onUseWith(IntType.ITEM, CraftingDefinition.UNFIRED_POTTERY_ITEM_IDS, *CraftingObject.POTTERY_OVENS) { player, used, oven ->
            val pottery = CraftingDefinition.Pottery.forId(used.id) ?: return@onUseWith false
            firePottery(player, pottery, oven)
            return@onUseWith true
        }

        /*
         * Handles interaction with fire ovens.
         */

        on(CraftingObject.POTTERY_OVENS, IntType.SCENERY, "fire") { player, node ->
            sendSkillDialogue(player) {
                val potteryMap = mapOf(
                    Items.UNFIRED_POT_1787 to CraftingDefinition.Pottery.POT,
                    Items.UNFIRED_PIE_DISH_1789 to CraftingDefinition.Pottery.DISH,
                    Items.UNFIRED_BOWL_1791 to CraftingDefinition.Pottery.BOWL,
                    Items.UNFIRED_PLANT_POT_5352 to CraftingDefinition.Pottery.PLANT,
                    Items.UNFIRED_POT_LID_4438 to CraftingDefinition.Pottery.LID
                )

                withItems(*potteryMap.keys.toIntArray())

                create { selectedItemId, _ ->
                    val pottery = potteryMap[selectedItemId]
                    if (pottery != null) {
                        firePottery(player, pottery, node)
                    }
                }

                calculateMaxAmount { selectedItemId ->
                    amountInInventory(player, selectedItemId)
                }
            }

            return@on true
        }

        /*
         * Handles interacting with the cooking range.
         */

        on(CraftingObject.RANGE, IntType.SCENERY, "fire") { player, node ->
            if (inInventory(player, Items.UNCOOKED_STEW_2001, 1)) {
                faceLocation(player, node.location)
                openDialogue(player, 43989, Items.UNCOOKED_STEW_2001, "stew")
            }

            return@on true
        }
    }

}
