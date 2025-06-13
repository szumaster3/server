package content.minigame.allfiredup.plugin

import content.global.skill.construction.item.Nails
import core.api.*
import core.api.quest.hasRequirement
import core.game.interaction.IntType
import core.game.interaction.InteractionListener
import core.game.node.entity.impl.ForceMovement
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.Item
import core.game.world.map.Direction
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import org.rs.consts.Animations
import org.rs.consts.Items
import org.rs.consts.Quests
import org.rs.consts.Scenery
import kotlin.random.Random

/**
 * Handles the repair and climbing interactions for the All Fired Up minigame.
 */
class AFURepairPlugin : InteractionListener {
    private val repairIDs = intArrayOf(Scenery.IRON_PEGS_38480, Scenery.WINDBREAK_38494, Scenery.LADDER_38470)
    private val climbIDs = intArrayOf(Scenery.LADDER_38469, Scenery.LADDER_38471, Scenery.IRON_PEG_38486, Scenery.IRON_PEGS_38481)

    override fun defineListeners() {
        on(repairIDs, IntType.SCENERY, "repair") { player, _ ->
            if (!hasRequirement(player, Quests.ALL_FIRED_UP)) return@on false
            val rco = getClimbingObject(player) ?: return@on false
            repair(player, rco)
            return@on true
        }

        on(climbIDs, IntType.SCENERY, "climb") { player, node ->
            val rco = getClimbingObject(player) ?: return@on false
            climb(player, rco, node.location)
            return@on true
        }
    }

    private fun getClimbingObject(player: Player): RepairClimbObject? =
        RepairClimbObject.values().firstOrNull {
            it.destinationDown?.withinDistance(player.location, 2) == true ||
                    it.destinationUp?.withinDistance(player.location, 2) == true
        }

    private fun repair(player: Player, rco: RepairClimbObject) {
        val inventory = player.inventory

        if (rco == RepairClimbObject.TEMPLE) {
            val hasSmithing = getDynLevel(player, Skills.SMITHING) >= 70
            val hasConstruction = getDynLevel(player, Skills.CONSTRUCTION) >= 59

            if (!hasSmithing && !hasConstruction) {
                sendDialogue(player, "You need level 70 smithing or 59 construction for this.")
                return
            }

            val hasHammer = inventory.contains(Items.HAMMER_2347, 1)
            val hasIronBars = inventory.contains(Items.IRON_BAR_2351, 2)
            val hasPlanks = inventory.contains(Items.PLANK_960, 2)

            if (hasSmithing && hasHammer && hasIronBars) {
                if (inventory.remove(Item(Items.IRON_BAR_2351, 2))) {
                    setVarbit(player, rco.varbit, 1, true)
                    return
                }
            }

            if (hasConstruction && hasHammer && hasPlanks) {
                Nails.get(player, 4)?.let { nails ->
                    val nailItem = Item(nails.itemId, 4)
                    if (inventory.contains(nails.itemId, 4) &&
                        inventory.remove(Item(Items.PLANK_960, 2)) &&
                        inventory.remove(nailItem)) {
                        setVarbit(player, rco.varbit, 1, true)
                        return
                    }
                }
            }

            val smithReq = if (hasSmithing) "a hammer and 2 iron bars" else ""
            val conReq = if (hasConstruction) "a hammer, 2 planks and 4 nails" else ""
            val separator = if (hasSmithing && hasConstruction) " or " else ""
            sendDialogue(player, "You need $smithReq$separator$conReq for this.")
            return
        }

        val (skill, level) = rco.levelRequirement ?: (0 to 0)
        if (player.skills.getLevel(skill) < level) {
            sendDialogue(player, "You need level $level ${Skills.SKILL_NAME[skill]} for this.")
            return
        }

        val requiredItems = when (rco) {
            RepairClimbObject.DEATH_PLATEAU -> arrayOf(Item(Items.PLANK_960, 2))
            RepairClimbObject.BURTHORPE -> arrayOf(Item(Items.IRON_BAR_2351, 2))
            RepairClimbObject.GWD -> arrayOf(Item(Items.JUTE_FIBRE_5931, 3))
            else -> return
        }

        val requiredAll = requiredItems.map { it.id }.toIntArray()

        if (rco == RepairClimbObject.GWD) {
            val hasNeedle = inventory.containsItem(Item(Items.NEEDLE_1733))
            if (hasNeedle && player.inventory.containItems(*requiredAll)) {
                inventory.remove(*requiredItems)
                if (Random.nextBoolean()) inventory.remove(Item(Items.NEEDLE_1733))
                setVarbit(player, rco.varbit, 1, true)
            } else {
                val needed = requiredItems.joinToString(", ") { "${it.amount} ${it.name.lowercase()}" }
                sendDialogue(player, "You need a needle and $needed for this.")
            }
            return
        }

        val hasHammer = inventory.containsItem(Item(Items.HAMMER_2347))
        if (!hasHammer || !inventory.containsAll(*requiredAll)) {
            val needed = requiredItems.joinToString(", ") { "${it.amount} ${it.name.lowercase()}" }
            sendDialogue(player, "You need a hammer and $needed for this.")
            return
        }

        if (rco == RepairClimbObject.DEATH_PLATEAU) {
            Nails.get(player, 4)?.let { nails ->
                val nailItem = Item(nails.itemId, 4)
                if (!inventory.contains(nails.itemId, 4)) {
                    sendDialogue(player, "You need 4 nails for this.")
                    return
                }
                inventory.remove(nailItem)
            }
        }

        inventory.remove(*requiredItems)
        setVarbit(player, rco.varbit, 1, true)
    }

    private fun climb(player: Player, rco: RepairClimbObject, location: Location) {
        val destination = rco.getOtherLocation(player) ?: return
        val direction = rco.getDirection(player)
        val animation = rco.getAnimation(player)
        ForceMovement.run(player, location, destination, animation, animation, direction, 20).endAnimation = Animation.RESET
    }

    private enum class RepairClimbObject(val varbit: Int, val destinationUp: Location?, val destinationDown: Location?, val levelRequirement: Pair<Int, Int>?, ) {
        DEATH_PLATEAU(5161, Location.create(2949, 3623, 0), Location.create(2954, 3623, 0), Skills.CONSTRUCTION to 42),
        BURTHORPE(5160, Location.create(2941, 3563, 0), Location.create(2934, 3563, 0), Skills.SMITHING to 56),
        GWD(5163, null, null, Skills.CRAFTING to 60),
        TEMPLE(5164, Location.create(2949, 3835, 0), Location.create(2956, 3835, 0), null);

        fun getOtherLocation(player: Player): Location? =
            if (player.location == destinationDown) destinationUp else destinationDown

        fun getAnimation(player: Player): Animation =
            if (getOtherLocation(player) == destinationDown) {
                Animation(Animations.WALK_BACKWARDS_CLIMB_1148)
            } else Animation(Animations.CLIMB_DOWN_B_740)

        fun getDirection(player: Player): Direction =
            if (this == BURTHORPE) Direction.EAST else Direction.WEST

        fun isRepaired(player: Player): Boolean = getVarbit(player, varbit) == 1
    }

    companion object {
        fun isRepaired(player: Player, beacon: AFUBeacon): Boolean = when (beacon) {
            AFUBeacon.DEATH_PLATEAU -> RepairClimbObject.DEATH_PLATEAU.isRepaired(player)
            AFUBeacon.BURTHORPE -> RepairClimbObject.BURTHORPE.isRepaired(player)
            AFUBeacon.GWD -> RepairClimbObject.GWD.isRepaired(player)
            AFUBeacon.TEMPLE -> RepairClimbObject.TEMPLE.isRepaired(player)
            else -> true
        }
    }
}
