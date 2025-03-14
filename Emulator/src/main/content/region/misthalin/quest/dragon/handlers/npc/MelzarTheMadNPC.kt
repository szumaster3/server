package content.region.misthalin.quest.dragon.handlers.npc

import content.region.misthalin.quest.dragon.DragonSlayer
import core.game.node.entity.Entity
import core.game.node.entity.combat.*
import core.game.node.entity.combat.equipment.ArmourSet
import core.game.node.entity.combat.spell.CombatSpell
import core.game.node.entity.npc.AbstractNPC
import core.game.node.entity.player.Player
import core.game.node.entity.player.link.SpellBookManager
import core.game.node.entity.skill.Skills
import core.game.node.item.GroundItemManager
import core.game.node.item.Item
import core.game.world.map.Location
import core.game.world.map.RegionManager.isTeleportPermitted
import core.tools.RandomFunction
import org.rs.consts.Items
import org.rs.consts.NPCs

class MelzarTheMadNPC : AbstractNPC {
    private val combatHandler = MelzarSwingHandler()

    constructor() : super(0, null)

    private constructor(id: Int, location: Location) : super(id, location)

    override fun construct(
        id: Int,
        location: Location,
        vararg objects: Any,
    ): AbstractNPC {
        return MelzarTheMadNPC(id, location)
    }

    override fun init() {
        super.init()
        getSkills().setLevel(Skills.MAGIC, 40)
        getSkills().setLevel(Skills.ATTACK, 30)
        getSkills().setLevel(Skills.STRENGTH, 35)
        getSkills().setLevel(Skills.DEFENCE, 10)
    }

    override fun handleTickActions() {
        super.handleTickActions()
        if (!DeathTask.isDead(this) && properties.combatPulse.isAttacking && RandomFunction.random(0, 4) == 1) {
            sendChat(MESSAGES[RandomFunction.random(MESSAGES.size)])
        }
    }

    override fun finalizeDeath(killer: Entity) {
        super.finalizeDeath(killer)
        GroundItemManager.create(DragonSlayer.PURPLE_KEY, getLocation(), (killer as Player))
    }

    override fun getSwingHandler(swing: Boolean): CombatSwingHandler {
        return combatHandler
    }

    override fun getIds(): IntArray {
        return intArrayOf(NPCs.MELZAR_THE_MAD_753)
    }

    inner class MelzarSwingHandler : CombatSwingHandler(CombatStyle.MAGIC) {
        private var style = CombatStyle.MAGIC
        private val SPELL_IDS = intArrayOf(8, 2, 7, 11)

        override fun canSwing(
            entity: Entity,
            victim: Entity,
        ): InteractionType? {
            return type!!.swingHandler.canSwing(entity, victim)
        }

        override fun swing(
            entity: Entity?,
            victim: Entity?,
            state: BattleState?,
        ): Int {
            style =
                if (RandomFunction.random(5) == 3) {
                    CombatStyle.MELEE
                } else {
                    CombatStyle.MAGIC
                }
            return 2
        }

        override fun visualize(
            entity: Entity,
            victim: Entity?,
            state: BattleState?,
        ) {
            if (style == CombatStyle.MAGIC) {
                state!!.spell = combatSpell
                for (i in 0..1) {
                    val l = getLocation().transform(RandomFunction.random(1, 5), RandomFunction.random(1, 5), 0)
                    if (isTeleportPermitted(l) &&
                        GroundItemManager.get(
                            CABBAGE.id,
                            l,
                            null,
                        ) == null &&
                        l.y <= 9651 &&
                        l.y >= 9644
                    ) {
                        if (victim is Player) victim.packetDispatch.sendPositionedGraphic(86, 1, 0, l)
                        GroundItemManager.create(CABBAGE, l, (victim as Player?))
                    }
                }
            }
            style.swingHandler.visualize(entity, victim, state)
        }

        override fun visualizeImpact(
            entity: Entity?,
            victim: Entity?,
            state: BattleState?,
        ) {
            style.swingHandler.visualizeImpact(entity, victim, state)
        }

        override fun adjustBattleState(
            entity: Entity,
            victim: Entity,
            state: BattleState,
        ) {
            style.swingHandler.adjustBattleState(entity, victim, state)
        }

        override fun calculateAccuracy(entity: Entity?): Int {
            return style.swingHandler.calculateAccuracy(entity)
        }

        override fun calculateDefence(
            victim: Entity?,
            attacker: Entity?,
        ): Int {
            return style.swingHandler.calculateDefence(victim, attacker)
        }

        override fun calculateHit(
            entity: Entity?,
            victim: Entity?,
            modifier: Double,
        ): Int {
            return style.swingHandler.calculateHit(entity, victim, modifier)
        }

        override fun impact(
            entity: Entity?,
            victim: Entity?,
            state: BattleState?,
        ) {
            style.swingHandler.impact(entity, victim, state)
        }

        override fun getArmourSet(e: Entity?): ArmourSet? {
            return style.swingHandler.getArmourSet(e)
        }

        override fun getSetMultiplier(
            e: Entity?,
            skillId: Int,
        ): Double {
            return style.swingHandler.getSetMultiplier(e, skillId)
        }

        val combatSpell: CombatSpell?
            get() = (
                SpellBookManager.SpellBook.MODERN.getSpell(
                    SPELL_IDS[RandomFunction.random(SPELL_IDS.size)],
                ) as CombatSpell?
            )
    }

    companion object {
        val CABBAGE: Item = Item(Items.CABBAGE_1965)
        val MESSAGES =
            arrayOf(
                "Feel the wrath of my feet!",
                "By the power of custard!",
                "Let me drink my tea in peace.",
                "Leave me alone, I need to feed my pet rock.",
            )
    }
}
