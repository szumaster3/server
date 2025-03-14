package content.minigame.allfiredup.handlers

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
import org.rs.consts.Scenery
import kotlin.random.Random

class AFURepairClimbHandler : InteractionListener {
    private val repairIDs = intArrayOf(Scenery.IRON_PEGS_38480, Scenery.WINDBREAK_38494, Scenery.LADDER_38470)
    private val climbIDs =
        intArrayOf(Scenery.LADDER_38469, Scenery.LADDER_38471, Scenery.IRON_PEG_38486, Scenery.IRON_PEGS_38481)

    override fun defineListeners() {
        on(repairIDs, IntType.SCENERY, "repair") { player, _ ->
            if (hasRequirement(player, "All Fired Up")) {
                getClimbingObject(player)?.let { repair(player, it) }
                return@on true
            } else {
                return@on false
            }
        }

        on(climbIDs, IntType.SCENERY, "climb") { player, node ->
            getClimbingObject(player)?.let { climb(player, it, node.location) }
            return@on true
        }
    }

    private fun getClimbingObject(player: Player): RepairClimbObject? {
        return RepairClimbObject.values().firstOrNull {
            it.destinationDown?.withinDistance(player.location, 2) == true ||
                it.destinationUp?.withinDistance(player.location, 2) == true
        }
    }

    private fun repair(
        player: Player,
        rco: RepairClimbObject,
    ) {
        if (rco == RepairClimbObject.TEMPLE) {
            val hasSmithingLevel = getDynLevel(player, Skills.SMITHING) >= 70
            val hasConstructionLevel = getDynLevel(player, Skills.CONSTRUCTION) >= 59

            if (!hasSmithingLevel && !hasConstructionLevel) {
                sendDialogue(player, "You need level 70 smithing or 59 construction for this.")
                return
            }

            val hasHammer = inInventory(player, Items.HAMMER_2347)
            val hasRequiredSmith = hasHammer && inInventory(player, Items.IRON_BAR_2351, 2)
            val hasRequiredItems = hasHammer && inInventory(player, Items.PLANK_960, 2)

            if (hasSmithingLevel && hasRequiredSmith && removeItem(player, Item(Items.IRON_BAR_2351, 2))) {
                setVarbit(player, rco.varbit, 1, true)
                return
            }

            if (hasConstructionLevel && hasRequiredItems) {
                Nails.get(player, 4)?.let { nails ->
                    if (removeItem(player, Item(Items.PLANK_960, 2)) && removeItem(player, Item(nails.itemId, 4))) {
                        setVarbit(player, rco.varbit, 1, true)
                        return
                    }
                }
            }

            val smithingReq = if (hasSmithingLevel) "a hammer and 2 iron bars" else ""
            val constructionReq = if (hasConstructionLevel) "a hammer, 2 planks and 4 nails" else ""
            sendDialogue(
                player,
                "You need $smithingReq${if (hasSmithingLevel && hasConstructionLevel) " or " else ""}$constructionReq for this.",
            )
            return
        }

        val (skill, level) = rco.levelRequirement ?: Pair(0, 0)
        if (player.skills.getLevel(skill) < level) {
            sendDialogue(player, "You need level $level ${Skills.SKILL_NAME[skill]} for this.")
            return
        }

        val requiredItems =
            when (rco) {
                RepairClimbObject.DEATH_PLATEAU -> arrayOf(Item(Items.PLANK_960, 2))
                RepairClimbObject.BURTHORPE -> arrayOf(Item(Items.IRON_BAR_2351, 2))
                RepairClimbObject.GWD -> arrayOf(Item(Items.JUTE_FIBRE_5931, 3))
                else -> return
            }

        val requiresNeedle = rco == RepairClimbObject.GWD
        if (requiresNeedle) {
            if (player.inventory.containsItem(Item(Items.NEEDLE_1733)) &&
                player.inventory.containItems(
                    *requiredItems
                        .map { it.id }
                        .toIntArray(),
                )
            ) {
                player.inventory.remove(*requiredItems)
                if (Random.nextBoolean()) player.inventory.remove(Item(Items.NEEDLE_1733))
            } else {
                val neededItems = requiredItems.joinToString(", ") { "${it.amount} ${it.name.lowercase()}" }
                sendDialogue(player, "You need a needle and $neededItems for this.")
                return
            }
        } else {
            if (player.inventory.containsItem(Item(Items.HAMMER_2347)) &&
                player.inventory.containItems(
                    *requiredItems
                        .map { it.id }
                        .toIntArray(),
                )
            ) {
                Nails.get(player, 4)?.let { nails ->
                    if (rco == RepairClimbObject.DEATH_PLATEAU) {
                        if (!player.inventory.containsItem(Item(nails.itemId, 4))) {
                            sendDialogue(player, "You need 4 nails for this.")
                            return
                        }
                        player.inventory.remove(Item(nails.itemId, 4))
                    }
                }
                player.inventory.remove(*requiredItems)
            } else {
                val neededItems = requiredItems.joinToString(", ") { "${it.amount} ${it.name.lowercase()}" }
                sendDialogue(player, "You need a hammer and $neededItems for this.")
                return
            }
        }
        setVarbit(player, rco.varbit, 1, true)
    }

    private fun climb(
        player: Player,
        rco: RepairClimbObject,
        location: Location,
    ) {
        val destination = rco.getOtherLocation(player)
        val direction = rco.getDirection(player)
        val animation = rco.getAnimation(player)
        ForceMovement.run(player, location, destination, animation, animation, direction, 20).endAnimation =
            Animation.RESET
    }

    private enum class RepairClimbObject(
        val varbit: Int,
        val destinationUp: Location?,
        val destinationDown: Location?,
        val levelRequirement: Pair<Int, Int>?,
    ) {
        DEATH_PLATEAU(
            5161,
            Location.create(2949, 3623, 0),
            Location.create(2954, 3623, 0),
            Pair(Skills.CONSTRUCTION, 42),
        ),
        BURTHORPE(5160, Location.create(2941, 3563, 0), Location.create(2934, 3563, 0), Pair(Skills.SMITHING, 56)),
        GWD(5163, null, null, Pair(Skills.CRAFTING, 60)),
        TEMPLE(5164, Location.create(2949, 3835, 0), Location.create(2956, 3835, 0), Pair(0, 0)),
        ;

        fun getOtherLocation(player: Player): Location? =
            if (player.location == destinationDown) destinationUp else destinationDown

        fun getAnimation(player: Player): Animation =
            if (getOtherLocation(player) ==
                destinationDown
            ) {
                Animation(Animations.WALK_BACKWARDS_CLIMB_1148)
            } else {
                Animation(
                    Animations.CLIMB_DOWN_B_740,
                )
            }

        fun getDirection(player: Player): Direction = if (this == BURTHORPE) Direction.EAST else Direction.WEST

        fun isRepaired(player: Player): Boolean = getVarbit(player, varbit) == 1
    }

    companion object {
        fun isRepaired(
            player: Player,
            beacon: AFUBeacon,
        ): Boolean =
            when (beacon) {
                AFUBeacon.DEATH_PLATEAU -> RepairClimbObject.DEATH_PLATEAU.isRepaired(player)
                AFUBeacon.BURTHORPE -> RepairClimbObject.BURTHORPE.isRepaired(player)
                AFUBeacon.GWD -> RepairClimbObject.GWD.isRepaired(player)
                AFUBeacon.TEMPLE -> RepairClimbObject.TEMPLE.isRepaired(player)
                else -> true
            }
    }
}
