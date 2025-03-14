package content.region.asgarnia.quest.ball.handlers

import core.api.*
import core.api.quest.isQuestComplete
import core.game.dialogue.FaceAnim
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.combat.ImpactHandler
import core.game.node.entity.player.link.TeleportManager
import core.game.world.map.Location
import core.game.world.map.RegionManager
import core.tools.RandomFunction
import org.rs.consts.Items
import org.rs.consts.Quests
import org.rs.consts.Scenery

class WitchHouseListener : InteractionListener {

    val items = intArrayOf(
        Items.NEEDLE_1733,
        Items.LEATHER_GLOVES_1059,
        Items.LEATHER_BOOTS_1061,
        Items.CABBAGE_1965,
        Items.THREAD_1734
    )
    private val basementGates = intArrayOf(Scenery.GATE_2865, Scenery.GATE_2866)

    override fun defineListeners() {

        /*
         * Handles search the plant for key to witch house.
         */

        on(Scenery.POTTED_PLANT_2867, IntType.SCENERY, "Look-under") { player, _ ->
            if (freeSlots(player) > 0 && !inInventory(player, Items.DOOR_KEY_2409)) {
                sendItemDialogue(player, Items.DOOR_KEY_2409, "You find a key hidden under the flower pot.")
                addItem(player, Items.DOOR_KEY_2409, 1)
            } else {
                sendMessage(player, "You search under the flower pot and find nothing.")
            }
            return@on true
        }

        /*
         * Handles stairs inside house.
         */

        on(Scenery.STAIRCASE_24672, IntType.SCENERY, "walk-up") { player, _ ->
            teleport(player, Location(2906, 3472, 1), TeleportManager.TeleportType.INSTANT, 1)
            return@on true
        }

        /*
         * Handles stairs inside the witch house.
         */

        on(Scenery.STAIRCASE_24673, IntType.SCENERY, "walk-down") { player, _ ->
            teleport(player, Location(2906, 3468, 0), TeleportManager.TeleportType.INSTANT)
            return@on true
        }

        /*
         * Handles use the door key to open the witch house doors.
         */

        onUseWith(IntType.SCENERY, Items.DOOR_KEY_2409, Scenery.DOOR_2861) { player, _, with ->
            if (isQuestComplete(player, Quests.WITCHS_HOUSE)) {
                sendMessage(player, "The lock has seemed to changed since the last time you visited.")
            }

            if (inInventory(player, Items.DOOR_KEY_2409) || player.location.x >= 2901) {
                DoorActionHandler.handleAutowalkDoor(player, with.asScenery())
            } else {
                sendMessage(player, "The door is locked.")
            }

            return@onUseWith true
        }

        /*
         * Handles interaction with witch house doors.
         */

        on(Scenery.DOOR_2861, IntType.SCENERY, "open") { player, node ->
            if (isQuestComplete(player, Quests.WITCHS_HOUSE)) {
                sendMessage(player, "The lock has seemed to changed since the last time you visited.")
                return@on true
            }

            if (inInventory(player, Items.DOOR_KEY_2409) || player.location.x >= 2901) {
                DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            } else {
                sendMessage(player, "The door is locked.")
            }
            return@on true
        }

        /*
         * Handles interaction with south doors that lead to witch garden.
         */

        on(Scenery.DOOR_2862, IntType.SCENERY, "open") { player, node ->
            val magnetAttached = getAttribute(player, "attached_magnet", false)

            if (magnetAttached || player.location.y < 3466) {
                DoorActionHandler.handleAutowalkDoor(player, (node as core.game.node.scenery.Scenery))
                removeAttribute(player, "attached_magnet")
            } else {
                sendDialogue(player, "This door is locked.")
                runTask(player, 2) {
                    sendPlayerDialogue(
                        player,
                        "Strange... I can't see any kind of lock or handle to open this door...", FaceAnim.THINKING
                    )
                }
            }
            return@on true
        }

        /*
         * Handles interaction with shed doors where ball is.
         */

        on(Scenery.DOOR_2863, IntType.SCENERY, "open") { player, node ->
            val shed = inBorders(player, 2934, 3459, 2937, 3467)
            val sadness = RegionManager.getLocalPlayers(player, 1)

            if(shed && sadness.isNotEmpty()){
                sendPlayerDialogue(player, "I'd better not go in there yet... I think I can hear someone inside!", FaceAnim.THINKING)
                return@on true
            }

            if (inInventory(player, Items.KEY_2411) || player.location.x >= 2934) {
                DoorActionHandler.handleAutowalkDoor(player, node as core.game.node.scenery.Scenery)
            } else {
                sendMessage(player, "The shed door is locked.")
            }

            return@on true
        }

        /*
         * Handles the gate interaction at the witch house basement.
         */

        on(basementGates, IntType.SCENERY, "open") { player, node ->
            if (!inEquipment(player, Items.LEATHER_GLOVES_1059)) {
                impact(player, RandomFunction.random(2, 3), ImpactHandler.HitsplatType.NORMAL)
                sendDialogue(player, "As your bare hands touch the gate you feel a shock.")
                return@on true
            }
            DoorActionHandler.handleAutowalkDoor(player, (node as core.game.node.scenery.Scenery))
            return@on true
        }

        /*
         * Handles interaction with gramophone.
         */

        on(Scenery.GRAMOPHONE_24724, IntType.SCENERY, "Wind-up") { player, _ ->
            sendMessage(player, "The gramophone doesn't have a record on it.")
            return@on true
        }

        /*
         * Handles interaction with piano.
         */

        on(Scenery.PIANO_24721, IntType.SCENERY, "play") { player, _ ->
            sendMessage(player, "You decide to not attract the attention of the witch by playing the piano.")
            return@on true
        }

        /*
         * Handles interaction with cupboard.
         */

        on(Scenery.CUPBOARD_2869, IntType.SCENERY, "search") { player, _ ->
            if (freeSlots(player) == 0) {
                sendMessage(player, "You don't have enough inventory space to hold that item.")
                return@on true
            }
            if (!inInventory(player, Items.MAGNET_2410)) {
                sendDialogueLines(player, "You find a magnet in the cupboard.")
                addItem(player, Items.MAGNET_2410, 1)
            } else {
                sendMessage(player, "You search the cupboard but find nothing interesting.")
            }
            return@on true
        }

        /*
         * Handles search the music stand.
         * Varbit: 3723
         */

        on(Scenery.MUSIC_STAND_24728, IntType.SCENERY, "search") { player, _ ->
            sendMessage(player, "You can't see anything of interest on the stand.")
            return@on true
        }

        /*
         * Handles check the fountain in witch garden.
         */

        on(Scenery.FOUNTAIN_2864, IntType.SCENERY, "check") { player, _ ->
            val readBook = getAttribute(player, "readWitchsBook", false)
            if(freeSlots(player) == 0) {
                sendMessage(player, "You search the fountain but find nothing.")
                return@on true
            }

            if (readBook && !inInventory(player, Items.KEY_2411)) {
                addItem(player, Items.KEY_2411, 1)
                sendDialogueLines(
                    player,
                    "You search for the secret compartment mentioned in the diary.",
                    "Inside it you find a small key. You take the key.",
                )
            } else {
                sendMessage(player, "You search the fountain but find nothing.")
            }
            return@on true
        }

        /*
         * Handles search the boxes.
         */

        on(Scenery.BOXES_24692, IntType.SCENERY, "search") { player, node ->
            val box = node as core.game.node.scenery.Scenery
            if (freeSlots(player) == 0) {
                sendMessage(player, "You don't have enough inventory space to hold that item.")
                return@on true
            }

            val boxItems = mapOf(
                Items.NEEDLE_1733 to Pair(
                    Location.create(2906, 9872, 0),
                    "You find a sewing needle in the bottom of one of the boxes!"
                ),
                Items.THREAD_1734 to Pair(null, "You find some sewing thread in the bottom of one of the boxes!"),
                Items.LEATHER_GLOVES_1059 to Pair(
                    Location.create(2905, 9872, 0),
                    "You find a pair of leather gloves in the bottom of one of the boxes!"
                ),
                Items.LEATHER_BOOTS_1061 to Pair(
                    Location.create(2908, 9873, 0),
                    "You find a pair of leather boots in the bottom of one of the boxes!"
                ),
                Items.CABBAGE_1965 to Pair(null, "You find an old cabbage in the bottom of one of the boxes!")
            )

            for (item in items) {
                if (!inInventory(player, item)) {
                    boxItems[item]?.let { (requiredLocation, message) ->
                        if (requiredLocation == null || box.location == requiredLocation) {
                            addItem(player, item, 1)
                            sendMessage(player, message)
                            return@on true
                        }
                    }
                }
            }

            sendMessage(player, "You find nothing interesting in the boxes.")
            return@on true
        }
    }
}