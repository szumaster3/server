package content.region.karamja.plugin

import content.data.items.SkillingTool
import content.global.skill.gathering.SkillReward
import content.global.skill.gathering.woodcutting.WoodcuttingNode
import core.api.*
import core.game.dialogue.FaceAnim
import core.game.global.action.ClimbActionHandler
import core.game.interaction.Clocks
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.map.Location
import shared.consts.*

class KaramjaPlugin : InteractionListener {

    companion object {
        private val PINEAPPLE_PLANT = intArrayOf(Scenery.PINEAPPLE_PLANT_1408, Scenery.PINEAPPLE_PLANT_1409, Scenery.PINEAPPLE_PLANT_1410, Scenery.PINEAPPLE_PLANT_1411, Scenery.PINEAPPLE_PLANT_1412, Scenery.PINEAPPLE_PLANT_1413)
        private val CUSTOM_OFFICERS = intArrayOf(NPCs.CUSTOMS_OFFICER_380, NPCs.CUSTOMS_OFFICER_381)
        private val MACHETE_ID = intArrayOf(Items.MACHETE_975, Items.JADE_MACHETE_6315, Items.OPAL_MACHETE_6313, Items.RED_TOPAZ_MACHETE_6317)
        private val JUNGLE_BUSH = intArrayOf(Scenery.JUNGLE_BUSH_2892, Scenery.JUNGLE_BUSH_2893)
    }

    override fun defineListeners() {

        /*
         * Handles chopping dense jungle bushes with a machete to clear a path.
         */

        defineInteraction(
            IntType.SCENERY,
            JUNGLE_BUSH,
            "chop-down",
            persistent = true,
            allowedDistance = 1,
            handler = ::handleChopBush
        )

        /*
         * Handles Customs Officer "Pay-fare" dialogue interaction
         *  after completing Pirate's Treasure quest.
         */

        on(CUSTOM_OFFICERS, IntType.NPC, "pay-fare") { player, node ->
            if (!isQuestComplete(player, Quests.PIRATES_TREASURE)) {
                openDialogue(player, node.asNpc().id, node)
                sendMessage(player, "You may only use the Pay-fare option after completing Pirate's Treasure.")
                return@on true
            }
            openDialogue(player, node.asNpc().id, node, true)
            return@on true
        }

        /*
         * Handles climbing down through the volcano pothole entrance.
         */

        on(Scenery.ROCKS_492, IntType.SCENERY, "climb-down") { player, _ ->
            ClimbActionHandler.climb(player, null, Location.create(2856, 9567, 0))
            sendMessage(player, "You climb down through the pot hole.")
            return@on true
        }

        /*
         * Handles climbing up the hanging rope out of the Karamja volcano.
         */

        on(Scenery.CLIMBING_ROPE_1764, IntType.SCENERY, "climb-up") { player, _ ->
            ClimbActionHandler.climb(player, ClimbActionHandler.CLIMB_UP, Location.create(2856, 3167, 0))
            sendMessage(player, "You climb up the hanging rope.")
            sendMessage(player, "You appear on the volcano rim.", 1)
            return@on true
        }

        /*
         * Handles picking pineapples from Karamja pineapple plants.
         */

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

        /*
         * Handles searching banana trees that have no bananas left.
         */

        on(Scenery.BANANA_TREE_2078, IntType.SCENERY, "search") { player, _ ->
            sendMessage(player, "There are no bananas left on the tree.")
            return@on true
        }

        /*
         * Handles shaking leafy palm trees to drop palm leaves onto the ground.
         */

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
                        produceGroundItem(player, Items.PALM_LEAF_2339, 1, getPathableRandomLocalCoordinate(player, 1, node.location))
                        sendMessage(player, "A palm leaf falls to the ground.")
                        return@queueScript stopExecuting(player)
                    }

                    else -> return@queueScript stopExecuting(player)
                }
            }
            return@on true
        }

        /*
         * Handles using pure essence notes on Jiminua to
         * un-note them for a 2-coin fee per essence.
         */

        onUseWith(IntType.NPC, Items.PURE_ESSENCE_7937, NPCs.JIMINUA_560) { player, used, _ ->
            assert(used.id == Items.PURE_ESSENCE_7937)
            val ess: Int = amountInInventory(player, Items.PURE_ESSENCE_7937)
            val coins: Int = amountInInventory(player, Items.COINS_995)
            val freeSpace = freeSlots(player)

            when {
                ess == 0 -> {
                    sendNPCDialogue(player, NPCs.JIMINUA_560, "You don't have any essence for me to un-note.", FaceAnim.HALF_GUILTY)
                    return@onUseWith false
                }

                freeSpace == 0 -> {
                    sendNPCDialogue(player, NPCs.JIMINUA_560, "You don't have any free space.", FaceAnim.HALF_GUILTY)
                    return@onUseWith false
                }

                coins <= 1 -> {
                    sendNPCDialogue(player, NPCs.JIMINUA_560, "I charge 2 gold coins to un-note each pure essence.", FaceAnim.HALF_GUILTY)
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

        /*
         * Handles Tiadeche NPC trade interaction
         * (available after Tai Bwo Wannai Trio quest).
         */

        on(NPCs.TIADECHE_1164, IntType.NPC, "trade") { player, _ ->
            if (!hasRequirement(player, Quests.TAI_BWO_WANNAI_TRIO)) return@on true
            openNpcShop(player, NPCs.TIADECHE_1164)
            return@on true
        }
    }

    /**
     * Handles chopping jungle bushes using a machete.
     * @param player The player.
     * @param bush The node id.
     * @param state The stage.
     */
    private fun handleChopBush(player: Player, bush: Node, state: Int): Boolean {
        val machete = SkillingTool.getMachete(player)

        if (machete == null) {
            sendMessage(player, "You need a machete to cut your way through this dense jungle bush.")
            return true
        }

        if (!finishedMoving(player) || !clockReady(player, Clocks.SKILLING)) {
            clearScripts(player)
            return true
        }

        if (state == 0) {
            sendMessage(player, "You swing your machete at the jungle plant.")
            animate(player, machete.animation)
            playAudio(player, Sounds.MACHETE_SLASH_1286)
            return delayScript(player, 2)
        }

        animate(player, machete.animation)

        val jungleBushNode = when (bush.id) {
            WoodcuttingNode.JUNGLE_BUSH_1.id -> WoodcuttingNode.JUNGLE_BUSH_1
            WoodcuttingNode.JUNGLE_BUSH_2.id -> WoodcuttingNode.JUNGLE_BUSH_2
            else -> null
        }

        if (jungleBushNode == null || !SkillReward.checkWoodcuttingReward(player, jungleBushNode, machete)) {
            delayClock(player, Clocks.SKILLING, 3)
            return delayScript(player, 2)
        }

        replaceScenery(bush.asScenery(), Scenery.SLASHED_BUSH_2895, 20)
        produceGroundItem(player, Items.LOGS_1511, 1, bush.location)
        rewardXP(player, Skills.WOODCUTTING, 100.0)
        resetAnimator(player)

        val target = if (player.location.y > bush.location.y)
            bush.location.transform(0, -1, 0)
        else
            bush.location.transform(0, 1, 0)

        forceMove(player, player.location, target, 0, 90, null, Animations.HUMAN_WALK_SHORT_819) {
            sendMessage(player, "You hack your way through the jungle bush.")
            delayClock(player, Clocks.SKILLING, 3)
            clearScripts(player)
        }

        return delayScript(player, 2)
    }
}
