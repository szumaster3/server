@file:Suppress("unused", "MemberVisibilityCanBePrivate", "PropertyName")

package content.global.skill.hunter.tracking

import core.api.*
import core.game.interaction.OptionHandler
import core.game.node.Node
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.node.scenery.Scenery
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.game.world.update.flag.context.Animation
import core.tools.Log
import core.tools.RandomFunction
import shared.consts.Animations
import shared.consts.Items
import shared.consts.Sounds

/**
 * Base handler for Hunter tracking interactions (e.g. kebbit tracking).
 */
abstract class HunterTracking : OptionHandler() {

    /** Successful catch animation using the noose wand. */
    var catchingKebbitAnimation = Animation(Animations.CATCH_KEBBIT_NOOSE_WAND_5257)

    /** Failed catch animation. */
    private val catchingFailAnimation = Animation(Animations.USE_NOOSE_WAND_5255)

    /** Maximum number of possible trail spots to generate. */
    var trailLimit = 0

    /** Player attribute name storing the generated trail. */
    var attribute = ""

    /** Player attribute name storing the current trail index. */
    var indexAttribute = ""

    /** Rewards given when the player successfully completes the trail. */
    var rewards: Array<Item> = emptyArray()

    /** Locations considered tunnel entrances. */
    var tunnelEntrances: Array<Location> = emptyArray()

    /** Map of objectId -> possible initial trails. */
    var initialMap = hashMapOf<Int, ArrayList<TrailDefinition>>()

    /** All linking trails used to extend paths. */
    var linkingTrails = arrayListOf<TrailDefinition>()

    /** Experience granted on success. */
    var experience = 0.0

    /** Varp id used for client varp updates (if applicable). */
    var varp = 0

    /** Minimum Hunter level required to start tracking. */
    var requiredLevel = 1

    /**
     * Returns a random starting trail for the given scenery object, or null
     * when none is configured.
     */
    private fun getInitialTrail(obj: Scenery): TrailDefinition? =
        initialMap[obj.id]?.randomOrNull()

    /**
     * Generates a trail for the given player starting at [startObject]. The
     * generated trail is stored on the player under [attribute]. If generation
     * fails (too many failed attempts), the trail is cleared.
     */
    private fun generateTrail(startObject: Scenery, player: Player) {
        val trail = player.getAttribute(attribute, arrayListOf<TrailDefinition>())
        val initialTrail = getInitialTrail(startObject)

        if (initialTrail == null) {
            log(this::class.java, Log.WARN, "UNHANDLED STARTING OBJECT FOR HUNTER TRACKING $startObject")
            return
        }

        trail.add(initialTrail)
        player.setAttribute(attribute, trail)

        var spotsLeft = RandomFunction.random(2, trailLimit)
        var triesRemaining = spotsLeft * 3

        while (spotsLeft > 0) {
            if (triesRemaining-- <= 0) {
                clearTrail(player)
                return
            }

            val nextTrail = getLinkingTrail(player) ?: continue

            // don't reuse the same varbit (avoid duplicates)
            if (trail.any { it.varbit == nextTrail.varbit }) continue

            // tunnels are allowed and don't decrement spotsLeft
            trail.add(nextTrail)
            if (nextTrail.type != TrailType.TUNNEL) spotsLeft--

            player.setAttribute(attribute, trail)
        }
    }

    /**
     * Picks a suitable linking trail based on the player current trail
     * progress.
     */
    private fun getLinkingTrail(player: Player): TrailDefinition? {
        val trail = player.getAttribute(attribute, arrayListOf<TrailDefinition>())
        val previousTrail = trail.lastOrNull() ?: return null

        val possibleTrails = if (previousTrail.type == TrailType.TUNNEL) {
            linkingTrails.filter { trailDef ->
                val inv = getTrailInverse(trailDef, swapLocations = false)
                inv.type == TrailType.TUNNEL &&
                        previousTrail.endLocation.withinDistance(inv.startLocation, 5) &&
                        previousTrail.endLocation != inv.startLocation &&
                        previousTrail.varbit != trailDef.varbit
            }
        } else {
            linkingTrails.filter { trailDef ->
                trailDef.startLocation == previousTrail.endLocation && previousTrail.varbit != trailDef.varbit
            }
        }

        return possibleTrails.randomOrNull()
    }

    /**
     * Creates an inverse of the provided [trail]. When [swapLocations] is true
     * start and end locations are swapped and triggerObjectLocation is preserved.
     */
    private fun getTrailInverse(trail: TrailDefinition, swapLocations: Boolean): TrailDefinition {
        return if (swapLocations) {
            TrailDefinition(
                trail.varbit,
                if (tunnelEntrances.contains(trail.startLocation)) TrailType.TUNNEL else TrailType.LINKING,
                !trail.inverted,
                trail.endLocation,
                trail.startLocation,
                trail.triggerObjectLocation,
            )
        } else {
            TrailDefinition(
                trail.varbit,
                if (tunnelEntrances.contains(trail.startLocation)) TrailType.TUNNEL else TrailType.LINKING,
                !trail.inverted,
                trail.startLocation,
                trail.endLocation,
            )
        }
    }

    /**
     * Adds reverse/inverse connections for linking trails and (for polar kebbits)
     * includes initial trails into the linking list.
     */
    fun addExtraTrails() {
        linkingTrails.toList().forEach { trail ->
            linkingTrails.add(getTrailInverse(trail, true))
        }

        if (this is PolarKebbitHunting) {
            initialMap.values.forEach {
                linkingTrails.addAll(it)
                it.forEach { trail -> linkingTrails.add(getTrailInverse(trail, true)) }
            }
        }
    }

    /**
     * Clears player trail attributes and resets varp.
     */
    fun clearTrail(player: Player) {
        player.removeAttribute(attribute)
        player.removeAttribute(indexAttribute)
        setVarp(player, varp, 0)
    }

    /**
     * Whether the player currently has an active trail. Default implementation
     * returns false; can be overridden in subclasses.
     */
    open fun hasTrail(player: Player): Boolean = false

    /**
     * Gives rewards (or plays fail animation) and schedules post-animation logic.
     */
    fun reward(player: Player, success: Boolean) {
        player.lock()
        player.animator.animate(if (success) catchingKebbitAnimation else catchingFailAnimation)
        playAudio(player, Sounds.HUNTING_NOOSE_2637)

        GameWorld.Pulser.submit(
            object : Pulse(catchingKebbitAnimation.duration) {
                override fun pulse(): Boolean {
                    if (hasTrail(player) && success) {
                        rewards.forEach { item ->
                            if (!player.inventory.add(item)) GroundItemManager.create(item, player)
                        }
                        player.skills.addExperience(Skills.HUNTER, experience)
                        clearTrail(player)
                    }
                    player.unlock()
                    return true
                }
            },
        )
    }

    /**
     * Updates varbits for trail progression up to the player.
     */
    private fun updateTrail(player: Player) {
        val trail = player.getAttribute(attribute, arrayListOf<TrailDefinition>())
        val trailIndex = player.getAttribute(indexAttribute, 0)
        for (index in 0..trailIndex) {
            val trl = trail[index]
            setVarbit(player, trl.varbit, (if (trl.inverted) 1 else 0) or (1 shl 2))
        }
    }

    /**
     * Handles interactions (attack / inspect / search) with tracking nodes.
     */
    override fun handle(player: Player?, node: Node?, option: String?): Boolean {
        node ?: return true
        player ?: return true

        val trail = player.getAttribute(attribute, arrayListOf<TrailDefinition>())
        val currentIndex = player.getAttribute(indexAttribute, 0)

        if (!hasTrail(player) && !initialMap.containsKey(node.id)) {
            sendDialogue(player, "You search but find nothing.")
            return true
        }

        val currentTrail = if (hasTrail(player)) {
            if (currentIndex < trail.lastIndex) trail[currentIndex + 1] else trail[currentIndex]
        } else {
            TrailDefinition(0, TrailType.LINKING, false, Location(0, 0, 0), Location(0, 0, 0), Location(0, 0, 0))
        }

        when (option) {
            "attack" -> {
                if (!hasNooseWand(player)) {
                    sendDialogue(player, "You need a noose wand to catch the kebbit.")
                    return true
                }
                if (currentIndex == trail.lastIndex && currentTrail.endLocation == node.location) {
                    reward(player, true)
                } else {
                    reward(player, false)
                }
            }

            "inspect", "search" -> {
                if (!hasTrail(player)) {
                    if (player.skills.getLevel(Skills.HUNTER) < requiredLevel) {
                        sendDialogue(player, "You need a Hunter level of $requiredLevel to track these.")
                        return true
                    }
                    generateTrail(node.asScenery(), player)
                    updateTrail(player)
                } else {
                    if (currentTrail.triggerObjectLocation == node.location || (currentIndex == trail.lastIndex && currentTrail.endLocation == node.location)) {
                        if (currentIndex == trail.lastIndex) {
                            sendDialogue(player, "It looks like something is moving around in there.")
                        } else {
                            sendDialogue(player, "You discover some tracks nearby.")
                            player.incrementAttribute(indexAttribute)
                            updateTrail(player)
                        }
                    } else {
                        sendDialogue(player, "You search but find nothing of interest.")
                    }
                }
            }
        }
        return true
    }

    /**
     * Whether the player has a noose wand in equipment or inventory.
     */
    private fun hasNooseWand(player: Player): Boolean =
        inEquipment(player, Items.NOOSE_WAND_10150, 1) || inInventory(player, Items.NOOSE_WAND_10150, 1)
}