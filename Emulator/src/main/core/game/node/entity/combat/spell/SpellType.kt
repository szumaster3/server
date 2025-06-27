package core.game.node.entity.combat.spell

import core.api.hasTimerActive
import core.game.container.impl.EquipmentContainer
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills

/**
 * Represents the spell types.
 *
 * @author Emperor
 */
enum class SpellType(val accuracyMod: Double) {
    STRIKE(1.0) {
        override fun getImpactAmount(e: Entity, victim: Entity?, base: Int): Int {
            if (victim is NPC && victim.id == 205) {
                return 8 + base
            }
            return 2 * base
        }
    },

    BOLT(1.1) {
        override fun getImpactAmount(e: Entity, victim: Entity?, base: Int): Int {
            if (e is Player && e.equipment.getNew(EquipmentContainer.SLOT_HANDS).id == 777) {
                return 11 + base
            }
            return 8 + base
        }
    },

    CRUMBLE_UNDEAD(1.2) {
        override fun getImpactAmount(e: Entity, victim: Entity?, base: Int): Int = 15
    },

    BLAST(1.2) {
        override fun getImpactAmount(e: Entity, victim: Entity?, base: Int): Int = 12 + base
    },

    WAVE(1.3) {
        override fun getImpactAmount(e: Entity, victim: Entity?, base: Int): Int = 16 + base
    },

    RUSH(1.1) {
        override fun getImpactAmount(e: Entity, victim: Entity?, base: Int): Int = 14 + base
    },

    BURST(1.2) {
        override fun getImpactAmount(e: Entity, victim: Entity?, base: Int): Int = 18 + base
    },

    BLITZ(1.3) {
        override fun getImpactAmount(e: Entity, victim: Entity?, base: Int): Int = 22 + base
    },

    BARRAGE(1.4) {
        override fun getImpactAmount(e: Entity, victim: Entity?, base: Int): Int = 26 + base
    },

    CONFUSE(1.15),
    WEAKEN(1.15),
    CURSE(1.15),
    VULNERABILITY(1.25),
    ENFEEBLE(1.25),
    STUN(1.25),
    GOD_STRIKE(1.2) {
        override fun getImpactAmount(e: Entity, victim: Entity?, base: Int): Int {
            if (e !is Player) return 20
            if (hasTimerActive(e, "magic:spellcharge")) {
                val cape = e.equipment.getNew(EquipmentContainer.SLOT_CAPE)
                if (cape.id == 2412 || cape.id == 2413 || cape.id == 2414) {
                    return 30
                }
            }
            return 20
        }
    },
    BIND(1.1),
    SNARE(1.2),
    ENTANGLE(1.3),
    MAGIC_DART(1.15) {
        override fun getImpactAmount(e: Entity, victim: Entity?, base: Int): Int = 10 + (e.getSkills().getLevel(Skills.MAGIC) / 10)
    },

    IBANS_BLAST(1.4) {
        override fun getImpactAmount(e: Entity, victim: Entity?, base: Int): Int = 25
    },

    TELEBLOCK(1.3),
    NULL(0.0), ;

    open fun getImpactAmount(e: Entity, victim: Entity?, base: Int): Int = 2
}
