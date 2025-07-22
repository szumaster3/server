package core.game.node.entity.combat.spell

import core.api.hasTimerActive
import core.game.container.impl.EquipmentContainer
import core.game.node.entity.Entity
import core.game.node.entity.npc.NPC
import core.game.node.entity.player.Player
import core.game.node.entity.skill.Skills

/**
 * Represents different types of magic spells, each with an accuracy modifier and
 * a method to calculate the spell's impact amount (damage or effect).
 *
 * @property accuracyMod Modifier applied to the spell's accuracy.
 */
enum class SpellType(val accuracyMod: Double) {
    STRIKE(1.0) {
        /**
         * Calculates the impact amount for the STRIKE spell.
         * Special case for NPC with id 205.
         */
        override fun getImpactAmount(e: Entity, victim: Entity?, base: Int): Int {
            if (victim is NPC && victim.id == 205) {
                return 8 + base
            }
            return 2 * base
        }
    },

    BOLT(1.1) {
        /**
         * Calculates the impact amount for the BOLT spell.
         * Increases damage if player wields item with id 777 in hands.
         */
        override fun getImpactAmount(e: Entity, victim: Entity?, base: Int): Int {
            if (e is Player && e.equipment.getNew(EquipmentContainer.SLOT_HANDS).id == 777) {
                return 11 + base
            }
            return 8 + base
        }
    },

    CRUMBLE_UNDEAD(1.2) {
        /**
         * Fixed impact amount of 15 for CRUMBLE_UNDEAD spell.
         */
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

    CONFUSE(1.15), WEAKEN(1.15), CURSE(1.15), VULNERABILITY(1.25), ENFEEBLE(1.25), STUN(1.25),

    GOD_STRIKE(1.2) {
        /**
         * Calculates the impact amount for GOD_STRIKE.
         * Increased damage if player has active spellcharge timer and wears certain capes.
         */
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

    BIND(1.1), SNARE(1.2), ENTANGLE(1.3),

    MAGIC_DART(1.15) {
        /** Impact amount scales with caster's magic level for MAGIC_DART. */
        override fun getImpactAmount(e: Entity, victim: Entity?, base: Int): Int =
            10 + (e.getSkills().getLevel(Skills.MAGIC) / 10)
    },

    IBANS_BLAST(1.4) {
        override fun getImpactAmount(e: Entity, victim: Entity?, base: Int): Int = 25
    },

    TELEBLOCK(1.3), NULL(0.0);

    /**
     * Gets the impact amount (damage or effect strength) of the spell.
     *
     * @param e The caster entity.
     * @param victim The target entity.
     * @param base The base damage value.
     * @return Calculated impact amount.
     */
    open fun getImpactAmount(e: Entity, victim: Entity?, base: Int): Int = 2
}