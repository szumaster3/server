package content.global.activity.champion.npc

import content.global.activity.champion.ChallengeListener
import core.api.*
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.equipment.Weapon
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills
import core.game.system.task.Pulse
import core.game.world.GameWorld
import core.game.world.map.Location
import core.plugin.Initializable
import org.rs.consts.Components
import org.rs.consts.Items
import org.rs.consts.NPCs
import org.rs.consts.Vars

@Initializable
class JogreChampionNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    var clearTime = 0

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC {
        return JogreChampionNPC(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.JOGRE_CHAMPION_3063)
    }

    override fun handleTickActions() {
        super.handleTickActions()
        if (clearTime++ > 288) poofClear(this)
    }

    companion object {
        @JvmStatic
        fun spawnJogreChampion(player: Player) {
            val champion = JogreChampionNPC(NPCs.JOGRE_CHAMPION_3063)
            champion.location = location(3170, 9758, 0)
            champion.isWalks = true
            champion.isAggressive = true
            champion.isActive = false

            if (champion.asNpc() != null && champion.isActive) {
                champion.properties.teleportLocation = champion.properties.spawnLocation
            }
            champion.isActive = true
            GameWorld.Pulser.submit(
                object : Pulse(0, champion) {
                    override fun pulse(): Boolean {
                        champion.init()
                        registerHintIcon(player, champion)
                        champion.attack(player)
                        return true
                    }
                },
            )
        }
    }

    override fun checkImpact(state: BattleState) {
        super.checkImpact(state)
        val player = state.attacker
        if (player is Player) {
            if (state.style == CombatStyle.MAGIC || state.style == CombatStyle.MELEE) {
                state.neutralizeHits()
                state.estimatedHit = state.maximumHit
            }
            if (state.weapon.type == Weapon.WeaponType.DEGRADING || state.style == CombatStyle.RANGE) {
                sendMessage(player, "You cannot use ranged weapons.")
                if (state.estimatedHit > -1) {
                    state.estimatedHit = 0
                    return
                }
                if (state.secondaryHit > -1) {
                    state.secondaryHit = 0
                    return
                }
            }
        }
    }

    override fun finalizeDeath(killer: Entity?) {
        if (killer is Player) {
            lock(killer, 2)
            runTask(killer, 1) {
                openInterface(killer, Components.CHAMPIONS_SCROLL_63)
                sendString(killer, "Well done, you defeated the Jogre Champion!", Components.CHAMPIONS_SCROLL_63, 2)
                sendItemZoomOnInterface(killer, Components.CHAMPIONS_SCROLL_63, 3, Items.CHAMPION_SCROLL_6804, 260)
                sendString(killer, "480 Slayer Xp", Components.CHAMPIONS_SCROLL_63, 6)
                sendString(killer, "480 Hitpoint Xp", Components.CHAMPIONS_SCROLL_63, 7)
            }
            setVarbit(killer, Vars.VARBIT_SCENERY_CHAMPIONS_CHALLENGE_JOGRE_BANNER_1458, 1, true)
            rewardXP(killer, Skills.HITPOINTS, 480.0)
            rewardXP(killer, Skills.SLAYER, 480.0)
            removeAttribute("championsarena:start")
            clearHintIcon(killer)
            ChallengeListener.isFinalBattle(killer)
        }
        clear()
        super.finalizeDeath(killer)
    }
}
