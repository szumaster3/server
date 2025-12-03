package content.global.skill.crafting

import core.api.*
import core.game.container.impl.EquipmentContainer
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.InterfaceListener
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryManager
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.map.Location
import shared.consts.Animations
import shared.consts.Components
import shared.consts.Items
import shared.consts.Sounds

class SpinningPlugin : InteractionListener, InterfaceListener {
    override fun defineListeners() {

        /*
         * Handles interaction with spinning wheel.
         */

        on(CraftingObject.SPINNING_WHEEL, IntType.SCENERY, "spin") { player, _ ->
            openInterface(player, Components.CRAFTING_SPINNING_459)
            return@on true
        }

        /*
         * Handles creating golden wool.
         */

        onUseWith(IntType.SCENERY, Items.GOLDEN_FLEECE_3693, *CraftingObject.SPINNING_WHEEL) { player, _, _ ->
            if (removeItem(player, Items.GOLDEN_FLEECE_3693)) {
                addItem(player, Items.GOLDEN_WOOL_3694)
                animate(player, Animations.OLD_COOK_RANGE_896)
                sendDialogue(player, "You spin the Golden Fleece into a ball of Golden Wool.")
            }
            return@onUseWith true
        }
    }

    override fun defineInterfaceListeners() {
        on(Components.CRAFTING_SPINNING_459) { player, _, opcode, buttonID, _, _ ->
            val spin = CraftingDefinition.Spinning.forId(buttonID) ?: return@on true
            if (!inInventory(player, spin.need, 1)) {
                sendMessage(player, "You need ${getItemName(spin.need).lowercase()} to make this.")
                return@on true
            }

            var amount = when (opcode) {
                155 -> 1
                196 -> 5
                124 -> player.inventory.getAmount(Item(spin.need))
                199 -> {
                    sendInputDialogue(player, true, "Enter the amount:") { value: Any ->
                        val valAmount = if (value is String) value.toInt() else value as Int
                        handleSpinning(player, spin, valAmount)
                    }
                    return@on true
                }

                else -> 1
            }

            handleSpinning(player, spin, amount)
            return@on true
        }
    }

    private fun handleSpinning(player: Player, spin: CraftingDefinition.Spinning, amount: Int) {
        var remaining = amount
        queueScript(player, 0, QueueStrength.WEAK) { stage ->
            if (remaining <= 0) return@queueScript stopExecuting(player)

            var delay = 5
            if (player.achievementDiaryManager.getDiary(DiaryType.SEERS_VILLAGE)?.isComplete(2) == true
                && withinDistance(player, Location(2711, 3471, 1))
                && player.equipment[EquipmentContainer.SLOT_HAT] != null
                && DiaryManager(player).getHeadband() == 2
            ) {
                delay = 2
            }

            when (stage) {
                0 -> {
                    animate(player, 894)
                    playAudio(player, Sounds.SPINNING_2590)
                    delayScript(player, delay)
                }
                else -> {
                    if (!inInventory(player, spin.need, 1)) {
                        sendMessage(player, "You have run out of ${getItemName(spin.need)}.")
                        return@queueScript stopExecuting(player)
                    }

                    if (removeItem(player, Item(spin.need, 1))) {
                        addItem(player, spin.product, 1)
                        rewardXP(player, Skills.CRAFTING, spin.exp)
                        remaining--
                    }

                    // Seers diary
                    if (player.viewport.region!!.id == 10806 && !hasDiaryTaskComplete(player, DiaryType.SEERS_VILLAGE, 0, 4)) {
                        if (player.getAttribute("diary:seers:bowstrings-spun", 0) >= 4) {
                            setAttribute(player, "/save:diary:seers:bowstrings-spun", 5)
                            finishDiaryTask(player, DiaryType.SEERS_VILLAGE, 0, 4)
                        } else {
                            setAttribute(
                                player,
                                "/save:diary:seers:bowstrings-spun",
                                getAttribute(player, "diary:seers:bowstrings-spun", 0) + 1
                            )
                        }
                    }

                    if (remaining > 0) {
                        setCurrentScriptState(player, 0)
                        delayScript(player, delay)
                    } else stopExecuting(player)
                }
            }
        }
    }

}