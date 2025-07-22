package content.region.misthalin.varrock.quest.dragon.plugin

import content.region.misthalin.varrock.quest.dragon.DragonSlayer
import core.api.addItemOrDrop
import core.api.sendItemDialogue
import core.api.setVarp
import core.cache.def.impl.NPCDefinition
import core.cache.def.impl.SceneryDefinition
import core.game.dialogue.SequenceDialogue.dialogue
import core.game.global.action.ClimbActionHandler
import core.game.global.action.DoorActionHandler.handleAutowalkDoor
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.plugin.Plugin
import org.rs.consts.Animations
import org.rs.consts.NPCs
import org.rs.consts.Quests

/**
 * Handles dragon slayer object interactions.
 */
class DragonSlayerPlugin : OptionHandler() {

    override fun newInstance(arg: Any?): Plugin<Any> {
        val npcOptions = listOf(747 to "trade", 742 to "attack", 745 to "attack", 745 to "talk-to")

        val sceneryOptions = listOf(
            25115,
            2595,
            32968,
            2602,
            2606,
            2600,
            2599,
            2598,
            2601,
            2596,
            2597,
            2603,
            2604,
            2605,
            2604,
            1755,
            2587,
            25036,
            2589,
            25214,
            25038,
            1746,
            1747,
            25045,
            1752,
            1747,
            25038,
            2595,
            2604,
            25161
        ).distinct()

        npcOptions.forEach { (id, option) ->
            NPCDefinition.forId(id).handlers["option:$option"] = this
        }

        sceneryOptions.forEach {
            SceneryDefinition.forId(it).handlers["option:open"] = this
        }

        /*
         * Climb options.
         */
        listOf(1746, 1755, 1752, 1747, 1752, 25045, 25038, 2605).forEach {
            SceneryDefinition.forId(it).handlers["option:climb-up"] = this
            SceneryDefinition.forId(it).handlers["option:climb-down"] = this
        }

        /*
         * Chest interactions.
         */
        SceneryDefinition.forId(2604).handlers["option:search"] = this
        SceneryDefinition.forId(2604).handlers["option:close"] = this

        return this
    }

    override fun handle(player: Player, node: Node?, option: String): Boolean {
        val quest = player.getQuestRepository().getQuest(Quests.DRAGON_SLAYER)
        val id = node?.id

        when (id) {
            NPCs.WORMBRAIN_745 -> {
                if (option == "attack") player.properties.combatPulse.attack(node)
                else player.dialogueInterpreter.open(745, node)
                return true
            }

            NPCs.OZIACH_747 -> {
                when (quest.getStage(player)) {
                    100 -> node.asNpc().openShop(player)
                    10, 15, 20, 30, 40 -> player.dialogueInterpreter.open(node.id, node, true)
                    else -> player.dialogueInterpreter.sendDialogues(
                        node.asNpc(), null, "I ain't got nothing to sell ye, adventurer. Leave me be!"
                    )
                }
                return true
            }

            25115 -> DragonSlayer.handleMagicDoor(player, true)

            2606 -> {
                val memorized = player.getSavedData().questData.getDragonSlayerAttribute("memorized")
                if (player.location.y < 9600 && !memorized && quest.getStage(player) != 100) {
                    player.packetDispatch.sendMessage("The door is securely locked.")
                } else {
                    if (!memorized) {
                        player.packetDispatch.sendMessage("You found a secret door.")
                        player.packetDispatch.sendMessage("You remember where the secret door is for future reference.")
                        player.getSavedData().questData.setDragonSlayerAttribute("memorized", true)
                        player.achievementDiaryManager.finishTask(player, DiaryType.KARAMJA, 1, 1)
                    }
                    handleAutowalkDoor(player, node.asScenery())
                }
                return true
            }

            25036, 2589 -> return handleShipRepair(player)

            2587 -> {
                if (!player.inventory.containsItem(DragonSlayer.MAGIC_PIECE) && !player.bank.containsItem(DragonSlayer.MAGIC_PIECE)) {
                    dialogue(player) {
                        message("As you open the chest, you notice an inscription on the lid:")
                        message(
                            "Here I rest the map to my beloved home. To whoever finds it, I beg",
                            "of you, let it be. I was honour-bound not to destroy the map piece,",
                            "but I have used all my magical skill to keep it from being recovered."
                        )
                        message(
                            "This map leads to the lair of the beast that destroyed my home,",
                            "devoured my family, and burned to a cinder all that I love. But",
                            "revenge would not benefit me now, and to disturb this beast is to risk",
                            "bringing its wrath down upon another land."
                        )
                        message(
                            "I cannot stop you from taking this map piece now, but think on this:",
                            "if you can slay the Dragon of Crandor, you are a greater hero than",
                            "my land ever produced. There is no shame in backing out now."
                        )
                        end {
                            addItemOrDrop(player, DragonSlayer.MAGIC_PIECE.id, 1)
                            sendItemDialogue(player, DragonSlayer.MAGIC_PIECE, "You find a map piece in the chest.")
                        }
                    }
                } else {
                    player.packetDispatch.sendMessage("You already have the map piece.")
                }
                return true
            }

            2603 -> {
                player.packetDispatch.sendMessage("You open the chest.")
                SceneryBuilder.replace(node.asScenery(), node.asScenery().transform(2604))
            }

            2604 -> return when (option) {
                "search" -> {
                    if (!player.inventory.containsItem(DragonSlayer.MAZE_PIECE)) {
                        if (!player.inventory.add(DragonSlayer.MAZE_PIECE)) {
                            GroundItemManager.create(DragonSlayer.MAZE_PIECE, player)
                        }
                        player.dialogueInterpreter.sendItemMessage(
                            DragonSlayer.MAZE_PIECE.id, "You find a map piece in the chest."
                        )
                    } else {
                        player.packetDispatch.sendMessage("You find nothing in the chest.")
                    }
                    true
                }

                "close" -> {
                    player.packetDispatch.sendMessage("You shut the chest.")
                    SceneryBuilder.replace(node.asScenery(), node.asScenery().transform(2603))
                    true
                }

                else -> false
            }

            2601 -> return handleKeyDoor(player, node.asScenery(), DragonSlayer.GREEN_KEY)
            2600 -> return handleKeyDoor(player, node.asScenery(), DragonSlayer.PURPLE_KEY)
            2599 -> return handleKeyDoor(player, node.asScenery(), DragonSlayer.BLUE_KEY)
            2598 -> return handleKeyDoor(player, node.asScenery(), DragonSlayer.YELLOW_KEY)
            2596 -> return handleKeyDoor(player, node.asScenery(), DragonSlayer.RED_KEY)
            2597 -> return handleKeyDoor(player, node.asScenery(), DragonSlayer.ORANGE_KEY)
            2595 -> {
                if (player.location.x == 2940 && player.location.y == 3248) {
                    handleAutowalkDoor(player, node.asScenery())
                    return true
                }
                if (player.inventory.containsItem(DragonSlayer.MAZE_KEY)) {
                    player.packetDispatch.sendMessage("You use the key and the door opens.")
                    handleAutowalkDoor(player, node.asScenery())
                    return true
                } else {
                    player.packetDispatch.sendMessage("This door is securely locked.")
                }
                return true
            }

            25045 -> return climbStairs(
                player, node.asScenery(), option, Location(2925, 3259, 1), Location(2924, 3258, 0)
            )

            1747 -> return climbStairs(
                player, node.asScenery(), option, Location(2940, 3256, 1), Location(2940, 3256, 2)
            )

            1746 -> {
                if (player.location.getDistance(Location(2923, 3241, 1)) < 3) {
                    ClimbActionHandler.climb(player, Animation(828), Location(2923, 3241, 0))
                } else if (player.location.getDistance(Location(2932, 3245, 2)) < 3) {
                    ClimbActionHandler.climb(player, Animation(828), Location(2932, 3245, 1))
                } else {
                    ClimbActionHandler.climbLadder(player, node.asScenery(), option)
                }
                return true
            }

            1752 -> {
                player.packetDispatch.sendMessage("The ladder is broken, I can't climb it.")
                return true
            }

            1755 -> return climbStairs(
                player, node.asScenery(), option, Location(2939, 9656, 0), Location(2939, 3256, 0)
            )

            25214 -> {
                player.packetDispatch.sendMessage("The trapdoor can only be opened from below.")
                return true
            }

            25038, 2605 -> {
                ClimbActionHandler.climbLadder(player, node.asScenery(), option)
                return true
            }
        }

        return true
    }

    private fun handleKeyDoor(player: Player, node: Node, requiredKey: Item): Boolean {
        return if (!player.inventory.containsItem(requiredKey)) {
            player.packetDispatch.sendMessage("This door is securely locked.")
            true
        } else {
            player.inventory.remove(requiredKey)
            player.packetDispatch.sendMessage("The key disintegrates as it unlocks the door.")
            handleAutowalkDoor(player, node.asScenery())
            true
        }
    }

    private fun handleShipRepair(player: Player): Boolean {
        if (player.getSavedData().questData.getDragonSlayerAttribute("memorized")) {
            player.dialogueInterpreter.sendDialogue(
                "You don't need to mess about with broken ships now that you have",
                "found the secret passage from Karamja."
            )
            return true
        }
        if (!player.inventory.containsItem(DragonSlayer.NAILS)) {
            player.dialogueInterpreter.sendDialogue("You need 30 steel nails to attach the plank with.")
            return true
        }
        if (!player.inventory.containsItem(DragonSlayer.PLANK)) {
            player.dialogueInterpreter.sendDialogue("You'll need to use wooden planks on this hole to patch it up.")
            return true
        }
        if (!player.inventory.containsItem(DragonSlayer.HAMMER)) {
            player.dialogueInterpreter.sendDialogue("You need a hammer to force the nails in with.")
            return true
        }
        if (player.inventory.remove(DragonSlayer.NAILS) && player.inventory.remove(DragonSlayer.PLANK)) {
            player.lock(2)
            player.animate(HAMMER_ANIM)
            player.getSavedData().questData.dragonSlayerPlanks += 1
            if (player.getSavedData().questData.dragonSlayerPlanks < 3) {
                player.dialogueInterpreter.sendDialogue(
                    "You nail a plank over the hole, but you still need more planks to", "close the hole completely."
                )
            } else {
                player.getSavedData().questData.setDragonSlayerAttribute("repaired", true)
                setVarp(player, 177, 1967876)
                player.dialogueInterpreter.sendDialogue(
                    "You nail a final plank over the hole. You have successfully patched", "the hole in the ship."
                )
            }
        }
        return true
    }

    private fun climbStairs(player: Player, node: Scenery, option: String, pos1: Location, pos2: Location): Boolean {
        return when (option) {
            "climb-up" -> {
                if (player.location.getDistance(pos1) < 3) {
                    ClimbActionHandler.climb(player, Animation(828), pos2)
                } else if (player.location.getDistance(pos2) < 3) {
                    ClimbActionHandler.climb(player, Animation(828), pos1)
                } else {
                    ClimbActionHandler.climbLadder(player, node, option)
                }
                true
            }

            "climb-down" -> {
                if (player.location.getDistance(pos2) < 3) {
                    ClimbActionHandler.climb(player, Animation(828), pos1)
                } else if (player.location.getDistance(pos1) < 3) {
                    ClimbActionHandler.climb(player, Animation(828), pos2)
                } else {
                    ClimbActionHandler.climbLadder(player, node, option)
                }
                true
            }

            else -> false
        }
    }

    override fun getDestination(node: Node, n: Node): Location? {
        if (n is Scenery) {
            if (n.id == 25115) {
                return if (node.location.x <= 3049) {
                    Location.create(3049, 9840, 0)
                } else {
                    Location.create(3051, 9840, 0)
                }
            } else if (n.id == 2587) {
                return Location.create(3056, 9841, 0)
            }
        } else if (n is NPC) {
            if (n.id == 745) {
                return Location.create(3012, 3188, 0)
            }
        }
        return null
    }

    override fun isWalk(): Boolean {
        return false
    }

    override fun isWalk(player: Player, node: Node): Boolean {
        return node !is Item
    }

    companion object {
        private val HAMMER_ANIM = Animation(Animations.BUILD_WITH_HAMMER_3676)
    }
}
