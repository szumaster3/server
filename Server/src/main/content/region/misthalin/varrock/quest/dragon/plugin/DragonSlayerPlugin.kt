package content.region.misthalin.varrock.quest.dragon.plugin

import content.region.misthalin.varrock.quest.dragon.DragonSlayer
import core.cache.def.impl.NPCDefinition
import core.cache.def.impl.SceneryDefinition
import core.game.global.action.ClimbActionHandler.climb
import core.game.global.action.ClimbActionHandler.climbLadder
import core.game.global.action.DoorActionHandler.handleAutowalkDoor
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.Entity
import core.game.node.entity.impl.ForceMovement
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.diary.DiaryType
import core.game.node.entity.player.link.quest.Quest
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.node.scenery.SceneryBuilder
import core.game.world.map.Location
import core.game.world.map.RegionManager.getLocalNpcs
import core.game.world.update.flag.context.Animation
import core.plugin.Plugin
import shared.consts.Quests

/**
 * Represents the plugin used to handle node interactions related to dragon slayer.
 *
 * @author Vexia
 */
class DragonSlayerPlugin : OptionHandler() {
    @Throws(Throwable::class)
    override fun newInstance(arg: Any?): Plugin<Any> {
        // door.
        NPCDefinition.forId(747).handlers["option:trade"] = this
        SceneryDefinition.forId(2595).handlers["option:open"] = this
        // main
        // door.
        // maze first floor.
        SceneryDefinition.forId(32968).handlers["option:open"] = this
        SceneryDefinition.forId(2602).handlers["option:open"] = this
        // door.
        SceneryDefinition.forId(1752).handlers["option:climb-up"] = this
        SceneryDefinition.forId(25038).handlers["option:climb-up"] = this
        // ladder
        SceneryDefinition.forId(25214).handlers["option:open"] = this // trapdoor
        SceneryDefinition.forId(1746).handlers["option:climb-down"] = this // ladder
        SceneryDefinition.forId(2605).handlers["option:climb-down"] = this // ladder
        // door.
        SceneryDefinition.forId(1747).handlers["option:climb-up"] = this
        SceneryDefinition.forId(25045).handlers["option:climb-down"] = this
        // chest.
        SceneryDefinition.forId(1755).handlers["option:climb-up"] = this
        // dwarv mine
        SceneryDefinition.forId(2587).handlers["option:open"] = this
        NPCDefinition.forId(745).handlers["option:talk-to"] = this
        // lady lumby
        SceneryDefinition.forId(25036).handlers["option:repair"] = this
        SceneryDefinition.forId(2589).handlers["option:repair"] = this

        // crandor
        SceneryDefinition.forId(25154).handlers["option:enter"] = this
        SceneryDefinition.forId(2606).handlers["option:open"] = this
        SceneryDefinition.forId(25213).handlers["option:climb"] = this
        SceneryDefinition.forId(25161).handlers["option:climb-over"] = this
        NPCDefinition.forId(742).handlers["option:attack"] = this
        NPCDefinition.forId(745).handlers["option:attack"] = this
        return this
    }

    override fun handle(player: Player, node: Node, option: String): Boolean {
        val quest = player.getQuestRepository().getQuest(Quests.DRAGON_SLAYER)
        val id = if (node is Item) node.id else if (node is Scenery) node.id else (node as NPC).id
        when (id) {
            1755 ->
                if (player.location.withinDistance(Location.create(2939, 9656, 0))) {
                    climb(player, Animation(828), Location.create(2939, 3256, 0))
                } else {
                    climbLadder(player, node as Scenery, option)
                    return true
                }
            742 -> {
                if (
                    player.getQuestRepository().getQuest(Quests.DRAGON_SLAYER).getStage(player) == 40 &&
                    (player.inventory.containsItem(DragonSlayer.ELVARG_HEAD))
                ) {
                    player.packetDispatch.sendMessage("You have already slain the dragon. Now you just need to return to Oziach for")
                    player.packetDispatch.sendMessage("your reward!")
                    return true
                }
                if (player.getQuestRepository().getQuest(Quests.DRAGON_SLAYER).getStage(player) > 40) {
                    player.packetDispatch.sendMessage("You have already slain Elvarg the dragon.")
                    return true
                }
                player.properties.combatPulse.attack(node)
                player.face(node as Entity)
            }
            25161 -> {
                if (player.location.x >= 2847) {
                    val movement =
                        ForceMovement(
                            player,
                            player.location,
                            player.location.transform(if (player.location.x == 2845) 2 else -2, 0, 0),
                            Animation(839)
                        )
                    movement.run(player, 10)
                    return true
                }
                if (
                    player.getQuestRepository().getQuest(Quests.DRAGON_SLAYER).getStage(player) == 40 &&
                    (player.inventory.containsItem(DragonSlayer.ELVARG_HEAD))
                ) {
                    player.packetDispatch.sendMessage("You have already slain the dragon. Now you just need to return to Oziach for")
                    player.packetDispatch.sendMessage("your reward!")
                    return true
                }
                if (player.getQuestRepository().getQuest(Quests.DRAGON_SLAYER).getStage(player) > 40) {
                    player.packetDispatch.sendMessage("You have already slain the dragon.")
                    return true
                }
                if (
                    player.getQuestRepository().getQuest(Quests.DRAGON_SLAYER).getStage(player) == 40 &&
                    !player.inventory.containsItem(DragonSlayer.ELVARG_HEAD)
                ) {
                    val movement =
                        ForceMovement(
                            player,
                            player.location,
                            player.location.transform(if (player.location.x == 2845) 2 else -2, 0, 0),
                            Animation(839)
                        )
                    movement.run(player, 10)
                    if (player.location.x <= 2845) {
                        val npcs = getLocalNpcs(player)
                        for (n in npcs) {
                            if (n.id == 742) {
                                n.properties.combatPulse.attack(player)
                                return true
                            }
                        }
                    }
                }
            }
            25213 -> {
                climb(player, Animation(828), Location(2834, 3258, 0))
                player.achievementDiaryManager.finishTask(player, DiaryType.KARAMJA, 1, 2)
            }
            2606 ->
                if (
                    player.location.y < 9600 &&
                    !player.getSavedData().questData.getDragonSlayerAttribute("memorized") &&
                    player.getQuestRepository().getQuest(Quests.DRAGON_SLAYER).getStage(player) != 100
                ) {
                    player.packetDispatch.sendMessage("The door is securely locked.")
                } else {
                    if (!player.getSavedData().questData.getDragonSlayerAttribute("memorized")) {
                        player.packetDispatch.sendMessage("You found a secret door.")
                        player.packetDispatch.sendMessage("You remember where the secret door is for future reference.")
                    }
                    player.achievementDiaryManager.finishTask(player, DiaryType.KARAMJA, 1, 1)
                    player.getSavedData().questData.setDragonSlayerAttribute("memorized", true)
                    handleAutowalkDoor(player, (node as Scenery))
                }
            25154 -> climb(player, Animation(828), Location(2833, 9658, 0))
            745 -> {
                if (option == "attack") {
                    player.properties.combatPulse.attack(node)
                    return true
                }
                player.dialogueInterpreter.open(745, (node as NPC))
            }
            2587 ->
                if (!player.inventory.containsItem(DragonSlayer.MAGIC_PIECE) && !player.bank.containsItem(DragonSlayer.MAGIC_PIECE)) {
                    player.dialogueInterpreter.open(3802875)
                } else {
                    player.packetDispatch.sendMessage("You already have the map piece.")
                }
            25115 -> {
                DragonSlayer.handleMagicDoor(player, true)
                return true
            }
            747 ->
                when (quest.getStage(player)) {
                    100 -> node.asNpc().openShop(player)
                    20,
                    30,
                    40,
                    15,
                    10 -> player.dialogueInterpreter.open((node as NPC).id, node, true)
                    else -> player.dialogueInterpreter.sendDialogues((node as NPC), null, "I ain't got nothing to sell ye, adventurer. Leave me be!")
                }
            else -> handleMelzarMaze(player, node, option, id, quest)
        }
        return true
    }

    /**
     * Method used to handle the melzar maze nodes.
     *
     * @param player the player.
     * @param node the node.
     * @param option the option.
     * @param id the id.
     * @param quest the quest.
     * @return `True` if so.
     */
    private fun handleMelzarMaze(player: Player, node: Node, option: String, id: Int, quest: Quest): Boolean {
        when (id) {
            2605 -> climb(player, Animation(827), Location.create(2933, 9640, 0))
            25045 -> {
                if (player.location.getDistance(Location(2925, 3259, 1)) < 3) {
                    climb(player, Animation(828), Location.create(2924, 3258, 0))
                    return true
                }
                climbLadder(player, node as Scenery, option)
                return true
            }
            1747 -> {
                if (player.location.getDistance(Location(2940, 3256, 1)) < 3) {
                    climb(player, Animation(828), Location.create(2940, 3256, 2))
                    return true
                }
                climbLadder(player, node as Scenery, option)
                return true
            }
            25214 -> player.packetDispatch.sendMessage("The trapdoor can only be opened from below.")
            25038 -> {
                climbLadder(player, node as Scenery, option)
                return true
            }
            1752 -> player.packetDispatch.sendMessage("The ladder is broken, I can't climb it.")
            1746 -> {
                if (player.location.getDistance(Location.create(2923, 3241, 1)) < 3) {
                    climb(player, Animation(828), Location.create(2923, 3241, 0))
                    return true
                }
                if (player.location.getDistance(Location.create(2932, 3245, 2)) < 3) {
                    climb(player, Animation(828), Location.create(2932, 3245, 1))
                    return true
                }
                climbLadder(player, node as Scenery, option)
                return true
            }
            2596 ->
                if (!player.inventory.containsItem(DragonSlayer.RED_KEY)) {
                    player.packetDispatch.sendMessage("This door is securely locked.")
                } else {
                    player.inventory.remove(DragonSlayer.RED_KEY)
                    player.packetDispatch.sendMessage("The key disintegrates as it unlocks the door.")
                    handleAutowalkDoor(player, (node as Scenery))
                    return true
                }
            2597 -> {
                if (!player.inventory.containsItem(DragonSlayer.ORANGE_KEY)) {
                    player.packetDispatch.sendMessage("This door is securely locked.")
                } else {
                    player.inventory.remove(DragonSlayer.ORANGE_KEY)
                    player.packetDispatch.sendMessage("The key disintegrates as it unlocks the door.")
                    handleAutowalkDoor(player, (node as Scenery))
                    return true
                }
                if (player.location == Location(2931, 9640, 0)) {
                    handleAutowalkDoor(player, (node as Scenery))
                    return true
                }
                if (player.location == Location(2927, 9649, 0)) {
                    handleAutowalkDoor(player, (node as Scenery))
                    return true
                }
                if (player.location == Location.create(2924, 9654, 0) || player.location == Location.create(2938, 3252, 0)) {
                    handleAutowalkDoor(player, (node as Scenery))
                    return true
                }
                player.packetDispatch.sendMessage("The door is locked.")
            }
            32968,
            2602 -> {
                if (player.location == Location(2931, 9640, 0)) {
                    handleAutowalkDoor(player, (node as Scenery))
                    return true
                }
                if (player.location == Location(2927, 9649, 0)) {
                    handleAutowalkDoor(player, (node as Scenery))
                    return true
                }
                if (player.location == Location.create(2924, 9654, 0) || player.location == Location.create(2938, 3252, 0)) {
                    handleAutowalkDoor(player, (node as Scenery))
                    return true
                }
                player.packetDispatch.sendMessage("The door is locked.")
            }
            2595 -> {
                if (player.location == Location.create(2940, 3248, 0)) {
                    handleAutowalkDoor(player, (node as Scenery))
                    return true
                }
                if (player.inventory.containsItem(DragonSlayer.MAZE_KEY)) {
                    player.packetDispatch.sendMessage("You use the key and the door opens.")
                    handleAutowalkDoor(player, (node as Scenery))
                    return true
                } else {
                    player.packetDispatch.sendMessage("This door is securely locked.")
                }
            }
        }
        return true
    }

    override fun getDestination(node: Node, n: Node): Location? {
        if (n is Scenery) {
            val obj = n
            if (obj.id == 25115) {
                return if (node.location.x <= 3049) {
                    Location.create(3049, 9840, 0)
                } else {
                    Location.create(3051, 9840, 0)
                }
            } else if (obj.id == 2587) {
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
}
