package content.global.skill.smithing.smelting

import core.api.*
import core.api.quest.isQuestComplete
import core.game.container.impl.EquipmentContainer
import core.game.event.ResourceProducedEvent
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.skill.SkillPulse
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.update.flag.context.Graphics
import core.tools.RandomFunction
import core.tools.StringUtils
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Quests
import org.rs.consts.Sounds

class SmeltingPulse : SkillPulse<Item?> {
    private val bar: Bar?
    private val superHeat: Boolean
    private var ticks = 0
    private var amount: Int

    constructor(player: Player?, node: Item?, bar: Bar?, amount: Int) : super(player, node) {
        this.bar = bar
        this.amount = amount
        this.superHeat = false
    }

    constructor(player: Player?, node: Item?, bar: Bar?, amount: Int, heat: Boolean) : super(player, node) {
        this.bar = bar
        this.amount = amount
        this.superHeat = heat
        this.resetAnimation = false
    }

    override fun checkRequirements(): Boolean {
        closeChatBox(player)
        if (bar == null || player == null) {
            return false
        }
        if (bar == Bar.BLURITE && !isQuestComplete(player, Quests.THE_KNIGHTS_SWORD)) {
            return false
        }
        if (getStatLevel(player, Skills.SMITHING) < bar.level) {
            sendMessage(
                player,
                "You need a Smithing level of at least " + bar.level + " in order to smelt " +
                    bar.product.name
                        .lowercase()
                        .replace("bar", "") +
                    ".",
            )
            closeChatBox(player)
            return false
        }
        for (item in bar.ores) {
            if (!inInventory(player, item.id, item.amount)) {
                when (bar.product.id) {
                    Items.BRONZE_BAR_2349 -> {
                        if (amountInInventory(player, Items.TIN_ORE_438) <
                            amountInInventory(player, Items.COPPER_ORE_436)
                        ) {
                            sendMessage(player, "You don't have enough copper to make any more bronze.")
                        } else if (amountInInventory(player, Items.TIN_ORE_438) >
                            amountInInventory(player, Items.COPPER_ORE_436)
                        ) {
                            sendMessage(player, "You don't have enough tin to make any more bronze.")
                        } else {
                            sendMessage(player, "You smelt the copper and tin together in the furnace.")
                        }
                    }

                    Items.IRON_BAR_2351 -> {
                        if (amountInInventory(player, Items.COAL_453) < 1) {
                            sendMessage(player, "you have run out of iron ore to smelt.")
                        } else {
                            sendMessage(player, "You place the iron into the furnace.")
                        }
                    }

                    Items.STEEL_BAR_2353 -> {
                        if (amountInInventory(player, Items.COAL_453) < 2) {
                            sendMessage(player, "You don't have enough coal to make any more steel.")
                        } else {
                            sendMessage(player, "You place the steel and two heaps of coal into the furnace.")
                        }
                    }

                    Items.SILVER_BAR_2355 -> {
                        if (amountInInventory(player, Items.SILVER_ORE_442) < 1) {
                            sendMessage(player, "You have run out of silver to smelt.")
                        } else {
                            sendMessage(player, "You place the silver into the furnace.")
                        }
                    }

                    Items.MITHRIL_BAR_2359 -> {
                        if (amountInInventory(player, Items.COAL_453) < 4) {
                            sendMessage(player, "You don't have enough coal to make any more mithril.")
                        } else {
                            sendMessage(player, "You place the mithril and four heaps of coal into the furnace.")
                        }
                    }

                    Items.ADAMANTITE_BAR_2361 ->
                        if (amountInInventory(player, Items.COAL_453) < 6) {
                            sendMessage(player, "You don't have enough coal to make any more adamantite.")
                        } else {
                            sendMessage(player, "You place the adamantite and six heaps of coal into the furnace.")
                        }

                    Items.RUNITE_BAR_2363 ->
                        if (amountInInventory(player, Items.COAL_453) < 8) {
                            sendMessage(player, "You don't have enough coal to make any more runite.")
                        } else {
                            sendMessage(player, "You place the runite and eight heaps of coal into the furnace.")
                        }

                    else ->
                        sendMessage(
                            player,
                            "You have run out of ${getItemName(bar.ores.size).lowercase()} to smelt.",
                        )
                }
                return false
            }
        }
        return true
    }

    override fun animate() {
        if (ticks == 0 || ticks % 5 == 0) {
            if (superHeat) {
                visualize(
                    player,
                    Animations.HUMAN_CAST_SUPERHEAT_SPELL_725,
                    Graphics(org.rs.consts.Graphics.SUPERHEAT_ITEM_148, 96),
                )
            } else {
                animate(player, Animations.HUMAN_FURNACE_SMELT_3243)
                playAudio(player, Sounds.FURNACE_2725)
            }
        }
    }

    override fun reward(): Boolean {
        if (!superHeat && ++ticks % 5 != 0) {
            return false
        }
        if (!superHeat) {
            sendMessage(
                player,
                "You place a lump of " + StringUtils.formatDisplayName(bar.toString()).lowercase() + " in the furnace.",
            )
        }
        for (i in bar!!.ores) {
            if (!removeItem(player, i)) {
                return true
            }
        }
        if (success(player)) {
            var amt =
                if ((
                        (
                            freeSlots(player) != 0 &&
                                !superHeat &&
                                withinDistance(player, Location(3107, 3500, 0)) &&
                                player.inventory.containsItems(*bar.ores)
                        ) &&
                            player.achievementDiaryManager.getDiary(DiaryType.VARROCK)!!.level != -1 &&
                            player.achievementDiaryManager.checkSmithReward(bar) &&
                            RandomFunction.random(100) <= 10
                    )
                ) {
                    2
                } else {
                    1
                }
            if (amt != 1) {
                if (!removeItem(player, bar.ores)) {
                    amt = 1
                } else {
                    sendMessage(player, "The magic of the Varrock armour enables you to smelt 2 bars at the same time.")
                }
            }
            addItem(player, bar.product.id, amt)
            player.dispatch(ResourceProducedEvent(bar.product.id, 1, player, -1))
            var xp = bar.experience * amt

            if ((
                    (
                        player.equipment[EquipmentContainer.SLOT_HANDS] != null &&
                            player.equipment[EquipmentContainer.SLOT_HANDS].id == Items.GOLDSMITH_GAUNTLETS_776
                    )
                ) &&
                bar.product.id == Items.GOLD_BAR_2357
            ) {
                xp = 56.2 * amt
            }
            rewardXP(player, Skills.SMITHING, xp)
            if (!superHeat) {
                sendMessage(
                    player,
                    "You retrieve a bar of " +
                        bar.product.name
                            .lowercase()
                            .replace(" bar", "") + ".",
                )
            }
        } else {
            sendMessage(player, "The ore is too impure and you fail to refine it.")
        }
        amount--
        return amount < 1
    }

    private fun hasForgingRing(player: Player): Boolean {
        return inEquipment(player, RING_OF_FORGING)
    }

    fun success(player: Player): Boolean {
        if (bar == Bar.IRON && !superHeat) {
            return if (hasForgingRing(player)) {
                val ring = getItemFromEquipment(player, EquipmentSlot.RING)
                if (ring != null) {
                    if (getCharge(ring) == 1000) setCharge(ring, 140)
                    adjustCharge(ring, -1)
                    if (getCharge(ring) == 0) {
                        removeItem(player, ring)
                        sendMessage(player, "Your ring of forging uses up its last charge and disintegrates.")
                    }
                }
                true
            } else {
                RandomFunction.getRandom(100) <= (if (getStatLevel(player, Skills.SMITHING) >= 45) 80 else 50)
            }
        }
        return true
    }

    companion object {
        private const val RING_OF_FORGING = Items.RING_OF_FORGING_2568
    }
}
