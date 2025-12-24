package content.global.skill.thieving.blackjack

import content.global.skill.slayer.items.MirrorShieldHandler.calculateHit
import content.global.skill.thieving.blackjack.timer.BlackjackUnconsciousTimer
import content.global.skill.thieving.blackjack.timer.BlackjackUnconsciousTimer.Companion.BLACKJACK_UNCONSCIOUS
import core.api.*
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import shared.consts.Animations
import core.api.stun
import core.api.impact
import core.api.rewardXP
import core.game.interaction.MovementPulse
import core.game.node.entity.impl.PulseType
import core.game.world.map.RegionManager
import core.tools.secondsToTicks

object BlackjackService {

    /**
     * Attempt to lure the NPC.
     */
    fun lure(player: Player, npc: NPC) {
        if(player.inCombat()) return
        sendMessage(player, "You attempt to lure the ${npc.name.lowercase()}.")
        npc.walkingQueue.reset()
        npc.pulseManager.run(getFollowPulse(npc, player), PulseType.STANDARD)

    }

    // Idk
    private fun getFollowPulse(npc: NPC, target: Player, durationSeconds: Int = 10): MovementPulse {
        val maxTicks = secondsToTicks(durationSeconds)
        return object : MovementPulse(npc, target) {
            var ticks = 0

            override fun pulse(): Boolean {
                ticks++
                return ticks >= maxTicks
            }
        }
    }

    /**
     * Attempt to knock out the NPC with a blackjack.
     */
    fun knockOut(player: Player, npc: NPC) {
        if(player.inCombat()) return

        val blackjack = getEquippedBlackjack(player) ?: run {
            sendMessage(player, "You need a blackjack to do this.")
            return
        }

        val npcData = BlackjackNPC.forId(npc.id) ?: run {
            sendMessage(player, "You can't do that.")
            return
        }

        if (isStunned(player)) {
            sendMessage(player, "You're stunned!")
            return
        }

        if (getDynLevel(player, Skills.THIEVING) < npcData.thievingLevel) {
            sendMessage(player, "You need a Thieving level of ${npcData.thievingLevel} to do this.")
            return
        }

        if (RandomFunction.random(100) >= knockOutChance(player, npc)) {
            fail(player, npc)
            return
        }

        removeTimer(npc, BLACKJACK_UNCONSCIOUS)
        registerTimer(npc, spawnTimer<BlackjackUnconsciousTimer>(8, 2))

        player.animate(Animation(393))

        val maxHit = calculateHit(player, npc, 1.0)
        if (maxHit > 0 && RandomFunction.random(0, maxHit) >
            RandomFunction.random(0, npc.skills.lifepoints)) {
            stun(npc, getStunTicks(blackjack))
        }

        rewardXP(player, Skills.THIEVING, npcData.xp)
        sendMessage(player, "You smack the ${npc.name.lowercase()} over the head and render them unconscious.")
    }

    /**
     * Notify when another player interact with an NPC that is currently knocked.
     */
    fun notify(player: Player, npc: NPC) {
        val timer = getTimer<BlackjackUnconsciousTimer>(npc) ?: return
        if (timer.remainingTicks <= 0 || timer.pickpocketsLeft <= 0) return

        val players = RegionManager.getLocalPlayers(npc, 1)
        for (p in players) {
            if (p != player) {
                sendMessage(p, "I'm teaching coshing here, not pickpocketing.")
                p.lock(1)
            }
        }
    }

    /**
     * Determines knock-out success chance.
     */
    private fun knockOutChance(player: Player, npc: NPC): Int {
        val data = BlackjackNPC.forId(npc.id) ?: return 0
        val level = player.skills.getLevel(Skills.THIEVING)
        return if (data == BlackjackNPC.MENAPHITE_THUG) {
            val clamped = level.coerceIn(1, 99)
            31 + ((clamped - 1) * 63 / 98)
        } else {
            (50 + (level - data.thievingLevel) * 2).coerceIn(5, 95)
        }
    }

    /**
     * Handle a failed knockout attempt.
     */
    private fun fail(player: Player, npc: NPC) {
        val coshingNPC = BlackjackNPC.forId(npc.id)
        player.lock(1)
        npc.faceLocation(player.location)
        npc.sendChat("I'll kill you for that!")
        npc.animator.animate(Animation(Animations.ATTACK_395))
        coshingNPC?.damage?.let { impact(player, it) }
        stun(player, 3, false)
        sendMessage(player, "You blow only glances off the ${npc.name.lowercase()} head.")
    }

    /**
     * Returns the blackjack equipped by the player, if any.
     */
    private fun getEquippedBlackjack(player: Player): BlackjackType? =
        BlackjackType.fromItem(player.equipment.getId(3))

    /**
     * Returns whether the NPC can currently be pickpocketed.
     */
    fun canPickpocket(npc: NPC): Boolean {
        val timer = getTimer<BlackjackUnconsciousTimer>(npc)
        return timer != null && timer.remainingTicks > 0 && timer.pickpocketsLeft > 0
    }

    /**
     * Called when a player successfully pickpockets the NPC.
     */
    fun onPickpocket(npc: NPC) {
        val timer = getTimer<BlackjackUnconsciousTimer>(npc) ?: return
        timer.pickpocketsLeft--
        if (timer.pickpocketsLeft <= 0) {
            removeTimer(npc, BlackjackUnconsciousTimer.BLACKJACK_UNCONSCIOUS)
        }
    }

    /**
     * Get the number of stun ticks for a blackjack.
     */
    fun getStunTicks(blackjack: BlackjackType): Int = when (blackjack) {
        BlackjackType.ORDINARY -> 1
        BlackjackType.OFFENSIVE -> 2
        BlackjackType.DEFENSIVE -> 3
    }
}
