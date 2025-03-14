package content.global.skill.summoning.familiar.npc

import content.global.skill.summoning.familiar.BurdenBeast
import content.global.skill.summoning.familiar.Familiar
import content.global.skill.summoning.familiar.FamiliarSpecial
import content.global.skill.summoning.familiar.Forager
import core.api.*
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.node.item.WeightedChanceItem
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.GameWorld.Pulser
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.plugin.Initializable
import core.tools.RandomFunction
import org.rs.consts.Items
import org.rs.consts.NPCs

@Initializable
class FruitBatFamiliar(
    owner: Player? = null,
    id: Int = FRUIT_BAT,
) : Forager(owner, id, 4500, Items.FRUIT_BAT_POUCH_12033, 6, *FRUIT_FORAGE),
    InteractionListener {
    override fun construct(
        owner: Player,
        id: Int,
    ): Familiar {
        return FruitBatFamiliar(owner, id)
    }

    override fun isHidden(player: Player?): Boolean {
        return super.isHidden(player)
    }

    override fun getIds(): IntArray {
        return intArrayOf(FRUIT_BAT)
    }

    override fun specialMove(special: FamiliarSpecial?): Boolean {
        if (owner.getAttribute("fruit-bat", 0) > GameWorld.ticks) {
            return false
        }

        animate(FLY_UP_ANIMATION)
        graphics(FRUIT_FALL_GFX)
        animate(FLY_DOWN_ANIMATION, 3)

        owner.setAttribute("fruit-bat", GameWorld.ticks + 5)
        lock(4)

        Pulser.submit(
            object : Pulse(4, this) {
                override fun pulse(): Boolean {
                    if (anyFruit) {
                        val coords =
                            mutableListOf(
                                Pair(-1, -1),
                                Pair(-1, 0),
                                Pair(-1, 1),
                                Pair(0, -1),
                                Pair(0, 1),
                                Pair(1, -1),
                                Pair(1, 0),
                                Pair(1, 1),
                            ).shuffled().toMutableList()

                        if (coords.isNotEmpty()) {
                            val firstCoord = coords.removeAt(0)
                            GroundItemManager.create(
                                Item(Items.PAPAYA_FRUIT_5972),
                                owner.location.transform(firstCoord.first, firstCoord.second, 0),
                                owner,
                            )
                            sendGraphics(
                                FRUIT_SPLASH_GFX,
                                owner.location.transform(firstCoord.first, firstCoord.second, 0),
                            )
                        }

                        if (coords.isNotEmpty()) {
                            val secondCoord = coords.removeAt(0)
                            GroundItemManager.create(
                                Item(Items.PAPAYA_FRUIT_5972),
                                owner.location.transform(secondCoord.first, secondCoord.second, 0),
                                owner,
                            )
                            sendGraphics(
                                FRUIT_SPLASH_GFX,
                                owner.location.transform(secondCoord.first, secondCoord.second, 0),
                            )
                        }

                        repeat(otherFruitAmount) {
                            if (coords.isNotEmpty()) {
                                val coord = coords.removeAt(0)
                                val item = RandomFunction.rollWeightedChanceTable(*FRUIT_FALL)
                                if (item.id != 0) {
                                    GroundItemManager.create(
                                        item,
                                        owner.location.transform(coord.first, coord.second, 0),
                                        owner,
                                    )
                                    sendGraphics(
                                        FRUIT_SPLASH_GFX,
                                        owner.location.transform(coord.first, coord.second, 0),
                                    )
                                }
                            }
                        }
                    }
                    return true
                }
            },
        )

        return true
    }

    override fun defineListeners() {
        on(FRUIT_BAT, IntType.NPC, "Interact") { player, node ->
            val familiar = node as? BurdenBeast ?: return@on false

            sendDialogueOptions(player, "Select an Option", "Chat", "Fly", "Withdraw")
            addDialogueAction(player) { _, button ->
                when (button) {
                    2 -> openDialogue(player, node.id, node.asNpc())
                    3 -> {
                        if (inZone(player, "Dark zone")) {
                            sendMessage(player, "You cannot use this ability here.")
                            return@addDialogueAction
                        }
                        if (!inZone(player, "karamja")) {
                            sendMessage(
                                player,
                                "You need to be in Karamja, close to the fruit-bearing jungle trees, for this to work.",
                            )
                            return@addDialogueAction
                        }

                        sendMessage(player, "The fruit bat flies away to look for fruit...")

                        familiar.animate(FLY_UP_ANIMATION)
                        familiar.animate(FLY_DOWN_ANIMATION, 10)

                        lock(13)

                        runTask(player, 3) {
                            familiar.isInvisible = true
                            runTask(player, 7) {
                                familiar.isInvisible = false
                            }
                        }

                        queueScript(player, 12, QueueStrength.SOFT) {
                            val item: Item = RandomFunction.rollWeightedChanceTable(*FRUIT_FLY)
                            familiar.container.add(item, true)
                            sendMessage(player, "Your fruit bat has returned with fruit!")
                            return@queueScript stopExecuting(player)
                        }
                    }

                    4 -> {
                        if (!familiar.isBurdenBeast) {
                            sendMessage(player, "This is not a beast of burden.")
                            return@addDialogueAction
                        }
                        familiar.openInterface()
                    }
                }
            }
            return@on true
        }
    }

    companion object {
        private val FLY_UP_ANIMATION: Animation = Animation(8320)
        private val FLY_DOWN_ANIMATION: Animation = Animation(8321)
        private val FRUIT_FALL_GFX: Graphics = Graphics(1332, 200)
        private val FRUIT_SPLASH_GFX: Graphics = Graphics(1331)
        private val FRUIT_BAT = NPCs.FRUIT_BAT_6817

        private val FRUIT_FORAGE =
            arrayOf(
                Item(Items.PAPAYA_FRUIT_5972),
                Item(Items.ORANGE_2108),
                Item(Items.PINEAPPLE_2114),
                Item(Items.LEMON_2102),
                Item(Items.LIME_2120),
                Item(Items.STRAWBERRY_5504),
                Item(Items.WATERMELON_5982),
                Item(Items.COCONUT_5974),
            )

        private val FRUIT_FALL =
            arrayOf(
                WeightedChanceItem(Items.ORANGE_2108, 1, 4),
                WeightedChanceItem(Items.PINEAPPLE_2114, 1, 3),
                WeightedChanceItem(Items.LEMON_2102, 1, 2),
                WeightedChanceItem(Items.LIME_2120, 1, 2),
                WeightedChanceItem(Items.BANANA_1963, 1, 2),
                WeightedChanceItem(0, 1, 4),
            )
        private val FRUIT_FLY =
            arrayOf(
                WeightedChanceItem(Items.ORANGE_2108, 1, 4),
                WeightedChanceItem(Items.PINEAPPLE_2114, 1, 3),
                WeightedChanceItem(Items.LEMON_2102, 1, 2),
                WeightedChanceItem(Items.LIME_2120, 1, 2),
                WeightedChanceItem(Items.BANANA_1963, 1, 2),
            )

        private val anyFruit = RandomFunction.random(10) <= 8
        private val goodFruit = RandomFunction.random(100) <= 2
        private val otherFruitAmount =
            if (!goodFruit && RandomFunction.random(10) == 1) {
                RandomFunction.random(0, 1)
            } else {
                RandomFunction.random(
                    0,
                    if (goodFruit) 7 else 3,
                )
            }
    }
}
