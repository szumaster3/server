package content.global.skill.crafting

import core.api.*
import core.game.container.impl.EquipmentContainer
import core.game.interaction.*
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryManager
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.world.map.Location
import shared.consts.*

class SpinningPlugin : InteractionListener, InterfaceListener {

    override fun defineListeners() {

        /*
         * Handles interaction with spinning wheel.
         */
        on(CraftingDefinition.SPINNING_WHEEL, IntType.SCENERY, "spin") { player, obj ->
            setAttribute(player, "current_spinning_wheel", obj)
            openInterface(player, Components.CRAFTING_SPINNING_459)
            return@on true
        }

        /*
         * Handles creating golden wool.
         */
        onUseWith(IntType.SCENERY, Items.GOLDEN_FLEECE_3693, *CraftingDefinition.SPINNING_WHEEL) { player, _, with ->
            if (removeItem(player, Items.GOLDEN_FLEECE_3693)) {
                lock(player, 3)
                playAudio(player, Sounds.SPINNING_2590)
                animate(player, 894)
                animateScenery(with.asScenery(), 466)
                addItem(player, Items.GOLDEN_WOOL_3694)
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

            val wheel = getAttribute<Scenery>(player, "current_spinning_wheel", Scenery(0,Location(0,0,0))) ?: run {
                return@on true
            }

            val amount = when (opcode) {
                155 -> 1
                196 -> 5
                124 -> amountInInventory(player, spin.need)
                199 -> {
                    sendInputDialogue(player, true, "Enter the amount:") { value: Any ->
                        val valAmount = if (value is String) value.toInt() else value as Int
                        closeInterface(player)
                        handleSpinning(player, spin, wheel, valAmount)
                    }
                    return@on true
                }
                else -> 1
            }

            closeInterface(player)
            handleSpinning(player, spin, wheel, amount)
            return@on true
        }
    }

    private fun handleSpinning(player: Player, spin: CraftingDefinition.Spinning, wheel: core.game.node.scenery.Scenery, amount: Int) {
        if (!clockReady(player, Clocks.SKILLING)) return

        var remaining = amount

        queueScript(player, 0, QueueStrength.WEAK) {
            if (remaining <= 0) return@queueScript false

            var delay = 5
            if (player.achievementDiaryManager.getDiary(DiaryType.SEERS_VILLAGE)?.isComplete(2) == true
                && withinDistance(player, Location(2711, 3471, 1))
                && player.equipment[EquipmentContainer.SLOT_HAT] != null
                && DiaryManager(player).headband == 2
            ) {
                delay = 2
            }

            if (!inInventory(player, spin.need, 1)) {
                sendMessage(player, "You have run out of ${getItemName(spin.need)}.")
                return@queueScript false
            }

            playAudio(player, Sounds.SPINNING_2590)
            animate(player, 894)
            animateScenery(wheel, 466)
            delayClock(player, Clocks.SKILLING, delay)

            if (removeItem(player, Item(spin.need, 1))) {
                addItem(player, spin.product, 1)
                rewardXP(player, Skills.CRAFTING, spin.exp)
                remaining--
            }

            // Seers diary.
            val spun = getAttribute<Int>(player, "diary:seers:bowstrings-spun", 0)
            if (player.viewport.region!!.id == 10806 && !hasDiaryTaskComplete(player, DiaryType.SEERS_VILLAGE, 0, 4)) {
                if (spun >= 4) {
                    setAttribute(player, "/save:diary:seers:bowstrings-spun", 5)
                    finishDiaryTask(player, DiaryType.SEERS_VILLAGE, 0, 4)
                } else {
                    setAttribute(player, "/save:diary:seers:bowstrings-spun", spun + 1)
                }
            }

            if (remaining > 0) {
                delayClock(player, Clocks.SKILLING, delay)
                setCurrentScriptState(player, 0)
                delayScript(player, delay)
            } else false
        }
    }
}
