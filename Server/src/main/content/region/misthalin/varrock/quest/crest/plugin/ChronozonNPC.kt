package content.region.misthalin.varrock.quest.crest.plugin

import core.api.getQuestStage
import core.api.sendMessage
import core.api.setQuestStage
import core.game.node.entity.Entity
import core.game.node.entity.combat.BattleState
import core.game.node.entity.combat.CombatStyle
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.world.map.Location
import shared.consts.NPCs
import shared.consts.Quests

class ChronozonNPC(id: Int, location: Location) : AbstractNPC(id, location) {

    private var targetPlayer: Player? = null

    private var airDamage   = 0
    private var waterDamage = 0
    private var earthDamage = 0
    private var fireDamage  = 0

    override fun construct(id: Int, location: Location?, vararg objects: Any?): AbstractNPC {
        return ChronozonNPC(id, location ?: Location(3085, 9936, 0))
    }

    override fun getIds(): IntArray = intArrayOf(NPCs.CHRONOZON_667)

    override fun checkImpact(state: BattleState?) {
        state ?: return
        val missingWeakness = airDamage == 0 || waterDamage == 0 || earthDamage == 0 || fireDamage == 0
        if (missingWeakness) {
            if (state.style != CombatStyle.MAGIC) {
                state.neutralizeHits()
                return
            }

            if (state.totalDamage >= skills.lifepoints) {
                state.neutralizeHits()
            }
        }

        val spell = state.spell ?: return

        when (spell.spellId) {

            24 -> { // Air Bolt
                if (state.totalDamage > 0 && airDamage == 0) {
                    targetPlayer?.let { sendMessage(it,"Chronozon weakens...") }
                }
                airDamage += state.totalDamage
            }

            27 -> { // Water Bolt
                if (state.totalDamage > 0 && waterDamage == 0) {
                    targetPlayer?.let { sendMessage(it,"Chronozon weakens...") }
                }
                waterDamage += state.totalDamage
            }

            33 -> { // Earth Bolt
                if (state.totalDamage > 0 && earthDamage == 0) {
                    targetPlayer?.let { sendMessage(it,"Chronozon weakens...") }
                }
                earthDamage += state.totalDamage
            }

            38 -> { // Fire Bolt
                if (state.totalDamage > 0 && fireDamage == 0) {
                    targetPlayer?.let { sendMessage(it,"Chronozon weakens...") }
                }
                fireDamage += state.totalDamage
            }
        }
    }

    override fun finalizeDeath(killer: Entity?) {
        val player = killer as? Player
        val target = targetPlayer

        if (player != null && player == target) {
            if (getQuestStage(player, Quests.FAMILY_CREST) != 20) {
                setQuestStage(player, Quests.FAMILY_CREST, 20)
            }
        }

        super.finalizeDeath(killer)
        clear()
    }

    fun setPlayer(player: Player) {
        targetPlayer = player
    }

    override fun init() {
        airDamage   = 0
        waterDamage = 0
        earthDamage = 0
        fireDamage  = 0
        super.init()
    }
}