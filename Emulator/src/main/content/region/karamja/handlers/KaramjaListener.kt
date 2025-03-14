package content.region.karamja.handlers

import core.api.*
import core.api.interaction.openNpcShop
import core.api.item.produceGroundItem
import core.api.quest.hasRequirement
import core.api.quest.isQuestComplete
import core.game.dialogue.FaceAnim
import core.game.global.action.ClimbActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.system.task.Pulse
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs.consts.*

class KaramjaListener : InteractionListener {
    private fun checkRequirement(player: Player): Boolean {
        return anyInEquipment(player, *MACHETE_ID) || anyInInventory(player, *MACHETE_ID)
    }

    private fun getAnimation(item: Int): Animation {
        return when (item) {
            Items.MACHETE_975 -> Animation.create(Animations.MACHETE_910)
            Items.JADE_MACHETE_6315 -> Animation.create(Animations.JADE_MACHETE_2424)
            Items.OPAL_MACHETE_6313 -> Animation.create(Animations.OPAL_MACHETE_2429)
            Items.RED_TOPAZ_MACHETE_6317 -> Animation.create(Animations.RED_TOPAZ_MACHETE_2426)
            else -> Animation.create(Animations.MACHETE_910)
        }
    }

    override fun defineListeners() {
        on(JUNGLE_BUSH, IntType.SCENERY, "chop-down") { player, node ->
            val randomChop = (1..5).random()
            val chopDown = getAttribute(player, "chop-bush", randomChop)
            if (!checkRequirement(player)) {
                sendMessage(player, "You need a machete to get through this dense jungle.")
                return@on false
            }

            MACHETE_ID.indices.forEach { i ->
                animate(player, getAnimation(i))
                playAudio(player, Sounds.MACHETE_SLASH_1286)
            }

            setAttribute(player, "chop-bush", 0)
            player.pulseManager.run(
                object : Pulse() {
                    var counter = 0

                    override fun pulse(): Boolean {
                        counter++
                        if (counter != randomChop) {
                            animate(player, Animations.SWIPE_WITH_MACHETE_TAI_BWO_WANNAI_CLEANUP_2382)
                        }
                        if (counter == chopDown) {
                            player.walkingQueue.reset()
                            replaceScenery(node.asScenery(), Scenery.SLASHED_BUSH_2895, 20)
                            produceGroundItem(player, Items.LOGS_1511, 1, node.location)
                            rewardXP(player, Skills.WOODCUTTING, 100.0)
                            removeAttribute(player, "chop-bush")
                            player.walkingQueue.addPath(node.location.x, node.location.y, true)
                        }
                        return counter == chopDown
                    }
                },
            )
            return@on true
        }

        on(CUSTOM_OFFICERS, IntType.NPC, "pay-fare") { player, node ->
            if (!isQuestComplete(player, Quests.PIRATES_TREASURE)) {
                openDialogue(player, node.asNpc().id, node)
                sendMessage(player, "You may only use the Pay-fare option after completing Pirate's Treasure.")
                return@on true
            }
            openDialogue(player, node.asNpc().id, node, true)
            return@on true
        }

        on(Scenery.ROCKS_492, IntType.SCENERY, "climb-down") { player, _ ->
            ClimbActionHandler.climb(player, ClimbActionHandler.CLIMB_DOWN, Location.create(2856, 9567, 0))
            sendMessage(player, "You climb down through the pot hole.")
            return@on true
        }

        on(Scenery.CLIMBING_ROPE_1764, IntType.SCENERY, "climb-up") { player, _ ->
            ClimbActionHandler.climb(player, ClimbActionHandler.CLIMB_UP, Location.create(2856, 3167, 0))
            sendMessage(player, "You climb up the hanging rope.")
            sendMessageWithDelay(player, "You appear on the volcano rim.", 1)
            return@on true
        }

        on(PINEAPPLE_PLANT, IntType.SCENERY, "pick") { player, node ->
            if (!hasSpaceFor(player, Item(Items.PINEAPPLE_2114))) {
                sendMessage(player, "You don't have enough space in your inventory.")
                return@on true
            }
            if (node.id == Scenery.PINEAPPLE_PLANT_1413) {
                sendMessage(player, "There are no pineapples left on this plant.")
                return@on true
            }
            val last: Boolean = node.id == Scenery.PINEAPPLE_PLANT_1412
            if (addItem(player, Items.PINEAPPLE_2114)) {
                animate(player, Animations.PICK_SOMETHING_UP_FROM_GROUND_2282)
                playAudio(player, Sounds.PICK_2581, 30)
                replaceScenery(node.asScenery(), node.id + 1, if (last) 270 else 40)
                sendMessage(player, "You pick a pineapple.")
            }
            return@on true
        }

        on(Scenery.BANANA_TREE_2078, IntType.SCENERY, "search") { player, _ ->
            sendMessage(player, "There are no bananas left on the tree.")
            return@on true
        }

        on(Scenery.LEAFY_PALM_TREE_2975, IntType.SCENERY, "Shake") { player, node ->
            queueScript(player, 0, QueueStrength.WEAK) { stage: Int ->
                when (stage) {
                    0 -> {
                        lock(player, 2)
                        face(player, node)
                        animate(player, Animations.PUSH_2572)
                        sendMessage(player, "You give the tree a good shake.")
                        replaceScenery(node.asScenery(), Scenery.LEAFY_PALM_TREE_2976, 60)
                        return@queueScript delayScript(player, 2)
                    }

                    1 -> {
                        produceGroundItem(
                            player,
                            Items.PALM_LEAF_2339,
                            1,
                            getPathableRandomLocalCoordinate(player, 1, node.location),
                        )
                        sendMessage(player, "A palm leaf falls to the ground.")
                        return@queueScript stopExecuting(player)
                    }

                    else -> return@queueScript stopExecuting(player)
                }
            }
            return@on true
        }

        onUseWith(IntType.NPC, Items.PURE_ESSENCE_7937, NPCs.JIMINUA_560) { player, used, _ ->
            assert(used.id == Items.PURE_ESSENCE_7937)
            val ess: Int = amountInInventory(player, Items.PURE_ESSENCE_7937)
            val coins: Int = amountInInventory(player, Items.COINS_995)
            val freeSpace = freeSlots(player)

            when {
                ess == 0 -> {
                    sendNPCDialogue(
                        player,
                        NPCs.JIMINUA_560,
                        "You don't have any essence for me to un-note.",
                        FaceAnim.HALF_GUILTY,
                    )
                    return@onUseWith false
                }

                freeSpace == 0 -> {
                    sendNPCDialogue(player, NPCs.JIMINUA_560, "You don't have any free space.", FaceAnim.HALF_GUILTY)
                    return@onUseWith false
                }

                coins <= 1 -> {
                    sendNPCDialogue(
                        player,
                        NPCs.JIMINUA_560,
                        "I charge 2 gold coins to un-note each pure essence.",
                        FaceAnim.HALF_GUILTY,
                    )
                    return@onUseWith false
                }

                else -> {
                    val unnote = minOf(freeSpace.toDouble(), ess.toDouble(), (coins / 2).toDouble()).toInt()
                    removeItem(player, Item(Items.PURE_ESSENCE_7937, unnote))
                    removeItem(player, (Item(Items.COINS_995, 2 * unnote)))
                    addItem(player, Items.PURE_ESSENCE_7936, unnote)
                    sendMessage(player, "You hand Jiminua some notes and coins, and she hands you back pure essence.")
                }
            }
            return@onUseWith true
        }

        on(NPCs.TIADECHE_1164, IntType.NPC, "trade") { player, _ ->
            if (!hasRequirement(player, "Tai Bwo Wannai Trio")) return@on true
            openNpcShop(player, NPCs.TIADECHE_1164)
            return@on true
        }
    }

    companion object {
        private val PINEAPPLE_PLANT =
            intArrayOf(
                Scenery.PINEAPPLE_PLANT_1408,
                Scenery.PINEAPPLE_PLANT_1409,
                Scenery.PINEAPPLE_PLANT_1410,
                Scenery.PINEAPPLE_PLANT_1411,
                Scenery.PINEAPPLE_PLANT_1412,
                Scenery.PINEAPPLE_PLANT_1413,
            )
        private val CUSTOM_OFFICERS =
            intArrayOf(
                NPCs.CUSTOMS_OFFICER_380,
                NPCs.CUSTOMS_OFFICER_381,
            )
        private val MACHETE_ID =
            intArrayOf(
                Items.MACHETE_975,
                Items.JADE_MACHETE_6315,
                Items.OPAL_MACHETE_6313,
                Items.RED_TOPAZ_MACHETE_6317,
            )
        private val JUNGLE_BUSH =
            intArrayOf(
                Scenery.JUNGLE_BUSH_2892,
                Scenery.JUNGLE_BUSH_2893,
            )
    }
}
