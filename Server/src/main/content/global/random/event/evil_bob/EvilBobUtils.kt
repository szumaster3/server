package content.global.random.event.evil_bob

import content.data.GameAttributes
import content.data.RandomEvent
import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.world.map.zone.ZoneBorders
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import shared.consts.Animations
import shared.consts.Items
import shared.consts.NPCs
import shared.consts.Scenery

/**
 * Utils for Evil Bob rando event.
 */
object EvilBobUtils {
    const val EVIL_BOB_NPC_ID = NPCs.EVIL_BOB_2479
    const val SERVANT_NPC_ID  = NPCs.SERVANT_2481
    const val EXIT_PORTAL     = Scenery.PORTAL_8987

    const val COOKING_POT  = Scenery.UNCOOKING_POT_8985
    const val FISHING_SPOT = Scenery.FISHING_SPOT_8986

    val RAW_FISH_CORRECT_IDS   = intArrayOf(Items.FISHLIKE_THING_6202, Items.FISHLIKE_THING_6206)
    val RAW_FISh_INCORRECT_IDS = intArrayOf(Items.RAW_FISHLIKE_THING_6200, Items.RAW_FISHLIKE_THING_6204)

    val COOK_ANIMATION    = Animation(Animations.OLD_COOK_FIRE_897)
    val FISHING_ANIMATION = Animation(Animations.NET_FISHING_1903)

    val NORTH_FISHING_ZONE = ZoneBorders(3421, 4789, 3427, 4792)
    val EAST_FISHING_ZONE  = ZoneBorders(3437, 4774, 3440, 4780)
    val SOUTH_FISHING_ZONE = ZoneBorders(3419, 4763, 3426, 4765)
    val WEST_FISHING_ZONE  = ZoneBorders(3405, 4773, 3408, 4779)

    fun giveEventFishingSpot(player: Player) {
        val zones = listOf(
            NORTH_FISHING_ZONE,
            SOUTH_FISHING_ZONE,
            EAST_FISHING_ZONE,
            WEST_FISHING_ZONE
        )

        val randomZone = zones.random()
        setAttribute(player, GameAttributes.RE_BOB_ZONE, randomZone.toString())
    }

    /**
     * Cleans up the event.
     *
     * @param player The player.
     */
    fun cleanup(player: Player) {
        if(inInventory(player, Items.SMALL_FISHING_NET_303))
            removeItem(player, Items.SMALL_FISHING_NET_303)
        player.locks.unlockTeleport()
        player.properties.teleportLocation = getAttribute(player, RandomEvent.save(), null)
        removeAttributes(
            player,
            GameAttributes.RE_BOB_ZONE,
            GameAttributes.RE_BOB_COMPLETE,
            RandomEvent.save(),
            GameAttributes.RE_BOB_ALERT,
            GameAttributes.RE_BOB_DIAL,
            GameAttributes.RE_BOB_DIAL_INDEX,
            GameAttributes.RE_BOB_START,
        )
        removeAll(player, Items.FISHLIKE_THING_6202)
        removeAll(player, Items.FISHLIKE_THING_6202, Container.BANK)
        removeAll(player, Items.FISHLIKE_THING_6206)
        removeAll(player, Items.FISHLIKE_THING_6206, Container.BANK)
        removeAll(player, Items.RAW_FISHLIKE_THING_6200)
        removeAll(player, Items.RAW_FISHLIKE_THING_6200, Container.BANK)
        removeAll(player, Items.RAW_FISHLIKE_THING_6204)
        removeAll(player, Items.RAW_FISHLIKE_THING_6204, Container.BANK)
    }

    fun reward(player: Player) {
        val experience = 650.0
        val hasHighMagic = getStatLevel(player, Skills.MAGIC) > 50

        val skill = if (hasHighMagic) {
            if (RandomFunction.random(2) == 0) Skills.FISHING else Skills.MAGIC
        } else {
            Skills.FISHING
        }

        rewardXP(player, skill, experience)
        openDialogue(
            player,
            EvilBobDialogue(rewardDialogue = true, rewardXpSkill = skill),
            NPCs.EVIL_BOB_2479
        )
    }
}
