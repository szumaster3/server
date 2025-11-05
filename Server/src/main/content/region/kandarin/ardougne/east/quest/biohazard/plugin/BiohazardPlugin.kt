package content.region.kandarin.ardougne.east.quest.biohazard.plugin

import content.data.GameAttributes
import content.global.skill.agility.AgilityHandler
import content.region.kandarin.ardougne.east.quest.biohazard.dialogue.GuidorDialogue
import core.api.*
import core.game.dialogue.DialogueFile
import core.game.dialogue.FaceAnim
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.interaction.QueueStrength
import core.game.node.entity.impl.Projectile
import core.game.node.item.Item
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import shared.consts.*

class BiohazardPlugin : InteractionListener {

    private val FENCE_CORNER_LOCATION = Location(2563, 3301, 0)
    private val WATCHTOWER_CORNER_LOCATION = Location(2561, 3303, 0)

    override fun defineListeners() {
        on(NPCs.GUIDOR_343, IntType.NPC, "talk-to") { player, _ ->
            openDialogue(player, GuidorDialogue())
            return@on true
        }

        on(Scenery.WATCHTOWER_FENCE_2067, IntType.SCENERY, "investigate") { player, _ ->
            if (removeItem(player, Items.BIRD_FEED_422)) {
                sendMessage(player, "You throw a handful of seeds onto the watchtower.")
                sendMessage(player, "The mourners do not seem to notice.", 1)
                setAttribute(player, GameAttributes.FEED_ON_FENCE, true)
            }
            return@on true
        }

        onUseWith(IntType.SCENERY, Items.BIRD_FEED_422, Scenery.WATCHTOWER_FENCE_2067) { player, used, _ ->
            if (getAttribute(player, GameAttributes.FEED_ON_FENCE, true)) {
                return@onUseWith true
            }
            if (removeItem(player, used.asItem())) {
                sendMessage(player, "You throw a handful of seeds onto the watchtower.")
                sendMessage(player, "The mourners do not seem to notice.", 1)
                setAttribute(player, GameAttributes.FEED_ON_FENCE, true)
            }
            return@onUseWith true
        }

        on(Items.PIGEON_CAGE_424, IntType.ITEM, "open") { player, node ->
            val hasFenceFeed = getAttribute(player, GameAttributes.FEED_ON_FENCE, false)
            val item = node.asItem()

            sendMessage(player, "You open the cage.")

            if (!hasFenceFeed) {
                sendMessage(player, "The pigeons don't want to leave.", 1)
                return@on true
            }

            if (player.location != FENCE_CORNER_LOCATION) {
                return@on true
            }

            if (!inInventory(player, node.id)) {
                return@on true
            }

            val slot = player.inventory.getSlot(item)
            replaceSlot(player, slot, Item(Items.PIGEON_CAGE_425))

            face(player, WATCHTOWER_CORNER_LOCATION)
            spawnProjectile(
                Projectile.getLocation(player),
                Location.create(2561, 3303, 0),
                72, 40, 200, 0, 250, 25
            )

            sendMessage(player, "The pigeons fly towards the watch tower.", 1)
            sendMessage(player, "The mourners are frantically trying to scare the pigeons away.", 2)

            setQuestStage(player, Quests.BIOHAZARD, 4)
            return@on true
        }

        on(Scenery.CUPBOARD_2056, IntType.SCENERY, "open") { player, node ->
            animate(player, Animations.HUMAN_OPEN_WARDROBE_542)
            playAudio(player, Sounds.CUPBOARD_OPEN_58)
            replaceScenery(node.asScenery(), Scenery.CUPBOARD_2057, -1)
            sendMessage(player, "You open the cupboard.")
            return@on true
        }

        on(Items.PIGEON_CAGE_425, IntType.ITEM, "open") { player, _ ->
            sendMessage(player, "You open the cage.")
            sendMessage(player, "It's empty.", 1)
            return@on true
        }

        on(Scenery.CUPBOARD_2057, IntType.SCENERY, "close") { player, node ->
            animate(player, Animations.CLOSE_CUPBOARD_543)
            playAudio(player, Sounds.CUPBOARD_CLOSE_57)
            replaceScenery(node.asScenery(), Scenery.CUPBOARD_2056, -1)
            return@on true
        }

        on(Scenery.CUPBOARD_2057, IntType.SCENERY, "search") { player, node ->
            if (inInventory(player, Items.BIRD_FEED_422)) {
                sendMessage(player, "You search the cupboard but find nothing interesting.")
                return@on true
            } else {
                openDialogue(
                    player,
                    object : DialogueFile() {
                        override fun handle(componentID: Int, buttonID: Int) {
                            when (stage) {
                                0 -> sendItemDialogue(player, Items.BIRD_FEED_422, "The cupboard is full of birdfeed.").also { stage++ }
                                1 -> player("Mmm, birdfeed! Now what could I do with that?").also { stage++ }
                                2 -> {
                                    end()
                                    addItemOrDrop(player, Items.BIRD_FEED_422)
                                    setQuestStage(player, Quests.BIOHAZARD, 3)
                                }
                            }
                        }
                    },
                    node,
                )
                return@on true
            }
        }

        on(Scenery.FENCE_2068, IntType.SCENERY, "squeeze-through") { player, _ ->
            queueScript(player, 1, QueueStrength.SOFT) {
                val newDirection =
                    when {
                        player.location.x < 2542 -> Direction.EAST
                        player.location.x >= 2542 -> Direction.WEST
                        else -> Direction.EAST
                    }
                AgilityHandler.forceWalk(
                    player,
                    -1,
                    player.location,
                    player.location.transform(newDirection, 1),
                    Animation(Animations.SIDE_STEP_TO_CRAWL_THROUGH_MCGRUBOR_S_WOODS_FENCE_3844),
                    5,
                    0.0,
                    "You squeeze through the loose railing.",
                )
                return@queueScript stopExecuting(player)
            }
            return@on true
        }

        onUseWith(IntType.SCENERY, Items.ROTTEN_APPLE_1984, Scenery.CAULDRON_2043) { player, used, _ ->
            if (removeItem(player, used.asItem())) {
                sendMessage(player, "You place the rotten apple in the pot...")
                setQuestStage(player, Quests.BIOHAZARD, 6)
            }
            return@onUseWith true
        }

        on(Scenery.DOOR_2036, IntType.SCENERY, "open") { player, node ->
            if (inBorders(player, getRegionBorders(10035)) || inBorders(player, getRegionBorders(10036))) {
                if (isQuestComplete(player, Quests.BIOHAZARD) ||
                    getQuestStage(player, Quests.BIOHAZARD) >= 7 ||
                    player.location.y in 3321..3327
                ) {
                    DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
                    return@on true
                }

                if (getQuestStage(player, Quests.BIOHAZARD) in 1..6) {
                    sendMessage(player, "The door is locked. You can hear the mourners eating...")
                    sendMessage(player, "You need to distract them from their stew.", 1)
                    return@on true
                }

                if (!inEquipment(player, Items.DOCTORS_GOWN_430) || getQuestStage(player, Quests.BIOHAZARD) == 0) {
                    openDialogue(player, MournerDialogue(), NPCs.MOURNER_347)
                    return@on true
                }
                sendNPCDialogue(player, NPCs.MOURNER_347, "In you go dot.")
                setQuestStage(player, Quests.BIOHAZARD, 7)
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else {
                DoorActionHandler.handleDoor(player, node.asScenery())
            }
            return@on true
        }

        on(Scenery.BOX_2062, IntType.SCENERY, "open") { _, node ->
            replaceScenery(node.asScenery(), Scenery.BOX_2063, -1)
            return@on true
        }

        on(Scenery.BOX_2063, IntType.SCENERY, "close") { _, node ->
            replaceScenery(node.asScenery(), Scenery.BOX_2062, -1)
            return@on true
        }

        on(Scenery.BOX_2063, IntType.SCENERY, "search") { player, _ ->
            sendMessage(player, "You search the box...")
            if (!inEquipmentOrInventory(player, Items.DOCTORS_GOWN_430)) {
                sendMessage(player, "...and find a doctor's gown.")
                addItemOrDrop(player, Items.DOCTORS_GOWN_430)
            } else {
                sendMessage(player, "...but you find nothing of interest.")
            }
            return@on true
        }

        onUseWith(IntType.SCENERY, Items.KEY_2832, *PLAGUE_1F_GATES) { player, _, with ->
            if (getQuestStage(player, Quests.BIOHAZARD) > 8) {
                sendMessage(player, "The key fits the gate.")
                DoorActionHandler.handleAutowalkDoor(player, with.asScenery())
            } else {
                sendMessage(player, "Nothing interesting happens.")
            }
            return@onUseWith true
        }

        on(PLAGUE_1F_GATES, IntType.SCENERY, "open") { player, node ->
            if (inInventory(player, Items.KEY_2832)) {
                sendMessage(player, "The key fits the gate.")
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else {
                sendMessage(player, "The gate is locked.")
                sendMessage(player, "You need a key.")
            }
            return@on true
        }

        on(Scenery.CRATE_34586, IntType.SCENERY, "search") { player, _ ->
            sendMessage(player, "You search the crate...")
            if (!inInventory(player, Items.DISTILLATOR_420) && getQuestStage(player, Quests.BIOHAZARD) in 8..15) {
                setQuestStage(player, Quests.BIOHAZARD, 10)
                addItemOrDrop(player, Items.DISTILLATOR_420)
                sendMessage(player, "and find Elena's distillator.")
            } else {
                sendMessage(player, "It's empty.")
            }
            return@on true
        }

        on(COMBAT_AREA, IntType.SCENERY, "open") { player, node ->
            val gatePair = when (node.id) {
                Scenery.GATE_2039 -> Pair(Scenery.GATE_2039, Scenery.GATE_2041)
                Scenery.GATE_2041 -> Pair(Scenery.GATE_2041, Scenery.GATE_2039)
                else -> null
            }
            if (isQuestComplete(player, Quests.BIOHAZARD)) {
                sendNPCDialogueLines(player, NPCs.GUARD_344, FaceAnim.NEUTRAL, false, "The king has granted you access to ths training area.", "Make good use of it, soon all your strength will be", "needed.")
                addDialogueAction(player) { _, _ ->
                    if (gatePair != null) {
                        DoorActionHandler.autowalkFence(player, node.asScenery(), gatePair.first, gatePair.second)
                    }
                }
            } else {
                sendDialogueLines(player, "You need to complete Biohazard to get access to the Combat", "Training Camp.")
            }
            return@on true
        }
    }

    override fun defineDestinationOverrides() {
        setDest(IntType.SCENERY, intArrayOf(Scenery.FENCE_2068), "squeeze-through") { player, _ ->
            if (player.location.x >= 2542) {
                return@setDest Location(2542, 3331, 0)
            } else {
                return@setDest Location.create(2541, 3331, 0)
            }
        }
    }

    inner class MournerDialogue : DialogueFile() {
        override fun handle(componentID: Int, buttonID: Int) {
            when(stage) {
                0 -> npc("Stay away from there.").also { stage++ }
                1 -> player("Why?").also { stage++ }
                2 -> npc("Several mourners are ill with food poisoning, we're waiting for a doctor.").also { stage++ }
                3 -> end()
            }
        }
    }

    companion object {
        val PLAGUE_1F_GATES = intArrayOf(Scenery.GATE_2058, Scenery.GATE_2060)
        val COMBAT_AREA = intArrayOf(Scenery.GATE_2039, Scenery.GATE_2041)
    }
}
