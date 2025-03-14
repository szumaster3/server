package content.global.activity.champion.npc

import content.global.activity.champion.ChallengeListener
import core.api.*
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.combat.equipment.WeaponInterface
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
class ImpChampionNPC(
    id: Int = 0,
    location: Location? = null,
) : AbstractNPC(id, location) {
    var clearTime = 0

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC {
        return ImpChampionNPC(id, location)
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.IMP_CHAMPION_3062)
    }

    override fun handleTickActions() {
        super.handleTickActions()
        if (clearTime++ > 288) poofClear(this)
    }

    companion object {
        @JvmStatic
        fun spawnImpChampion(player: Player) {
            val champion = ImpChampionNPC(NPCs.IMP_CHAMPION_3062)
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
            val w = player.getExtension<WeaponInterface>(WeaponInterface::class.java)
            if (state.style == CombatStyle.MELEE ||
                state.style == CombatStyle.MAGIC ||
                state.style == CombatStyle.RANGE
            ) {
                state.neutralizeHits()
                state.estimatedHit = state.maximumHit
            }
            if (w.weaponInterface?.interfaceId == 10) {
                sendMessage(player, "You cannot use special attack in this challenge.")
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
                sendString(killer, "Well done, you defeated the Imp Champion!", Components.CHAMPIONS_SCROLL_63, 2)
                sendItemZoomOnInterface(killer, Components.CHAMPIONS_SCROLL_63, 3, Items.CHAMPION_SCROLL_6803, 260)
                sendString(killer, "160 Slayer Xp", Components.CHAMPIONS_SCROLL_63, 6)
                sendString(killer, "160 Hitpoint Xp", Components.CHAMPIONS_SCROLL_63, 7)
            }
            setVarbit(killer, Vars.VARBIT_SCENERY_CHAMPIONS_CHALLENGE_IMP_BANNER_1457, 1, true)
            rewardXP(killer, Skills.HITPOINTS, 160.0)
            rewardXP(killer, Skills.SLAYER, 160.0)
            removeAttribute("championsarena:start")
            clearHintIcon(killer)
            ChallengeListener.isFinalBattle(killer)
        }
        clear()
        super.finalizeDeath(killer)
    }
}
