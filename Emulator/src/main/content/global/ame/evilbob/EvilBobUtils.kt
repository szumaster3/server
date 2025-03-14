package content.global.ame.evilbob

import content.data.GameAttributes
import content.data.RandomEvent
import core.api.*
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.world.map.zone.ZoneBorders
import core.game.world.update.flag.context.Animation
import core.game.world.update.flag.context.Graphics
import core.tools.RandomFunction
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Scenery

object EvilBobUtils {
    const val evilBob = NPCs.EVIL_BOB_2479
    const val servant = NPCs.SERVANT_2481
    const val exitPortal = Scenery.PORTAL_8987

    const val uncookingPot = Scenery.UNCOOKING_POT_8985
    const val fishingSpot = 8986

    val fishlikeThings = intArrayOf(Items.FISHLIKE_THING_6202, Items.FISHLIKE_THING_6206)
    val rawFishlikeThings = intArrayOf(Items.RAW_FISHLIKE_THING_6200, Items.RAW_FISHLIKE_THING_6204)

    val cookAnim = Animation(897)
    val fishAnim = Animation(1903)
    val teleAnim = Animation(714)
    val telegfx = Graphics(308, 100, 50)

    val northFishingZone = ZoneBorders(3421, 4789, 3427, 4792)
    val eastFishingZone = ZoneBorders(3437, 4774, 3440, 4780)
    val southFishingZone = ZoneBorders(3419, 4763, 3426, 4765)
    val westFishingZone = ZoneBorders(3405, 4773, 3408, 4779)

    fun giveEventFishingSpot(player: Player) {
        when (RandomFunction.getRandom(3)) {
            0 -> setAttribute(player, GameAttributes.RE_BOB_ZONE, northFishingZone.toString())
            1 -> setAttribute(player, GameAttributes.RE_BOB_ZONE, southFishingZone.toString())
            2 -> setAttribute(player, GameAttributes.RE_BOB_ZONE, eastFishingZone.toString())
            3 -> setAttribute(player, GameAttributes.RE_BOB_ZONE, westFishingZone.toString())
            else -> setAttribute(player, GameAttributes.RE_BOB_ZONE, northFishingZone.toString())
        }
    }

    fun cleanup(player: Player) {
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
        removeItem(player, Items.SMALL_FISHING_NET_303)
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
        if (getStatLevel(player, Skills.MAGIC) > 50) {
            when (RandomFunction.getRandom(1)) {
                0 -> {
                    rewardXP(player, Skills.FISHING, experience)
                    openDialogue(
                        player,
                        EvilBobDialogue(rewardDialogue = true, rewardXpSkill = Skills.FISHING),
                        NPCs.EVIL_BOB_2479,
                    )
                }

                1 -> {
                    rewardXP(player, Skills.MAGIC, experience)
                    openDialogue(
                        player,
                        EvilBobDialogue(rewardDialogue = true, rewardXpSkill = Skills.MAGIC),
                        NPCs.EVIL_BOB_2479,
                    )
                }
            }
        } else {
            rewardXP(player, Skills.FISHING, experience)
            openDialogue(
                player,
                EvilBobDialogue(rewardDialogue = true, rewardXpSkill = Skills.FISHING),
                NPCs.EVIL_BOB_2479,
            )
        }
    }
}
