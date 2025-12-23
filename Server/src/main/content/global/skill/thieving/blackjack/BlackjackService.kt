package content.global.skill.thieving.blackjack

import content.global.skill.slayer.items.MirrorShieldHandler.calculateHit
import core.api.*
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.world.update.flag.context.Animation
import core.tools.RandomFunction
import shared.consts.Animations
import core.game.world.GameWorld

object BlackjackService {
    private val ticks: Int
        get() = GameWorld.ticks

    /**
     * Attempt to lure the NPC.
     */
    fun lure(player: Player, npc: NPC) {
        sendMessage(player, "You attempt to lure the ${npc.name.lowercase()}.")
        npc.face(player)
    }

    /**
     * Attempt to knock out the NPC with a blackjack.
     */
    fun knockOut(player: Player, npc: NPC) {
        val blackjack = getEquippedBlackjack(player) ?: run {
            sendMessage(player, "You need a blackjack to do this.")
            return
        }

        val npcData = BlackjackDefinition.forId(npc.id) ?: run {
            sendMessage(player, "You can't do that.")
            return
        }

        if(isStunned(player)){
            sendMessage(player, "You're stunned!")
            return
        }

        if (getStatLevel(player, Skills.THIEVING) < npcData.thievingLevel) {
            sendMessage(player, "You need a Thieving level of ${npcData.thievingLevel} to do this.")
            return
        }

        if (RandomFunction.random(100) >= knockOutChance(player, npc)) {
            fail(player, npc)
        } else {
            success(player, npc, npcData, blackjack)
        }
    }

    /**
     * Handle a successful knockout.
     */
    private fun success(player: Player, npc: NPC, data: BlackjackDefinition, blackjack: BlackjackType) {
        val state = BlackjackState(
            unconsciousUntil = ticks + 1,
            pickpocketsLeft = 2
        )
        npc.attributes["blackjack"] = state

        player.animate(Animation(393))
        npc.animator.animate(Animation(838))
        npc.sendChat("Arghh my head.")
        npc.sendChat("Zzzzzzz")

        val maxHit = calculateHit(player, npc, 1.0)
        if (maxHit > 0) {
            val playerRoll = RandomFunction.random(0, maxHit)
            val npcRoll = RandomFunction.random(0, npc.skills.lifepoints)
            if (playerRoll > npcRoll) {
                val stunTicks = getStunTicks(blackjack)
                stun(npc, stunTicks)
            }
        }

        rewardXP(player, Skills.THIEVING, data.xp)
        sendMessage(player, "You smack the ${npc.name.lowercase()} over the head and render them unconscious.")
    }

    /**
     * Determines knock-out success chance based on player thieving level.
     */
    private fun knockOutChance(player: Player, npc: NPC): Int {
        val npcData = BlackjackDefinition.forId(npc.id) ?: return 0
        return if (npcData == BlackjackDefinition.MENAPHITE_THUG) {
            val level = getStatLevel(player, Skills.THIEVING).coerceIn(1, 99)
            // linear interpolation: 31 + (level - 1) * (94 - 31) / (99 - 1)
            31 + ((level - 1) * 63 / 98)
        } else {
            failChance(player, npc)
        }
    }

    /**
     * Handle a failed knockout attempt.
     */
    private fun fail(player: Player, npc: NPC) {
        npc.animator.animate(Animation(Animations.ATTACK_395))
        impact(player, 2)
        stun(player, 3)
        npc.sendChat("I'll kill you for that!")
        sendMessage(player, "You blow only glances off the ${npc.name.lowercase()} head.")
    }

    /**
     * Cleanup knock-out state for NPC if unconscious time expired and no pickpockets left.
     */
    fun updateBlackjackState(npc: NPC) {
        val state = npc.attributes["blackjack"] as? BlackjackState ?: return
        if (!state.isUnconscious(ticks) && state.pickpocketsLeft <= 0) {
            npc.attributes.remove("blackjack")
            npc.animator.reset()
        }
    }

    /**
     * Returns the blackjack equipped by the player, if any.
     */
    private fun getEquippedBlackjack(player: Player): BlackjackType? =
        BlackjackType.fromItem(player.equipment.getId(3))

    /**
     * Determines fail chance for knockout attempt.
     */
    private fun failChance(player: Player, npc: NPC): Int {
        val levelDiff = getStatLevel(player, Skills.THIEVING) - (BlackjackDefinition.forId(npc.id)?.thievingLevel ?: 0)
        return (50 - levelDiff).coerceIn(5, 95)
    }

    /**
     * Get the number of stun ticks for a blackjack.
     */
    private fun getStunTicks(blackjack: BlackjackType): Int = when (blackjack) {
        BlackjackType.ORDINARY -> 1
        BlackjackType.OFFENSIVE -> 2
        BlackjackType.DEFENSIVE -> 3
    }
}