package content.region.other.dorgeshuun.plugin

import content.region.other.dorgeshuun.dialogue.CaveGoblinsDialogueFile
import core.api.*
import core.game.dialogue.FaceAnim
import core.game.dialogue.SequenceDialogue.dialogue
import core.game.global.action.DoorActionHandler
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.world.map.Location
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Quests
import shared.consts.Scenery

class DorgeshuunPlugin : InteractionListener {

    companion object {
        val BONE_DOORS = intArrayOf(Scenery.BONE_DOOR_32952, Scenery.BONE_DOOR_32953, Scenery.BONE_DOOR_22945)
        val DOORS = intArrayOf(Scenery.DOOR_22913, Scenery.DOOR_22914)
        val CAVE_GOBLINS = intArrayOf(
            NPCs.CAVE_GOBLIN_MINER_2069,
            NPCs.CAVE_GOBLIN_MINER_2070,
            NPCs.CAVE_GOBLIN_MINER_2071,
            NPCs.CAVE_GOBLIN_MINER_2072,
            NPCs.CAVE_GOBLIN_MINER_2075,
            NPCs.CAVE_GOBLIN_MINER_2076,
            NPCs.CAVE_GOBLIN_MINER_2077,
            NPCs.CAVE_GOBLIN_MINER_2078,
            NPCs.CAVE_GOBLIN_MINER_2079,
            NPCs.CAVE_GOBLIN_MINER_2080,
            NPCs.CAVE_GOBLIN_MINER_2081
        )
    }

    override fun defineListeners() {

        /*
         * Handles talk to Cave goblins NPC.
         */

        on(CAVE_GOBLINS, IntType.NPC, "talk-to") { player, _ ->
            openDialogue(player, CaveGoblinsDialogueFile())
            return@on true
        }

        /*
         * Handles talk to Ambassador Alvijar NPC.
         */

        on(NPCs.AMBASSADOR_ALVIJAR_5863, IntType.NPC, "talk-to") { player, _ ->
            player.dialogueInterpreter.open(5887)
            return@on true
        }

        /*
         * Handles exchange brooch for mining helmet.
         */

        onUseWith(IntType.NPC, Items.BROOCH_5008, NPCs.MISTAG_2084) { player, used, npc ->
            val randomReward = arrayOf(Items.MINING_HELMET_5013, Items.MINING_HELMET_5014).random()

            if (!isQuestComplete(player, Quests.THE_LOST_TRIBE) || !removeItem(player, used.asItem(), Container.INVENTORY)) {
                sendMessage(player, "Nothing interesting happens.")
                return@onUseWith true
            }

            dialogue(player) {
                player(FaceAnim.HALF_ASKING, "Is this your brooch?")
                npc(npc.id, FaceAnim.OLD_NORMAL,
                    "Yes! I thought I'd lost it. Thank you. Have one of these",
                    "helmets. It will be useful if you want to work in the mine."
                )
                end {
                    addItemOrDrop(player, randomReward, 1)
                    sendItemDialogue(player, randomReward, "Mistag hands you a Mining helmet.")
                }
            }
            return@onUseWith true
        }

        /*
         * Handles open the bone doors.
         */

        on(BONE_DOORS, IntType.SCENERY, "open") { player, node ->
            when (node.id) {
                Scenery.BONE_DOOR_32952, Scenery.BONE_DOOR_32953 -> {
                    if (!isQuestComplete(player, Quests.THE_LOST_TRIBE)) {
                        sendNPCDialogue(
                            player,
                            NPCs.CAVE_GOBLIN_GUARD_2073,
                            "Surface-dweller! You may not pass through that door!",
                            FaceAnim.OLD_NORMAL,
                        ).also { player.faceLocation(Location(3318, 9604, 0)) }
                    } else {
                        teleport(player, Location(if (node.id == Scenery.BONE_DOOR_32952) 2747 else 2748, 5374, 0))
                    }
                }

                Scenery.BONE_DOOR_22945 -> teleport(player, Location(3318, 9602, 0))
            }
            return@on true
        }

        /*
         * Handles open the bone doors.
         */

        on(DOORS, IntType.SCENERY, "open") { player, node ->
            DoorActionHandler.handleAutowalkDoor(player, node.asScenery())
            return@on true
        }
    }
}